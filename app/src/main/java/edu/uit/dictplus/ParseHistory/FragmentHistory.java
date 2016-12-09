package edu.uit.dictplus.ParseHistory;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.uit.dictplus.Activity_Login;
import edu.uit.dictplus.ActivityTabStudy.DataTracNghiem;
import edu.uit.dictplus.R;


public class FragmentHistory extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
    boolean isScrolling=false;
    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    //
    boolean isRefresh=false;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static ListView mListView;
    public static  HistoryAdapter mAdapter;
    public static List<History> tempHistory=new ArrayList<>();
    boolean dangnhap=false;
    public FragmentHistory() {
     //   dangnhap=dn;
        // Required empty public constructor
    }
    @Override
    public void onRefresh() {
        fetchMovies();
    }
    private void fetchMovies() {
        // showing refresh animation before making http call
        isRefresh=true;
        swipeRefreshLayout.setRefreshing(true);
        tempHistory=new ArrayList<>();
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        String user= ParseUser.getCurrentUser().getUsername();
        query.whereEqualTo("User", user);
        query.setLimit(1000);
        query.addDescendingOrder("STT");

    //    query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> tasks, ParseException error) {
                if (tasks != null) {
                    try {
                        ParseObject.unpinAll("History");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    ParseObject.pinAllInBackground("History", tasks);
                    tempHistory.clear();
                    for (int i = 0; i < tasks.size(); i++) {

                        tempHistory.add(tasks.get(i));

                    }

                    mAdapter = new HistoryAdapter(getActivity(), tempHistory, mTouchListener, true);

                    mListView.setAdapter(mAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    isRefresh = false;
                }

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    public static List<DataTracNghiem> getDataTracNghiem()
    {
        List<DataTracNghiem> data=new ArrayList<>();


        try {
            int isCheckint = mListView.getCount();
            if(isCheckint<4)
                return data;
        }catch (Exception e){
            return data;
        }
        Integer[] arr = new Integer[mListView.getCount()];
        for (int i = 0; i < mListView.getCount(); i++) {
            arr[i]= i;
        }
        Collections.shuffle(Arrays.asList(arr));

        for(int i=0;i<mListView.getCount();i++)
        {
            boolean isCheck = mAdapter.getItem(i).getCheck();
            if(isCheck)
            {
                Collections.shuffle(Arrays.asList(arr));
                Integer[] tempData=new Integer[4];
                int dem=0;
                arr[3]=i;

                tempData[0]=arr[0];
                tempData[1]=arr[1];
                tempData[2]=arr[2];
                tempData[3]=arr[3];


                Collections.shuffle(Arrays.asList(tempData));

                //
                String dapan=mAdapter.getItem(i).getMean();//((TextView) getViewByPosition(i, mListView).findViewById(R.id.lvtvMean)).getText().toString();
                String Voca=mAdapter.getItem(i).getVocabulay();//((TextView) getViewByPosition(i, mListView).findViewById(R.id.lvtvVoca)).getText().toString();
                String dapanA=mAdapter.getItem(tempData[dem++]).getMean();//((TextView) getViewByPosition(tempData[dem++], mListView).findViewById(R.id.lvtvMean)).getText().toString();
                String dapanB=mAdapter.getItem(tempData[dem++]).getMean();//((TextView) getViewByPosition(tempData[dem++], mListView).findViewById(R.id.lvtvMean)).getText().toString();
                String dapanC=mAdapter.getItem(tempData[dem++]).getMean();//((TextView) getViewByPosition(tempData[dem++], mListView).findViewById(R.id.lvtvMean)).getText().toString();
                String dapanD=mAdapter.getItem(tempData[dem++]).getMean();//((TextView) getViewByPosition(tempData[dem++], mListView).findViewById(R.id.lvtvMean)).getText().toString();

                data.add(new DataTracNghiem(Voca,dapan,dapanD,dapanC,dapanB,dapanA));
            }
        }

        return  data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */

        mListView=(ListView)rootView.findViewById(R.id.listViewHistory);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                try {
                    if (scrollState == 1)
                        handler.removeCallbacks(mLongPressed);
                }
                catch (Exception e)
                {}


            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        initDataParse();




        // Inflate the layout for this fragment
        return rootView;
    }
    public  boolean isOnline() {

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }
    public void initDataParse(){
        //khoi tao history
        if(isOnline() && Activity_Login.Login) {
            try {
                tempHistory = new ArrayList<>();
                ParseQuery<History> query = ParseQuery.getQuery(History.class);
                String user = ParseUser.getCurrentUser().getUsername();
                //  query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.whereEqualTo("User", user);
                query.setLimit(1000);
                query.orderByDescending("createdAt");
                query.findInBackground(new FindCallback<History>() {
                    @Override
                    public void done(List<History> tasks, ParseException error) {
                        if (error == null) {
                            ParseObject.unpinAllInBackground("History");
                            ParseObject.pinAllInBackground("History", tasks);

                            tempHistory.clear();
                            for (int i = 0; i < tasks.size(); i++) {

                                tempHistory.add(tasks.get(i));

                            }

                            mAdapter = new HistoryAdapter(getActivity(), tempHistory, mTouchListener, true);

                            mListView.setAdapter(mAdapter);
                        }

                    }
                });
            }catch (Exception e)
            {

            }
        }else {
            try {
                tempHistory = new ArrayList<>();
                ParseQuery<History> query = ParseQuery.getQuery(History.class);
                //query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query.fromPin("History");
                String user = ParseUser.getCurrentUser().getUsername();
                query.whereEqualTo("User", user);
                query.setLimit(1000);
                query.orderByDescending("STT");
                query.findInBackground(new FindCallback<History>() {
                    @Override
                    public void done(List<History> tasks, ParseException error) {
                        if (error == null) {

                            tempHistory.clear();
                            for (int i = 0; i < tasks.size(); i++) {

                                tempHistory.add(tasks.get(i));

                            }

                            mAdapter = new HistoryAdapter(getActivity(), tempHistory, mTouchListener, true);

                            mListView.setAdapter(mAdapter);
                        }

                    }
                });
            } catch (Exception e) {

            }

        }

    }
    Runnable mLongPressed;
    Handler handler;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {


            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                handler = new Handler();
                mLongPressed = new Runnable() {
                    public void run() {
                        try {
                            if(!isRefresh ) {
                                int position = mListView.getPositionForView(v);
                                Intent acPopEd = new Intent(getActivity(), PopupEditHistory.class);
                                acPopEd.putExtra("Voca", mAdapter.getItem(position).getVocabulay());
                                acPopEd.putExtra("Mean", mAdapter.getItem(position).getMean());
                                acPopEd.putExtra("DauNhan", mAdapter.getItem(position).getDauNhan());
                                acPopEd.putExtra("Position", position);
                                startActivity(acPopEd);
                            }
                        }catch (Exception e){}

                    }
                };
              //  if(!isRefresh && !isScrolling)
                handler.postDelayed(mLongPressed, 1000);
            }
            if((event.getAction() == MotionEvent.ACTION_MOVE)||(event.getAction() == MotionEvent.ACTION_UP)){
                handler.removeCallbacks(mLongPressed);
              //  isScrolling=false;

            }
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(getActivity()).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {
                        // Multi-item swipes not handled
                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1);
                    v.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                {
                    float x = event.getX() + v.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            mListView.requestDisallowInterceptTouchEvent(true);

                        }
                    }
                    if (mSwiping) {
                        v.setTranslationX((x - mDownX));
                        v.setAlpha(1 - deltaXAbs / v.getWidth());
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                {

                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + v.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean remove;
                        if (deltaXAbs > v.getWidth() / 4) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / v.getWidth();
                            endX = deltaX < 0 ? -v.getWidth() : v.getWidth();
                            endAlpha = 0;
                            remove = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / v.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            remove = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        mListView.setEnabled(false);
                        try {
                            v.animate().setDuration(duration).
                                    alpha(endAlpha).translationX(endX).
                                    withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Restore animated values
                                            v.setAlpha(1);
                                            v.setTranslationX(0);
                                            if (remove) {
                                                animateRemoval(mListView, v);
                                            } else {
                                                //   Toast.makeText(getActivity(), "Edit", Toast.LENGTH_SHORT).show();
                                                mSwiping = false;
                                                mListView.setEnabled(true);
                                            }
                                        }
                                    });
                        }catch (Exception e){}
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    /**
     * This method animates all other views in the ListView container (not including ignoreView)
     * into their final positions. It is called after ignoreView has been removed from the
     * adapter, but before layout has been run. The approach here is to figure out where
     * everything is now, then allow layout to run, then figure out where everything is after
     * layout, and then to run animations between all of those start/end positions.
     */
    private void animateRemoval(final ListView listview, View viewToRemove) {
        int firstVisiblePosition = listview.getFirstVisiblePosition();
        for (int i = 0; i < listview.getChildCount(); ++i) {
            View child = listview.getChildAt(i);
            if (child != viewToRemove) {
                int position = firstVisiblePosition + i;
                long itemId = mAdapter.getItemId(position);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        int position = mListView.getPositionForView(viewToRemove);
        try {
            mAdapter.getItem(position).deleteEventually();

            mAdapter.remove(mAdapter.getItem(position));


        } catch (Exception e) {
            e.printStackTrace();
        }



        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = mAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(MOVE_DURATION).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {

                                        mSwiping = false;
                                        mListView.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(MOVE_DURATION).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {

                                    mSwiping = false;
                                    mListView.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

}

