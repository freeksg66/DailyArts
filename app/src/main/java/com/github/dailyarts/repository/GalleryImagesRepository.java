package com.github.dailyarts.repository;

import com.github.dailyarts.R;
import com.github.dailyarts.entity.GalleryModel;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.net.DailyArtsAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by legao005426 on 2018/5/9.
 */

public class GalleryImagesRepository extends BaseRepository implements GalleryImagesDataSource {
    public GalleryImagesRepository(RxLifecycleBinder binder) {
        super(binder);
    }

    @Override
    public Observable<Response<ImageMessageModel>> getGalleryImage(int date) {
        return DailyArtsAPI.getInstance().getImageMessage(date);
    }
}
