package com.github.dailyarts.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.dailyarts.event.CollectionEvent;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.ui.widget.ShareDialog;
import com.github.dailyarts.utils.DeviceInfo;
import com.github.dailyarts.utils.ImageLoadUtils;
import com.github.dailyarts.utils.SharedPreferencesUtils;
import com.github.dailyarts.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

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
    private LinearLayout llTools;

    private ImageModel mImageModel;
    private boolean hasCollected = false;

    private boolean isZoomEnable = false;
    private boolean isFirstPage = true;
    private boolean collectedStatus = false; // 记录初始关注状态

    private int mBigImageSlideHeight = 0; // 大图滑动距离
    private int mCoverSlideHeight = 0; // 封面滑动距离
    private int mTextSlideHeight = 0; // 描述部分滑动距离
    private int mToolsHeight = 0; // 底部工具栏滑动距离

    private int mImageWidth = 0;
    private int mImageHeight = 0;
    private float mScale = 1.0F;

    private final String APP_ACTION_BAR_TAG = "AppActionBar";
    private final String CORVER_TAG = "Corver";

    public boolean isActivity = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mImageModel = getArguments().getParcelable("BigImage");
        hasCollected = getArguments().getBoolean("HasCollected", false);
        collectedStatus = hasCollected;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_painting_details;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        appActionBar.setTag(APP_ACTION_BAR_TAG);
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
        rlCover.setTag(CORVER_TAG);
        svIntro = rootView.findViewById(R.id.sv_details_introduction);
        llTools = rootView.findViewById(R.id.ll_details_tools);

        ssivBigImage.setMinScale(0.5F);
        ssivBigImage.setMaxScale(10F);
        Glide.with(this)
                .load(mImageModel.getBigImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mImageWidth = resource.getWidth();
                        mImageHeight = resource.getHeight();
                        ssivBigImage.post(() -> {
                            mScale = initImageViewScale(mImageWidth, mImageHeight, ssivBigImage.getMeasuredWidth(), ssivBigImage.getMeasuredHeight());
                            ssivBigImage.setImage(ImageSource.bitmap(resource), initImageViewState(mImageWidth, mImageHeight, ssivBigImage.getMeasuredWidth(), ssivBigImage.getMeasuredHeight()));
                        });
                    }
                });
        Glide.with(this)
                .load(mImageModel.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivCover);
        tvImageName.setText(mImageModel.getName());
        tvImageAuthor.setText(mImageModel.getAuthor());
        tvImageIntro.setText("作品介绍：\n" + mImageModel.getDetail());

        isZoomEnable = false;
        ssivBigImage.setZoomEnabled(false);
        ssivBigImage.setPanEnabled(false);

        isFirstPage = true;

        if(hasCollected){
            ivCollection.setImageResource(R.drawable.collection_true);
        }else {
            ivCollection.setImageResource(R.drawable.collection_false);
        }

        ssivBigImage.post(() -> mBigImageSlideHeight = ssivBigImage.getHeight());
        rlCover.post(() -> mCoverSlideHeight = rlCover.getTop() - appActionBar.getHeight());
        svIntro.post(() -> {
            mTextSlideHeight = ssivBigImage.getHeight() - rlCover.getHeight() - appActionBar.getHeight();
            svIntro.setVisibility(View.GONE);
        });
        llTools.post(() -> {
            mToolsHeight = llTools.getHeight();
            llTools.setVisibility(View.GONE);
        });

        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivity = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivity = false;
    }

    private void initListener(){
        ssivBigImage.setOnClickListener(v -> {
            rlCover.setEnabled(false);
            isZoomEnable = !isZoomEnable;
            ssivBigImage.setZoomEnabled(isZoomEnable);
            ssivBigImage.setPanEnabled(isZoomEnable);
            if(!isZoomEnable){
                SubsamplingScaleImageView.AnimationBuilder animationBuilder= ssivBigImage.animateScaleAndCenter(mScale, new PointF(mImageWidth / 2, mImageHeight / 2));
                if(animationBuilder != null){
                    animationBuilder.start();
                }
            }
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
        ivShare.setOnClickListener(v -> shareToOthers());

        appActionBar.setLeftBackBtnClickListener(v -> backFunction());
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

    private void shareToOthers(){
        if (!DeviceInfo.isNetworkAvailable(getContext())) {
            ToastUtils.show(getContext(), getString(R.string.toast_network_not_enable));
            return;
        }
        String content = "我很喜欢"+mImageModel.getAuthor()+"的《"+mImageModel.getName()+"》，你也来看看吧！";
        ShareDialog shareDialog = ShareDialog.getInstance(getContext(), mImageModel.getPic());
        shareDialog.show(getFragmentManager(), ShareDialog.class.getSimpleName());
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
    }

    private void animJumpPage(boolean isFirstPage){
        if(isFirstPage){
            moveAnim(ssivBigImage, 0 - mBigImageSlideHeight, 0F);
            moveAnim(rlCover, 0 - mCoverSlideHeight, 0F);
            moveAnim(svIntro, 0F, mTextSlideHeight);
            moveAnim(llTools, 0F, mToolsHeight);
        }else {
            if(svIntro.getVisibility() == View.GONE){
                svIntro.setVisibility(View.VISIBLE);
            }
            if(llTools.getVisibility() == View.GONE){
                llTools.setVisibility(View.VISIBLE);
            }
            moveAnim(ssivBigImage, 0F, 0 - mBigImageSlideHeight);
            moveAnim(rlCover, 0F, 0 - mCoverSlideHeight);
            moveAnim(svIntro, mTextSlideHeight, 0F);
            moveAnim(llTools, mToolsHeight, 0F);
        }
    }

    private ImageViewState initImageViewState(int imgWidth, int imgHeight, int viewWidth, int viewHeight){
        PointF center = new PointF(imgWidth / 2, imgHeight / 2);
        int orientation = 0;
        float scale = initImageViewScale(imgWidth, imgHeight, viewWidth, viewHeight);
        return new ImageViewState(scale, center, orientation);
    }

    private float initImageViewScale(int imgWidth, int imgHeight, int viewWidth, int viewHeight){
        float scale = 1.0F;
        float scaleX = (float) viewWidth / imgWidth;
        float scaleY = (float) viewHeight / imgHeight;
        if (imgWidth > 0 && imgHeight > 0 && viewWidth > 0 && viewHeight > 0) {
            scale = scaleX > scaleY ? scaleX : scaleY;
        }
        return scale;
    }

    /**
     * 动画相关的方法
     */
    private void alphaAnim(View view, float start, float end){
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", start, end);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimation.setStartDelay(0);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                String tag = (String)view.getTag();
                if(tag.equals(CORVER_TAG))
                    rlCover.setEnabled(false);
                if(appActionBar.getVisibility() == View.GONE) {
                    appActionBar.setVisibility(View.VISIBLE);
                }
                if(rlCover.getVisibility() == View.GONE) {
                    rlCover.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                String tag = (String)view.getTag();
                if(tag.equals(CORVER_TAG))
                    rlCover.setEnabled(true);
                int visible = isZoomEnable ? View.GONE : View.VISIBLE;
                appActionBar.setVisibility(visible);
                rlCover.setVisibility(visible);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alphaAnimation.start();
    }

    private void moveAnim(View view, float start, float end){
        ObjectAnimator moveAnimation = ObjectAnimator.ofFloat(view, "translationY", start, end);
        moveAnimation.setDuration(500);
        moveAnimation.setRepeatCount(0);
        moveAnimation.setRepeatMode(ValueAnimator.REVERSE);
        moveAnimation.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(collectedStatus != hasCollected){
            EventBus.getDefault().post(new CollectionEvent(mImageModel, hasCollected));
        }
    }

    public void backFunction(){
        if(isFirstPage){
            getActivity().getSupportFragmentManager().popBackStack();
        }else {
            isFirstPage = true;
            animJumpPage(true);
        }
    }

}
