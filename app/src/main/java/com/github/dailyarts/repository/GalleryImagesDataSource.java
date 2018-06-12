package com.github.dailyarts.repository;

import com.github.dailyarts.entity.GalleryModel;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.entity.ImageModel;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by legao005426 on 2018/5/9.
 */

public interface GalleryImagesDataSource {
    Observable<Response<ImageMessageModel>> getGalleryImage(int date);
}
