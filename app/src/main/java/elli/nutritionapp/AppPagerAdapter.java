package elli.nutritionapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ellina on 02-Oct-15.
 */
public class AppPagerAdapter extends FragmentPagerAdapter {

    private static final int TABS_COUNT = 2;

    public AppPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i){
        switch (i) {
            case 0:
                return new DiaryFragment();
            case 1:
                return new StatsFragment();
            default:
                return new DiaryFragment();
        }
    }

    @Override
    public int getCount(){
        return TABS_COUNT;
    }
}
