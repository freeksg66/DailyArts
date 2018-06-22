package com.github.dailyarts.net;

import com.github.dailyarts.entity.ImageMessageListModel;
import com.github.dailyarts.entity.ImageMessageModel;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class DailyArtsAPI {
    private static class InstanceHolder {
        static Api INSTANCE = ServiceFactory.createService("http://dailyarts.sinaapp.com/api/", Api.class);
    }

    public interface Api {
        // 按日期获取图片
        @FormUrlEncoded
        @POST("info")
        Observable<Response<ImageMessageModel>> getImageMessage(@Field("date") int date);

        // 按关键字搜索图片
        @GET("search")
        Observable<Response<ImageMessageListModel>> searchImageMessage(@Query("devicetype") String deviceType, @Query("key") String key, @Query("productid") int id, @Query("version") String version);
    }

    public static Api getInstance() {
        return InstanceHolder.INSTANCE;
    }
}
