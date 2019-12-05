package com.example.d.linking.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.d.linking.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Search extends AppCompatActivity {
    private ImageButton btn_back;
    private TabLayout mTabLayout;
    private Context mContext;

    private ViewPager mViewPager;
    private ContentsPagerAdapter mContentsPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        mContext = getApplicationContext();

        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab2);
        mTabLayout.addTab(mTabLayout.newTab().setText("전체"));
        mTabLayout.addTab(mTabLayout.newTab().setText("사용자"));
        mTabLayout.addTab(mTabLayout.newTab().setText("태그"));

        mViewPager = (ViewPager) findViewById(R.id.pager_content2);
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        Drawable ic_id = getResources().getDrawable(R.drawable.search);
        int ic_width = Math.round(14 * density);
        int ic_height = Math.round(14 * density);
        ic_id.setBounds(0, 0, ic_width, ic_height);

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
                    Fragment_All fragment_all = new Fragment_All();
                    return fragment_all;

                case 1:
                    Fragment_User fragment_user = new Fragment_User();
                    return fragment_user;

                case 2:
                    Fragment_Tag fragment_tag = new Fragment_Tag();
                    return fragment_tag;

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
