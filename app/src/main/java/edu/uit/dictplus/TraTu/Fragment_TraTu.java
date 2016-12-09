package edu.uit.dictplus.TraTu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uit.dictplus.R;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class Fragment_TraTu extends Fragment {
    public Fragment_TraTu(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tratu,container,false);
        TabAdapter adapter;
        final ViewPager viewPager;
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_tratu);
     //   tabLayout.setTabTextColors();



        tabLayout.addTab(tabLayout.newTab().setText("Anh Việt"));
        tabLayout.addTab(tabLayout.newTab().setText("Việt Anh"));
        tabLayout.addTab(tabLayout.newTab().setText("Chuyên Ngành"));
        tabLayout.setTabTextColors(getResources().getColor(R.color.button_material_light), getResources().getColor(R.color.button_material_dark));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter=new TabAdapter(getChildFragmentManager(),tabLayout.getTabCount());

        viewPager   = (ViewPager) view.findViewById(R.id.view_tratu);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            //
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return  view;
    }

}
