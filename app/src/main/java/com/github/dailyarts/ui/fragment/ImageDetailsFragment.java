package com.github.dailyarts.ui.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.github.dailyarts.ui.activity.WatchImageActivity;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.utils.ImageLoadUtils;
import com.github.dailyarts.utils.SharedPreferencesUtils;
import com.github.dailyarts.utils.ToastUtils;

import java.io.File;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class ImageDetailsFragment extends BaseFragment {
    private static final String TAG = "ImageDetailsFragment";

    private AppActionBar appActionBar;
    private SubsamplingScaleImageView ssivBigImage;
    private ImageView ivCover;
    private TextView tvImageName, tvImageAuthor;
    private TextView tvImageIntro;
    private ImageView ivZoom, ivComment, ivCollection, ivDownload, ivShare;
    private RelativeLayout rlCover;
    private ScrollView svIntro;
    private RelativeLayout rlTools;
    private RelativeLayout rlNextPage;

    private ImageModel mImageModel;
    private boolean hasCollected = false;

    private boolean isZoomEnable = false;
    private boolean isFirstPage = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mImageModel = getArguments().getParcelable("BigImage");
        hasCollected = getArguments().getBoolean("HasCollected", false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_painting_details;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        ssivBigImage = rootView.findViewById(R.id.ssiv_details_big_image);
        ivCover = rootView.findViewById(R.id.iv_details_cover_image);
        tvImageName = rootView.findViewById(R.id.tv_details_image_name);
        tvImageAuthor = rootView.findViewById(R.id.tv_details_image_author);
        tvImageIntro = rootView.findViewById(R.id.tv_details_image_introduction);
        ivZoom = rootView.findViewById(R.id.iv_details_zoom);
        ivComment = rootView.findViewById(R.id.iv_details_comment);
        ivCollection = rootView.findViewById(R.id.iv_detail_collection);
        ivDownload = rootView.findViewById(R.id.iv_details_download);
        ivShare = rootView.findViewById(R.id.iv_details_share);
        rlCover = rootView.findViewById(R.id.rl_details_cover);
        svIntro = rootView.findViewById(R.id.sv_details_introduction);
        rlTools = rootView.findViewById(R.id.rl_details_tools);
        rlNextPage = rootView.findViewById(R.id.rl_details_next_page);

        ssivBigImage.setMinScale(0.5F);
        ssivBigImage.setMaxScale(10F);
        Glide.with(this)
                .load(mImageModel.getBigImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ViewTreeObserver observer =  ssivBigImage.getViewTreeObserver();
                        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                if(observer.isAlive()){
                                    observer.removeOnPreDrawListener(this);
                                }
                                ssivBigImage.setImage(ImageSource.bitmap(resource.copy(resource.getConfig(), true)), initImageViewState(resource.getWidth(), resource.getHeight(), ssivBigImage.getMeasuredWidth(), ssivBigImage.getMeasuredHeight()));
                                return true;
                            }
                        });
                    }
                });
        Glide.with(this)
                .load(mImageModel.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivCover);
        tvImageName.setText(mImageModel.getName());
        tvImageAuthor.setText(mImageModel.getAuthor());
        tvImageIntro.setText(mImageModel.getDetail());

        isZoomEnable = false;
        ssivBigImage.setZoomEnabled(false);

        isFirstPage = true;

        if(hasCollected){
            ivCollection.setImageResource(R.drawable.collection_true);
        }else {
            ivCollection.setImageResource(R.drawable.collection_false);
        }

        initListener();
    }

    private void initListener(){
        ssivBigImage.setOnClickListener(v -> {
            Log.e(TAG, "SSIV click! isZoomEnable="+String.valueOf(isZoomEnable));
            isZoomEnable = !isZoomEnable;
            ssivBigImage.setZoomEnabled(isZoomEnable);
            animZoomEnable(isZoomEnable);
        });
        rlCover.setOnClickListener(v -> {
            isFirstPage = !isFirstPage;
            animJumpPage(isFirstPage);
        });
        ivZoom.setOnClickListener(v ->
            RouterManager
                    .getInstance()
                    .startActivity(RouterConstant.WatchImageActivityConst.PATH, RouterConstant.WatchImageActivityConst.BIG_IMAGE_URL, mImageModel.getBigImg())
        );
        ivComment.setOnClickListener(v -> ToastUtils.show(getContext(), "评论功能暂未开放！"));
        ivCollection.setOnClickListener(v -> collectImage());
        ivDownload.setOnClickListener(v -> saveImage());
    }

    private void collectImage(){
        if(mImageModel == null) return;
        if(hasCollected){
            if(SharedPreferencesUtils.deleteCollectImage(getContext(), mImageModel)) {
                ToastUtils.ShowCenter(getContext(), "取消收藏成功√");
                ivCollection.setImageResource(R.drawable.collection_false);
                hasCollected = false;
            }
            else {
                ToastUtils.ShowCenter(getContext(), "取消收藏失败×");
                ivCollection.setImageResource(R.drawable.collection_true);
            }
        }else {
            if(SharedPreferencesUtils.saveCollectImage(getContext(), mImageModel)) {
                ToastUtils.ShowCenter(getContext(), "收藏成功√");
                ivCollection.setImageResource(R.drawable.collection_true);
                hasCollected = true;
            }
            else {
                ToastUtils.ShowCenter(getContext(), "收藏失败×");
                ivCollection.setImageResource(R.drawable.collection_false);
            }
        }
    }

    private void saveImage(){
        Glide.with(this)
                .load(mImageModel.getBigImg())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ImageLoadUtils.saveImageToGallery(getContext(), resource);
                        ToastUtils.show(getContext(), "保存成功");
                    }
                });
    }

    private void animZoomEnable(boolean enable){
        float start, end;
        int visible;
        if(enable){
            start = 1f;
            end = 0f;
            visible = View.GONE;
        }else {
            start = 0f;
            end = 1f;
            visible = View.VISIBLE;
        }
        alphaAnim(appActionBar, start, end);
        alphaAnim(rlCover, start, end);
        appActionBar.setVisibility(visible);
        rlCover.setVisibility(visible);
    }

    private void animJumpPage(boolean isFirstPage){
        if(isFirstPage){
            moveAnim(ssivBigImage, 0 - rlTools.getBottom(), 0f);
            moveAnim(rlCover, appActionBar.getHeight() - rlNextPage.getHeight(), 0f);
            moveAnim(rlNextPage, 0f, rlNextPage.getBottom());
        }else {
            moveAnim(ssivBigImage, 0f, 0 - ssivBigImage.getBottom());
            moveAnim(rlCover, 0f, 0 - rlCover.getTop() + appActionBar.getBottom());
            rlNextPage.setVisibility(View.VISIBLE);
            moveAnim(rlNextPage, ssivBigImage.getHeight() - appActionBar.getHeight() - rlCover.getHeight(), 0f);
        }
    }

    private ImageViewState initImageViewState(int imgWidth, int imgHeight, int viewWidth, int viewHeight){
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

    /**
     * 动画相关的方法
     */
    private void alphaAnim(View view, float start, float end){
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", start, end);
        alphaAnimation.setDuration(500);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.setStartDelay(0);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.start();
    }

    private void moveAnim(View view, float start, float end){
        ObjectAnimator moveAnimation = ObjectAnimator.ofFloat(view, "translationY", start, end);
        moveAnimation.setDuration(500);
        moveAnimation.setRepeatCount(0);
        moveAnimation.setRepeatMode(ValueAnimator.REVERSE);
        moveAnimation.start();
    }
}
