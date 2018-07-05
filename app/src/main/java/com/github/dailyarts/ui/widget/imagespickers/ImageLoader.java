package com.github.dailyarts.ui.widget.imagespickers;


import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

public interface ImageLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}