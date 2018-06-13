package com.github.dailyarts.contract;

import com.github.dailyarts.entity.ImageModel;

import java.util.List;

/**
 * Created by legao005426 on 2018/6/13.
 */

public interface FindArtsContract {
    interface IView {
        void showImageList(List<ImageModel> list);
        void showNothing();
    }

    interface IPresenter {
        void searchImages(String keyWords);
    }
}
