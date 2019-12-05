package com.example.d.linking.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.d.linking.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Follow extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Context mContext;

    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#2c3130'>Linking</font>"));
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("팔로워"));
        mTabLayout.addTab(mTabLayout.newTab().setText("팔로잉"));

        mViewPager = (ViewPager) findViewById(R.id.pager_content);
        mContentsPagerAdapter = new ContentsPagerAdapter(
                getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mContentsPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //toolbar 뒤로가기 버튼
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //viewpager adapter
    private class ContentsPagerAdapter extends FragmentStatePagerAdapter {
        private int mPageCount;

        public ContentsPagerAdapter(FragmentManager fm, int pageCount) {
            super(fm);
            this.mPageCount = pageCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Fragment_follower fragment_follower = new Fragment_follower();
                    return fragment_follower;

                case 1:
                    Fragment_following fragment_following = new Fragment_following();
                    return fragment_following;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mPageCount;
        }
    }
}
