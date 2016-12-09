package edu.uit.dictplus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;

import edu.uit.dictplus.Activity_Question.Activity_ListComment.ParseComment;
import edu.uit.dictplus.Activity_Question.Activity_ListQuestion.ParseQuestion;
import edu.uit.dictplus.Activity_Question.Activity_Question;
import edu.uit.dictplus.ParseHistory.FragmentAllHistory;
import edu.uit.dictplus.ParseHistory.FragmentHistory;
import edu.uit.dictplus.ParseHistory.History;
import edu.uit.dictplus.ParseHistory.TabPublic;
import edu.uit.dictplus.TraTu.Fragment_TraTu;
import edu.uit.dictplus.TraTu.Tab_AnhViet;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class MainActivity extends AppCompatActivity implements Tab_AnhViet.OnHeadlineSelectedListener {


    public static boolean HocTuLichSu=true;


    private Toolbar mToolbar;
    public static android.support.v7.app.ActionBar actionBar;
    private FragmentDrawer drawerFragment;
    FrameLayout frameTraTu,frameHistory,frameStudy;
    RelativeLayout SplashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ParseObject.registerSubclass(History.class);
        ParseObject.registerSubclass(ParseQuestion.class);
        ParseObject.registerSubclass(ParseComment.class);
        SplashScreen=(RelativeLayout)findViewById(R.id.layoutSplashScreen);

        frameTraTu=(FrameLayout)findViewById(R.id.fra_tratu);
        frameHistory=(FrameLayout)findViewById(R.id.fra_history);
        frameStudy=(FrameLayout)findViewById(R.id.fra_study);
        frameTraTu.setVisibility(View.GONE);
        frameHistory.setVisibility(View.GONE);
        frameStudy.setVisibility(View.GONE);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar=getSupportActionBar();

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(new FragmentDrawer.FragmentDrawerListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                displayView(position);
            }
        }, getApplicationContext());


        for(int i=0;i<3;i++){
            if(i==0) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fra_tratu, new Fragment_TraTu());


                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle("Tra từ");
            }
            if(i==1) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fra_history, new TabPublic());

                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle("Lịch sử");
            }
            if(i==2) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
              fragmentTransaction.replace(R.id.fra_study, new FragmentStudy());


                fragmentTransaction.commit();

                // set the toolbar title
                getSupportActionBar().setTitle("Học từ vựng");

            }
        }
        // display the first navigation drawer view on app launch
        displayView(1);

    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

    }


    private void displayView(int position) {

        switch (position) {
            case 0:
                frameTraTu.setVisibility(View.VISIBLE);
                frameHistory.setVisibility(View.INVISIBLE);
                frameStudy.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Tra Từ");
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case 1:

                frameTraTu.setVisibility(View.INVISIBLE);
                frameHistory.setVisibility(View.VISIBLE);
                frameStudy.setVisibility(View.INVISIBLE);
                getSupportActionBar().setTitle("Lịch Sử");


                break;
            case 2:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                HocTuLichSu=false;
                                FragmentStudy.mListDataTracNghiem = FragmentAllHistory.getDataTracNghiem();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                FragmentStudy.mListDataTracNghiem = FragmentHistory.getDataTracNghiem();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Chọn dữ liệu để học").setPositiveButton("Cộng Đồng", dialogClickListener)
                        .setNegativeButton("Lịch Sử", dialogClickListener).show();

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                frameTraTu.setVisibility(View.INVISIBLE);
                frameHistory.setVisibility(View.INVISIBLE);
                frameStudy.setVisibility(View.VISIBLE);

                getSupportActionBar().setTitle("Học từ vựng");
                break;
            case 3:
                Intent activity_qes=new Intent(MainActivity.this, Activity_Question.class);
                startActivity(activity_qes);
                break;
            case 4:
                ParseObject.unpinAllInBackground("History");
                ParseUser.logOutInBackground();
                Intent login=new Intent(MainActivity.this, Activity_Login.class);
                login.putExtra("Login",false);
                startActivity(login);
                finish();
                break;
            default:
                break;
        }



    }


    public void onArticleSelected(int position) {
        Toast.makeText(MainActivity.this, "Ok Let Go", Toast.LENGTH_SHORT).show();
        SplashScreen.setVisibility(View.GONE);
    }
}
