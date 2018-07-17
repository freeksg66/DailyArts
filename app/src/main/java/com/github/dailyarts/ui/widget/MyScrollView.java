package com.github.dailyarts.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by legao215985 on 2018/7/17.
 */

public class MyScrollView extends ScrollView {
    private float startY, endY;
    private static final float MIN_DISTANCE_Y = 60; // 最小滑动距离60px
    private boolean upLoadEnable = false;
    private UpLoadListener mUpLoadListener;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(getScrollY() == 0) {
                    upLoadEnable = true;
                }
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(upLoadEnable && getScrollY() != 0) {
                    upLoadEnable = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                endY = event.getY();
                if(endY - startY > MIN_DISTANCE_Y && upLoadEnable && mUpLoadListener != null) {
                    mUpLoadListener.upLoad();
                    return true;
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
