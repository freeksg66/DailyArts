package com.github.dailyarts.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by legao215985 on 2018/7/17.
 */

public class MySubsamplingScaleImageView extends SubsamplingScaleImageView {
    private float startX, startY, endX, endY;
    private static final float MIN_DISTANCE_Y = 60; // 最小滑动距离60px
    private static final long MIN_MOVE_TIME = 60; // 最短滑动时间60ms
    private UpLoadListener mUpLoadListener;
    private boolean upLoadEnable = false;
    private long currentMS;

    public MySubsamplingScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public MySubsamplingScaleImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(upLoadEnable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    currentMS = System.currentTimeMillis();//long currentMS     获取系统时间
                    break;
                case MotionEvent.ACTION_UP:
                    endX = event.getX();
                    endY = event.getY();
                    long moveTime = System.currentTimeMillis() - currentMS;//移动时间
                    if (moveTime > MIN_MOVE_TIME && startY - endY > MIN_DISTANCE_Y && mUpLoadListener != null) {
                        mUpLoadListener.upLoad();
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public interface UpLoadListener {
        void upLoad();
    }

    public void setUpLoadListener(UpLoadListener listener) {
        mUpLoadListener = listener;
    }

    public void setUpLoadEnable(boolean enable) {
        upLoadEnable = enable;
    }
}
