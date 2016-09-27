package com.wang.android_lib.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.android_lib.R;
import com.wang.android_lib.util.ScreenUtil;

/**
 * 使用方法：
 * 1.xml布局定义fragment,android:name="com.wang.ebook.view.TabLineViewPagerFragment"
 * 2.FragmentActivity中，yourFragment=getSupportFragmentManager().findFragmentById(R.id.your_fragment)
 * 3.yourFragment.setTabsAndFragments(tabTitles, fragments,ResourceUtil.getColor(this, R.color.dark_green));
 */
public class TabLineViewPagerFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private LinearLayout llTabTitles;
    private LinearLayout llTop;
    private ImageView ivTabBar;
    private ViewPager viewPager;

    private int tabColor;
    private int tabBarWidth;
    private int currPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_line_view_pager, null);
        llTabTitles = (LinearLayout) view.findViewById(R.id.ll_tab_titles);
        llTop = (LinearLayout) view.findViewById(R.id.ll_top);
        ivTabBar = (ImageView) view.findViewById(R.id.iv_tab_bar);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        return view;
    }

    /**
     * 设置相邻缓存fragment的距离，默认为1，即缓存相邻的1个
     */
    public void setOffscreenPageLimit(int offscreenPageLimit) {
        viewPager.setOffscreenPageLimit(offscreenPageLimit);
    }

    public void setTabsAndFragments(String[] tabTitles,
                                    Fragment[] fragments, int tabColor) {
        this.tabColor = tabColor;

        tabBarWidth = ScreenUtil.getScreenWidth(getActivity()) / tabTitles.length;
        ivTabBar.getLayoutParams().width = tabBarWidth;

        TabLineViewPagerAdapter adapter = new TabLineViewPagerAdapter(
                getActivity().getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        for (int i = 0; i < tabTitles.length; i++) {
            addPageTitle(tabTitles[i], i);
        }
        ivTabBar.setBackgroundColor(tabColor);
        onPageSelected(0);
    }

    private void addPageTitle(String title, final int position) {
        TextView tvTitle = new TextView(getActivity());
        tvTitle.setText(title);
        tvTitle.setTextColor(Color.BLACK);
        tvTitle.setTextSize(15);
        tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(position, true);
            }
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        lp.bottomMargin = 8;
        lp.topMargin = 8;
        llTabTitles.addView(tvTitle, lp);
    }

    private void resetTextView() {
        for (int i = 0; i < llTabTitles.getChildCount(); i++) {
            TextView tvTitle = (TextView) llTabTitles.getChildAt(i);
            tvTitle.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivTabBar
                .getLayoutParams();
        if (position == currPage) {//由左往右滑动
            lp.leftMargin = (int) (tabBarWidth * (currPage + positionOffset));
        } else {//由右往左滑动
            lp.leftMargin = (int) (tabBarWidth * (currPage - 1 + positionOffset));
        }
        ivTabBar.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        resetTextView();
        TextView tvTitle = (TextView) llTabTitles.getChildAt(position);
        tvTitle.setTextColor(tabColor);
        currPage = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class TabLineViewPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] fragments;

        public TabLineViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    public void setBackgroundResource(int resId) {
        llTop.setBackgroundResource(resId);
    }

}
