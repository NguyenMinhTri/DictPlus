package edu.uit.dictplus.ParseHistory;

/**
 * Created by Ravi on 29/07/15.
 */

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import edu.uit.dictplus.R;


public class TabPublic extends Fragment {



    //boolean dangnhap;
    public TabPublic() {

        // Required empty public constructor
    }


    private static TabLayout tabLayout;
    public FrameLayout fragmentUser,fragmentPublic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_history, container, false);
        fragmentUser=(FrameLayout)rootView.findViewById(R.id.frameUser);
        fragmentPublic=(FrameLayout)rootView.findViewById(R.id.framePublic);


        fragmentPublic.setVisibility(View.GONE);

        tabLayout=null;

        Log.d("This is the output", "Khởi tạo lại");
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Lịch Sử"));
        tabLayout.addTab(tabLayout.newTab().setText("Cộng Đồng"));
        {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameUser, new FragmentHistory());
            fragmentTransaction.commit();
        }
        {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.framePublic, new FragmentAllHistory());
            fragmentTransaction.commit();
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0) {
                    fragmentUser.setVisibility(View.VISIBLE);
                    fragmentPublic.setVisibility(View.GONE);
                }
                if(tab.getPosition()==1) {
                    fragmentUser.setVisibility(View.GONE);
                    fragmentPublic.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }



}
