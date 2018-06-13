package com.github.dailyarts.contract;

import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.entity.GalleryModel;
import com.github.dailyarts.entity.ImageModel;

import java.util.List;

/**
 * Created by legao005426 on 2018/5/9.
 */

public interface GalleryImagesContract {
    interface IView{
        void loadPicture(ImageModel imageModel);
        void loadPictureFail(String errorMessage);
    }

    interface IPresenter{
        void getImage(int date);
    }
}
