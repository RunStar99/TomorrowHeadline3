package com.example.eight.scannews.model;

import android.content.Context;
import android.util.Log;

import com.example.eight.scannews.utils.HttpUtils;
import com.example.eight.scannews.beans.NewsBean;
import com.example.eight.scannews.contract.Contract;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 *
 */

public class NewsModelImpl implements Contract.NewsModel {
    private Context context;

    public NewsModelImpl(Context context) {
        this.context = context;
    }

    private static final String BASE_URL = "https://api.tianapi.com/";
    @Override
    public void loadNews(String channel, String key, int num, int page,
                         final Contract.OnLoadNewsListListener listener) {
        HttpUtils.getInstance(BASE_URL)
                .getNewsFromHttp(channel, key, num, page, new Observer<NewsBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull NewsBean newsBean) {
                        List<NewsBean.NewslistBean> newslistBeanList = newsBean.getNewslist();
                        Log.e("--------->", "onNext: " + newslistBeanList.size());
                        listener.onSuccess(newslistBeanList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("---------->", "onError: "
                                + e.getMessage() + "--->"
                                + e.getCause() + "--->"
                                + e.toString() + "--->"
                                + e.getLocalizedMessage());
                        listener.onFailure("Failed...", (Exception) e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i("---------->", "onComplete: ");
                    }
                });
    }

}
