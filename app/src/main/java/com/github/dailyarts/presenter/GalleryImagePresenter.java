package com.github.dailyarts.presenter;

import com.github.dailyarts.contract.GalleryImagesContract;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.net.NetError;
import com.github.dailyarts.net.NetSubscriber;
import com.github.dailyarts.repository.GalleryImagesRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class GalleryImagePresenter implements GalleryImagesContract.IPresenter {

    private GalleryImagesContract.IView mView;
    private GalleryImagesRepository mGalleryImagesRepository;

    public GalleryImagePresenter(GalleryImagesContract.IView view, GalleryImagesRepository galleryImagesRepository){
        mView = view;
        mGalleryImagesRepository = galleryImagesRepository;
    }

    @Override
    public void getImage(int date) {
        mGalleryImagesRepository.getGalleryImage(date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSubscriber<ImageMessageModel>() {
                    @Override
                    public void onSuccess(ImageMessageModel imageMessageModel) {
                        mView.loadPicture(imageMessageModel.getData());
                    }

                    @Override
                    public void onFailure(NetError error) {
                        mView.loadPictureFail(error.message);
                    }
                });
    }
}
