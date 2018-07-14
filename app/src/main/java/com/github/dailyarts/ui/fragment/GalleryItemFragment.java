package com.github.dailyarts.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.github.dailyarts.R;
import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.event.CollectionEvent;
import com.github.dailyarts.event.NetConnectionChangeEvent;
import com.github.dailyarts.presenter.GalleryImagePresenter;
import com.github.dailyarts.contract.GalleryImagesContract;
import com.github.dailyarts.repository.GalleryImagesRepository;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.transformation.DetailTransition;
import com.github.dailyarts.utils.SharedPreferencesUtils;
import com.github.dailyarts.utils.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by legao005426 on 2018/5/7.
 */

public class GalleryItemFragment extends BaseFragment implements GalleryImagesContract.IView {
    private static String TAG = "GalleryItemFragment";

    private LinearLayout llItemTime;
    private ImageView ivGalleryImage, ivCollection;
    private TextView tvMonth;
    private TextView tvDay;

    private DateModel mDateModel;

    private int mLoadState; // 图片加载状态
    private boolean mLoadSuccess = false; // 是否加载成功
    private boolean hasCollected = true; // 是否已经收藏
    private boolean isTomorrow = false; // 是否是明天

    private ImageModel mImageModel;
    private ActivityOptionsCompat mOptionsCompat;
    private OnImageItemClick mImageItemClick;

    private GalleryImagesContract.IPresenter mPresenter;
    private int mOffset;

    private static final int DEFAULT = 1;
    private static final int LOADING = 2;
    private static final int LOAD_COMPLETE = 3;

    @Override
    protected int getLayoutResource() {
        return R.layout.gallery_item;
    }

    @Override
    protected void onInitView() {
        mPresenter = new GalleryImagePresenter(this, new GalleryImagesRepository(getHoldingActivity()));
        mLoadState = DEFAULT;

        llItemTime = rootView.findViewById(R.id.ll_item_time);
        ivGalleryImage = rootView.findViewById(R.id.iv_gallery_item_image);
        tvMonth = rootView.findViewById(R.id.tv_gallery_item_month);
        tvDay = rootView.findViewById(R.id.tv_gallery_item_day);
        ivCollection = rootView.findViewById(R.id.iv_my_collection);
        ivCollection.setOnClickListener(v -> collectImage());

        /*
        * 字体
        * 楷体 simkai.ttf
        * 华文行楷 stxingka.ttf
        * 华文隶书 stiliti.ttf
        * 华文楷体 stkaiti.ttf
        * 华文琥珀 sthupo.ttf
        * */
        tvMonth.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "font/stiliti.ttf"));
        mOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ivGalleryImage, "sharedView");
        loadData();
    }

    public void loadData(){
        if(rootView == null) return;
        if(mPresenter == null) return;
        if (null != mDateModel) {
            if (mOffset == 1) {
                ivGalleryImage.setImageResource(R.drawable.tomorrow);
            } else {
                mPresenter.getImage(mDateModel.toInt());
            }
            ivGalleryImage.setOnClickListener(v -> toImageDetail());
            tvMonth.setText(convert2Hanzi(mDateModel.month) + "月");
            tvDay.setText(String.valueOf(mDateModel.day));
        } else if (mImageModel != null) {
            llItemTime.setVisibility(View.GONE);
            ivCollection.setVisibility(View.VISIBLE);
            ivGalleryImage.setOnClickListener(v -> toImageDetail());
            loadingImages();
        } else {
            ivGalleryImage.setImageResource(R.drawable.image_placeholder);
            tvMonth.setText("未知月");
            tvDay.setText("未知日");
        }
    }

    public void setData(DateModel dateModel, int offset) {
        mDateModel = dateModel;
        mOffset = offset;
        isTomorrow = offset == 1;
    }

    public void setData(ImageModel imageModel) {
        mImageModel = imageModel;
    }

    public void updateData(DateModel dateModel, int offset) {
        mDateModel = dateModel;
        mOffset = offset;
        isTomorrow = offset == 1;
        loadData();
    }

    public void updateData(ImageModel imageModel) {
        mImageModel = imageModel;
    }

    public int getOffset(){
        return mOffset;
    }

    private void toImageDetail() {
        if (isTomorrow) {
            ToastUtils.show(getContext(), "敬请期待！");
        } else if (mLoadSuccess) {
            // 进入图片详情页
            if(mImageItemClick != null) {
                mImageItemClick.onClick(mImageModel);
            }
        } else if(mLoadState == LOADING){
            ToastUtils.show(getContext(), "努力加载中...");
        } else if(mLoadState == DEFAULT) {
            if(mImageModel != null) {
                loadingImages();
            } else {
                mPresenter.getImage(mDateModel.toInt());
            }
        }
    }

    private void collectImage() {
        if (mImageModel == null) return;
        if (hasCollected) {
            if (SharedPreferencesUtils.deleteCollectImage(getContext(), mImageModel)) {
                ToastUtils.ShowCenter(getContext(), "取消收藏成功√");
                ivCollection.setImageResource(R.drawable.collection_false);
                hasCollected = false;
            } else {
                ToastUtils.ShowCenter(getContext(), "取消收藏失败×");
                ivCollection.setImageResource(R.drawable.collection_true);
            }
        } else {
            if (SharedPreferencesUtils.saveCollectImage(getContext(), mImageModel)) {
                ToastUtils.ShowCenter(getContext(), "收藏成功√");
                ivCollection.setImageResource(R.drawable.collection_true);
                hasCollected = true;
            } else {
                ToastUtils.ShowCenter(getContext(), "收藏失败×");
                ivCollection.setImageResource(R.drawable.collection_false);
            }
        }
    }

    @Override
    public void loadPicture(ImageModel imageModel) {
        if(getContext() == null) return;
        mImageModel = imageModel;
        loadingImages();
    }

    private String convert2Hanzi(int num) {
        if(num <= 0 || num > 12) return "未知";
        switch (num) {
            case 1: return "一";
            case 2: return "二";
            case 3: return "三";
            case 4: return "四";
            case 5: return "五";
            case 6: return "六";
            case 7: return "七";
            case 8: return "八";
            case 9: return "九";
            case 10: return "十";
            case 11: return "十一";
            case 12: return "十二";
            default:
        }
        return "未知";
    }

    private void loadingImages() {
        mLoadSuccess = false;
        mLoadState = LOADING;
        Glide.with(getContext())
                .load(mImageModel.getBigImg())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        mLoadSuccess = false;
                        mLoadState = DEFAULT;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mLoadSuccess = true;
                        mLoadState = LOAD_COMPLETE;
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(ivGalleryImage);
    }

    @Override
    public void loadPictureFail(String errorMessage) {
        if(getContext() == null) return;
        ToastUtils.show(getContext(), errorMessage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionChange(CollectionEvent event) {
        if (event.imageModel.getId().equals(mImageModel.getId())) {
            if (event.status) {
                ivCollection.setImageResource(R.drawable.collection_true);
            } else {
                ivCollection.setImageResource(R.drawable.collection_false);
            }
        }
    }

    /**
     * 网络连接状态变化时调用
     *
     * @param event 包含网络连接状态和当前网络类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetConnectionChangeEvent event) {
        if(isTomorrow) return;
        if(mLoadState == DEFAULT) {
            if(mImageModel != null) {
                loadingImages();
            } else {
                mPresenter.getImage(mDateModel.toInt());
            }
        }
    }

    public void setOnImageItemClick(OnImageItemClick imageItemClick) {
        mImageItemClick = imageItemClick;
    }

    public interface OnImageItemClick {
        void onClick(ImageModel imageModel);
    }
}
