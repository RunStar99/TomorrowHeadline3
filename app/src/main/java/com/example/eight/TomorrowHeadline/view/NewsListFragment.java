package com.example.eight.TomorrowHeadline.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eight.TomorrowHeadline.utils.NewsAdapter;
import com.example.eight.TomorrowHeadline.R;
import com.example.eight.TomorrowHeadline.beans.NewsBean;
import com.example.eight.TomorrowHeadline.contract.Contract;
import com.example.eight.TomorrowHeadline.presenter.NewsPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class NewsListFragment extends Fragment
        implements Contract.NewsView, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView newsListView;
    private LinearLayoutManager layoutManager;
    private Contract.NewsPresenter newsPresenter;
    private NewsAdapter newsAdapter;


    private List<NewsBean.NewslistBean> data;
    private int type = 0;
    private int pageIndex = 0;
    private final int pageSize = 10;
    private final String key = "27fe422dbc0fafc86d0be396ea5761e0";

    public static NewsListFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        bundle.putInt("type", type);
        Log.e("----->", "newInstance: " + type);
        fragment.setArguments(bundle);                                                              //.setArguments 官方推荐的传参方法
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsPresenter = new NewsPresenterImpl(getContext(), this);
        type = getArguments().getInt("type");
        Log.e("---------->", "onCreate: " + type);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_list_fragment, null);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        newsListView = view.findViewById(R.id.news_list_view);
        Log.e("----->", "onCreateView: ");

        swipeRefreshLayout.setColorSchemeResources(                                                                 //设置下拉进度条颜色变化
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(this);                                                                  //监听界面的滑动

        newsListView.setHasFixedSize(true);                                                                             //避免重复计算item高度
        layoutManager = new LinearLayoutManager(getActivity());
        newsListView.setLayoutManager(layoutManager);
        newsListView.setItemAnimator(new DefaultItemAnimator());                                                             //设置动画
        newsListView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));                                                                            //设置分割线
        newsAdapter = new NewsAdapter(getActivity().getApplicationContext());
        newsAdapter.setOnItemClickListener(onItemClickListener);
        newsListView.setAdapter(newsAdapter);
        newsListView.addOnScrollListener(onScrollListener);                                                                 //滚动事件监听
        onRefresh();
        return view;
    }



    private NewsAdapter.OnItemClickListener onItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (data.size() <= 0) {
                return;
            }
            NewsBean.NewslistBean newslistBean = newsAdapter.getItem(position);
            // 跳转
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            ArrayList<String> news = new ArrayList<>();
            news.add(newslistBean.getTitle());
            news.add(newslistBean.getUrl());
            news.add(newslistBean.getPicUrl());
            Log.e(TAG, "onItemClick: ---> " + news.toString());
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("news", news);
            intent.putExtras(bundle);
            View transitionView = view.findViewById(R.id.news_picture);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), transitionView, "news_picture");     //转场动画
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
    };


    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {    /*监听滑动状态的变化*/
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE      //开始滚动（SCROLL_STATE_FLING），正在滚动(SCROLL_STATE_TOUCH_SCROLL), 已经停止（SCROLL_STATE_IDLE）
                    && lastVisibleItem + 1 == newsAdapter.getItemCount()             //判断到达底部
                    && newsAdapter.isShowFooter()) {
                newsPresenter.loadNews(type, key, pageSize, pageIndex);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        }
    };

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);                                                     //设置刷新的状态
    }

    @Override
    public void addNews(List<NewsBean.NewslistBean> newsBeanList) {
        newsAdapter.setShowFooter(true);
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(newsBeanList);                                       //传入整个list

        if (pageIndex == 0) {
            newsAdapter.setData(data);
        } else {
            if (newsBeanList.size() == 0) {
                newsAdapter.setShowFooter(false);
            }
            newsAdapter.notifyDataSetChanged();
        }
        pageIndex ++;
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingFail() {
        if (pageIndex == 0) {
            newsAdapter.setShowFooter(false);
            newsAdapter.notifyDataSetChanged();
        }
        View view = getActivity() == null ? newsListView.getRootView()
                : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, "加载失败...", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        pageIndex = 0;
        if (data != null) {
            data.clear();
        }
        newsPresenter.loadNews(type, key, pageSize, pageIndex);
    }

}
