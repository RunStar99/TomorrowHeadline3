package com.example.eight.TomorrowHeadline.utils;

import com.example.eight.TomorrowHeadline.beans.NewsBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface ApiService {
    @GET("{channel}/")
    Observable<NewsBean> getNews(@Path("channel") String channel,
                                 @Query("key") String key,
                                 @Query("num") int num,
                                 @Query("page") int page);
}
