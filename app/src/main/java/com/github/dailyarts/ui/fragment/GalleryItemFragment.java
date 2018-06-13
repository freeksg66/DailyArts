package com.github.dailyarts.ui.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.dailyarts.R;
import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.presenter.GalleryImagePresenter;
import com.github.dailyarts.contract.GalleryImagesContract;
import com.github.dailyarts.repository.GalleryImagesRepository;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.utils.SharedPreferencesUtils;
import com.github.dailyarts.utils.ToastUtils;

/**
 * Created by legao005426 on 2018/5/7.
 */

public class GalleryItemFragment extends BaseFragment implements GalleryImagesContract.IView{
    private static String TAG = "GalleryItemFragment";

    private LinearLayout llItemTime;
    private ImageView ivGalleryImage, ivCollection;
    private TextView tvMonth, tvDay;

    private DateModel mDateModel;

    private boolean mLoadSuccess = false;
    private boolean hasCollected = true;

    private ImageModel mImageModel;

    private GalleryImagesContract.IPresenter mPresenter;

    @Override
    protected int getLayoutResource() {
        return R.layout.gallery_item;
    }

    @Override
    protected void onInitView() {
        mPresenter = new GalleryImagePresenter(this, new GalleryImagesRepository(getHoldingActivity()));

        llItemTime = rootView.findViewById(R.id.ll_item_time);
        ivGalleryImage = rootView.findViewById(R.id.iv_gallery_item_image);
        tvMonth = rootView.findViewById(R.id.tv_gallery_item_month);
        tvDay = rootView.findViewById(R.id.tv_gallery_item_day);
        ivCollection = rootView.findViewById(R.id.iv_my_collection);
        ivCollection.setOnClickListener(v -> collectImage());

        //获得ViewTreeObserver
        ViewTreeObserver observer=ivGalleryImage.getViewTreeObserver();
        //注册观察者，监听变化
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(observer.isAlive()){
                    observer.removeOnPreDrawListener(this);
                }
                //获得宽高
                int viewWidth = ivGalleryImage.getMeasuredWidth();
                int viewHeight = viewWidth * 16 / 9;
                ViewGroup.LayoutParams params = ivGalleryImage.getLayoutParams();
                params.width = viewWidth;
                params.height = viewHeight;
                ivGalleryImage.setLayoutParams(params);
                ivGalleryImage.requestLayout();
                return true;
            }
        });

        if(null != mDateModel) {
            if(mDateModel.offset == 1){
                ivGalleryImage.setImageResource(R.drawable.tomorrow);
            }else {
                mPresenter.getImage(mDateModel.toInt());
            }
            ivGalleryImage.setOnClickListener(v -> toImageDetail());
            tvMonth.setText(String.valueOf(mDateModel.month) + "月");
            tvDay.setText(String.valueOf(mDateModel.day) + "日");
        }
        else if(mImageModel != null){
            llItemTime.setVisibility(View.GONE);
            ivCollection.setVisibility(View.VISIBLE);
            mLoadSuccess = false;
            Glide.with(getContext())
                    .load(mImageModel.getBigImg())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mLoadSuccess = true;
                            ivGalleryImage.setImageBitmap(resource);
                        }
                    });
            ivGalleryImage.setOnClickListener(v -> toImageDetail());
        }
        else {
            ivGalleryImage.setImageResource(R.drawable.image_placeholder);
            tvMonth.setText("未知月");
            tvDay.setText("未知日");
        }
    }

    public void setData(DateModel dateModel){
        mDateModel = dateModel;
    }

    public void setData(ImageModel imageModel){
        mImageModel = imageModel;
    }

    private void toImageDetail(){
        if(mLoadSuccess){
            // 进入图片详情页
            RouterManager
                    .getInstance()
                    .startActivity(
                            RouterConstant.ImageDetailsActivityConst.PATH,
                            RouterConstant.ImageDetailsActivityConst.IMAGE_MODEL,
                            mImageModel);
        }else {
            ToastUtils.show(getContext(), "努力加载中...");
        }
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

    @Override
    public void loadPicture(ImageModel imageModel) {
        mImageModel = imageModel;
        mLoadSuccess = false;
        Glide.with(getContext())
                .load(imageModel.getBigImg())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mLoadSuccess = true;
                        ivGalleryImage.setImageBitmap(resource);
                    }
                });
    }

    @Override
    public void loadPictureFail(String errorMessage) {
        ToastUtils.show(getContext(), errorMessage);
    }
}
