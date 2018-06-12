package com.github.dailyarts.net;

import com.github.dailyarts.entity.ImageMessageModel;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class DailyArtsAPI {
    private static class InstanceHolder{
        static Api INSTANCE = ServiceFactory.createService("http://dailyarts.sinaapp.com/api/", Api.class);
    }

    public interface Api{
        @FormUrlEncoded
        @POST("info")
        Observable<Response<ImageMessageModel>> getImageMessage(@Field("date") int date);
    }

    public static Api getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
