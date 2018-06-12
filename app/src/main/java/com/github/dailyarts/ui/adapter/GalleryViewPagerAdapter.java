package com.github.dailyarts.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.dailyarts.R;
import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.entity.GalleryModel;
import com.github.dailyarts.entity.ImageMessageModel;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.net.DailyArtsAPI;
import com.github.dailyarts.net.NetError;
import com.github.dailyarts.net.NetSubscriber;
import com.github.dailyarts.presenter.GalleryImagePresenter;
import com.github.dailyarts.repository.GalleryImagesRepository;
import com.github.dailyarts.utils.ImageLoadUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by legao005426 on 2018/5/8.
 */

public class GalleryViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "GalleryViewPagerAdapter";

    private Context mContext;
    private List<DateModel> mList;
    private LayoutInflater mInflater;

    public GalleryViewPagerAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateModel dateModel = new DateModel(calendar.get(Calendar.YEAR) - 1, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 0);
        mList = new ArrayList<>();
        mList.add(dateModel.getYesterday());
        mList.add(dateModel);
        mList.add(dateModel.getTomorrow());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        Log.d(TAG, "position="+String.valueOf(position));
        View rootView = mInflater.inflate(R.layout.gallery_item, null);
        ImageView ivGalleryImage = rootView.findViewById(R.id.iv_gallery_item_image);
        TextView tvMonth = rootView.findViewById(R.id.tv_gallery_item_month);
        TextView tvDay = rootView.findViewById(R.id.tv_gallery_item_day);
        DateModel dateModel = mList.get(position);
//        DailyArtsAPI.getInstance().getImageMessage(dateModel.toInt())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new NetSubscriber<ImageMessageModel>() {
//                    @Override
//                    public void onSuccess(ImageMessageModel imageMessageModel) {
//                        if(imageMessageModel != null && imageMessageModel.getData()!=null){
//                            ImageLoadUtils.load(mContext, R.drawable.image_placeholder, imageMessageModel.getData().getPic(), ivGalleryImage);
//                        }
//                        else {
//                            // 加载失败
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(NetError error) {
//                        // 加载失败
//                    }
//                });
        ivGalleryImage.setImageResource(R.drawable.image_placeholder);
        tvMonth.setText(String.valueOf(dateModel.month) + String.valueOf("月"));
        tvDay.setText(String.valueOf(dateModel.day) + String.valueOf("日"));
        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }
}
