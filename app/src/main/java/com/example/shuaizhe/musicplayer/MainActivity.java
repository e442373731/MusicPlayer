package com.example.shuaizhe.musicplayer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.Window;

import com.example.shuaizhe.musicplayer.adapter.MusicAdapter;
import com.example.shuaizhe.musicplayer.bean.MusicInfo;
import com.example.shuaizhe.musicplayer.fragments.MusicFragment;
import com.example.shuaizhe.musicplayer.fragments.SingerFragment;
import com.example.shuaizhe.musicplayer.utils.MusicLoder;
import com.example.shuaizhe.musicplayer.views.PagerSlidingTabStrip;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private MusicFragment mMusicFragment;
    private SingerFragment mSingerFragment;
    private DisplayMetrics mDisplayMetrics;
    private PagerSlidingTabStrip mTabs;
    private Toolbar mToolbar;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        initToolbar();
        mDisplayMetrics = getResources().getDisplayMetrics();
        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.view_tabs);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabs.setViewPager(pager);
        setTabsProperties();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private final String[] titles = {"music", "singer"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    if (mMusicFragment == null) {
                        mMusicFragment = new MusicFragment();
                    }
                    return mMusicFragment;
                case 1:
                    if (mSingerFragment == null) {
                        mSingerFragment = new SingerFragment();
                    }
                    return mSingerFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }

    private void setTabsProperties() {
        mTabs.setShouldExpand(true);
        mTabs.setDividerColor(Color.TRANSPARENT);
        mTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics));
        mTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mDisplayMetrics));
        mTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mDisplayMetrics));
        mTabs.setIndicatorColor(Color.parseColor("#45c01a"));
        mTabs.setSelectedTextColor(Color.parseColor("#45c01a"));
        mTabs.setTabBackground(0);
    }

    private void initToolbar(){
        mToolbar = (Toolbar)findViewById(R.id.view_toolbar);
        //mToolbar.setLogo(R.drawable.music_player);
        mToolbar.setSubtitle("郑帅");
        mToolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }
}
