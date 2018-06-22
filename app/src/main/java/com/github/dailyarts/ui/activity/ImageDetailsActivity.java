package com.github.dailyarts.ui.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.dailyarts.R;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.fragment.ImageDetailsFragment;
import com.github.dailyarts.utils.SharedPreferencesUtils;

import java.time.LocalDate;

/**
 * Created by legao005426 on 2018/5/18.
 */

@Route(path = RouterConstant.ImageDetailsActivityConst.PATH)
public class ImageDetailsActivity extends BaseActivity {
    @Autowired
    ImageModel imageModel;

    private ImageDetailsFragment mImageDetailsFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_painting_details;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_painting_details_activity_content;
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BigImage", imageModel);
        bundle.putBoolean("HasCollected", SharedPreferencesUtils.checkCollect(this, imageModel));
        mImageDetailsFragment = getStoredFragment(ImageDetailsFragment.class, bundle);
        addFragment(mImageDetailsFragment);
    }

    @Override
    public void onBackPressed() {
        if (mImageDetailsFragment != null) {
            mImageDetailsFragment.backFunction();
        } else {
            super.onBackPressed();
        }
    }

}
