package com.github.dailyarts.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.utils.ImageLoadUtils;
import com.github.dailyarts.utils.ToastUtils;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class WatchImageFragment extends BaseFragment {
    private static final String TAG = "WatchImageFragment";
    private TextView tvDownload;
    private AppActionBar appActionBar;
    private SubsamplingScaleImageView ssivImage;

    private boolean showActionBar = true;
    private boolean isZoomEnabled = false;

    private String bigImageUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bigImageUrl = getArguments().getString("BigImageUrl");
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_watch_image;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        tvDownload = appActionBar.findViewById(R.id.tv_download_image);
        appActionBar.showDownload();
        ssivImage = rootView.findViewById(R.id.ssiv_watch_big_image);
        ssivImage.setMinScale(0.5F);
        ssivImage.setMaxScale(10F);
        Glide.with(this)
                .load(bigImageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ssivImage.post(() -> ssivImage.setImage(ImageSource.bitmap(resource), initImageViewState(resource.getWidth(), resource.getHeight(), ssivImage.getMeasuredWidth(), ssivImage.getMeasuredHeight())));
                    }
                });
        ssivImage.setZoomEnabled(false);
        ssivImage.setPanEnabled(false);
        ssivImage.setOnClickListener(v -> {
            isZoomEnabled = !isZoomEnabled;
            ssivImage.setZoomEnabled(isZoomEnabled);
            ssivImage.setPanEnabled(isZoomEnabled);
            startActionBarAnim();
        });
        tvDownload.setOnClickListener(v -> saveImage());
    }

    private ImageViewState initImageViewState(int imgWidth, int imgHeight, int viewWidth, int viewHeight) {
        PointF center = new PointF(imgWidth / 2, imgHeight / 2);
        int orientation = 0;
        float scale = 1.0F;
        float scaleX = (float) viewWidth / imgWidth;
        float scaleY = (float) viewHeight / imgHeight;
        if (imgWidth > 0 && imgHeight > 0 && viewWidth > 0 && viewHeight > 0) {
            scale = scaleX > scaleY ? scaleX : scaleY;
        }
        return new ImageViewState(scale, center, orientation);
    }

    private void saveImage() {
        Glide.with(this)
                .load(bigImageUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ImageLoadUtils.saveImageToGallery(getContext(), resource);
                        ToastUtils.show(getContext(), "保存成功");
                    }
                });
    }

    private void startActionBarAnim() {
        showActionBar = !showActionBar;
        if (showActionBar) {
            alphaAnim(appActionBar, 0F, 1F);
        } else {
            alphaAnim(appActionBar, 1F, 0F);
        }
    }

    private void alphaAnim(View view, float start, float end) {
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", start, end);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.setStartDelay(0);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.start();
    }
}
