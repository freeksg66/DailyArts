package com.github.dailyarts.entity;

/**
 * Created by legao005426 on 2018/5/7.
 */

public class GalleryModel {
    private ImageModel imageModel;
    private int month;
    private int day;
    private int resId;

    public GalleryModel(ImageModel imageModel, int month, int day) {
        this.imageModel = imageModel;
        this.month = month;
        this.day = day;
    }

    public GalleryModel(int resId, int month, int day) {
        this.resId = resId;
        this.month = month;
        this.day = day;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getResId() {
        return resId;
    }
}
