package elli.nutritionapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private static final int TABS_COUNT = 3;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // Enable tabs to be displayed in the action bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // When the user swipes between the tabs, set the given tab selected.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Add tabs to the action bar
        for(int i = 0; i < TABS_COUNT; i++ ){
            actionBar.addTab(actionBar.newTab()
                    .setText("Tab")
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // When the user selects a given tab, switch to that tab in the ViewPager
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}
