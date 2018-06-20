package com.github.dailyarts.event;

import com.github.dailyarts.entity.ImageModel;

/**
 * Created by legao005426 on 2018/6/20.
 */

public class CollectionEvent {
    public ImageModel imageModel;
    public boolean status;

    public CollectionEvent(){
        imageModel = null;
        status = false;
    }

    public CollectionEvent(ImageModel model, boolean status){
        imageModel = model;
        this.status = status;
    }
}
