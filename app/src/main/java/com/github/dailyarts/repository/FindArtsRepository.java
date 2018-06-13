package com.github.dailyarts.repository;

import com.github.dailyarts.entity.ImageMessageListModel;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.net.DailyArtsAPI;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by legao005426 on 2018/6/13.
 */

public class FindArtsRepository extends BaseRepository implements FindArtsDataSource{
    public FindArtsRepository(RxLifecycleBinder binder){
        super(binder);
    }

    @Override
    public Observable<Response<ImageMessageListModel>> searchImages(String keyWords) {
        return DailyArtsAPI.getInstance().searchImageMessage("iPhone", keyWords, 1, "1.3.0.7");
    }
}
