package com.github.dailyarts.repository;

import com.github.dailyarts.entity.ImageMessageListModel;
import com.github.dailyarts.entity.ImageMessageModel;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by legao005426 on 2018/6/13.
 */

public interface FindArtsDataSource {
    Observable<Response<ImageMessageListModel>> searchImages(String keyWords);
}
