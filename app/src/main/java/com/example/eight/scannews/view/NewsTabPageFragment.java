package com.example.eight.scannews.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eight.scannews.R;
import com.example.eight.scannews.utils.ChannelsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class NewsTabPageFragment extends Fragment {

    private static List<String> newsTab = new ArrayList<>();

    public static List<String> getNewsTab() {
        return newsTab;
    }

    public static void setNewsTab(List<String> newsTab) {
        NewsTabPageFragment.newsTab = newsTab;
    }

    @Override
    public void onAttach(Context context) {                                                                          //当一个Fragment对象关联到一个Activity时调用
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {                                                 //创建Fragment时被回调
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,                                                       //每次创建、绘制该Fragment的View组件时回调该方法，Fragment将会显示该方法返回的View 组件
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_tab_page_layout, null);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);

        newsTab =  ChannelsUtils.setupTab("cn");
        viewPager.setOffscreenPageLimit(1);                                                                  //预加载页数
        initViewPager(viewPager);
        for (String aNewsTab : newsTab) {
            tabLayout.addTab(tabLayout.newTab().setText(aNewsTab));
        }

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void initViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < newsTab.size(); i++) {
            adapter.addFragment(NewsListFragment.newInstance(i), newsTab.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {                                        //当 Fragment 所在的Activity被启动完成后回调该方法
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {                                                                                       //启动 Fragment 时被回调，此时Fragment可见
        super.onStart();
    }

    @Override
    public void onResume() {                                                                                //恢复 Fragment 时被回调，获取焦点时回调
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {                                                               //销毁与Fragment有关的视图，但未与Activity解除绑定
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {                                                            //与onAttach相对应，当Fragment与Activity关联被取消时调用
        super.onDetach();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }


        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
