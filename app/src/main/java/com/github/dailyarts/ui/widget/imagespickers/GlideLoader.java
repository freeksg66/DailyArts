package com.github.dailyarts.ui.widget.imagespickers;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.dailyarts.R;

/**
 * Created by ZhangCong on 18/3/20.
 */

public class GlideLoader implements ImageLoader {

    private static final long serialVersionUID = 1L;

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.image_selector_default_img)
                .centerCrop()
                .into(imageView);
    }
}
