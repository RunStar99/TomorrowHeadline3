package com.example.eight.TomorrowHeadline.presenter;

import android.content.Context;

import com.example.eight.TomorrowHeadline.utils.ChannelsUtils;
import com.example.eight.TomorrowHeadline.beans.NewsBean;
import com.example.eight.TomorrowHeadline.contract.Contract;
import com.example.eight.TomorrowHeadline.model.NewsModelImpl;

import java.util.List;

/**
 *
 */

public class NewsPresenterImpl implements Contract.NewsPresenter, Contract.OnLoadNewsListListener{

    private Contract.NewsModel newsModel;
    private Contract.NewsView newsView;

    public NewsPresenterImpl(Context context, Contract.NewsView newsView) {
        this.newsModel = new NewsModelImpl(context);
        this.newsView = newsView;
    }

    @Override
    public void loadNews(int type, String key, int num, int page) {
        //List<Channels> channelsList = DataSupport.findAll(Channels.class);
        // tab 列表
        List<String> tabList = ChannelsUtils.setupTab("en");
        if (page == 0) {
            newsView.showProgress();
        }
        if (tabList == null) throw new AssertionError();
        newsModel.loadNews(tabList.get(type), key, num, page, this);
    }

    @Override
    public void onSuccess(List<NewsBean.NewslistBean> list) {
        newsView.hideProgress();
        newsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        newsView.hideProgress();
        newsView.showLoadingFail();
    }
}
