package com.github.dailyarts.presenter;

import com.github.dailyarts.contract.FindArtsContract;
import com.github.dailyarts.entity.ImageMessageListModel;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.net.NetError;
import com.github.dailyarts.net.NetSubscriber;
import com.github.dailyarts.repository.FindArtsRepository;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by legao005426 on 2018/6/13.
 */

public class FindArtsPresenter implements FindArtsContract.IPresenter {
    private FindArtsContract.IView mView;
    private FindArtsRepository mFindArtsRepository;

    public FindArtsPresenter(FindArtsRepository repository, FindArtsContract.IView view) {
        mFindArtsRepository = repository;
        mView = view;
    }

    @Override
    public void searchImages(String keyWords) {
        mFindArtsRepository.searchImages(keyWords)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSubscriber<ImageMessageListModel>() {
                    @Override
                    public void onSuccess(ImageMessageListModel imageMessageListModel) {
                        if (imageMessageListModel == null || imageMessageListModel.getData() == null || imageMessageListModel.getData().size() <= 0) {
                            mView.showNothing();
                        } else {
                            mView.showImageList(imageMessageListModel.getData());
                        }
                    }

                    @Override
                    public void onFailure(NetError error) {
                        mView.showNothing();
                    }
                });
    }
}
