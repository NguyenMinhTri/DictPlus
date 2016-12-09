package edu.uit.dictplus.TraTu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by nmtri_000 on 12/31/2015.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TabAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab_AnhViet tab1 = new Tab_AnhViet();
                return tab1;
            case 1:
                Tab_VietAnh tab2 = new Tab_VietAnh();
                return tab2;
            case 2:
                Tab_Online tab3 = new Tab_Online();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}