package com.example.eight.TomorrowHeadline.contract;

import com.example.eight.TomorrowHeadline.beans.NewsBean;

import java.util.List;



public interface Contract {
    // Model 层
    interface NewsModel {
        // 加载
        void loadNews(String channel, String key, int num, int page, OnLoadNewsListListener listener);
    }

    // Presenter 层
    interface NewsPresenter {
        // 加载
        void loadNews(int type, String key, int num, int page);
    }

    // View 层
    interface NewsView {
        void showProgress(); // 显示进度条
        void addNews(List<NewsBean.NewslistBean> newsBeanList); // 数据显示
        void hideProgress(); // 隐藏进度条
        void showLoadingFail(); // 加载失败
    }

    // 监听事件
    interface OnLoadNewsListListener {
        void onSuccess(List<NewsBean.NewslistBean> list); // 返回数据成功
        void onFailure(String msg, Exception e); // 返回数据失败
    }
}
