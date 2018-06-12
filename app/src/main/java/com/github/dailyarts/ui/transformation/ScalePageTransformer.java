package com.github.dailyarts.ui.transformation;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by legao005426 on 2018/5/18.
 */

public class ScalePageTransformer implements ViewPager.PageTransformer{
    private static final String TAG = "ScalePageTransformer";
    public static final float MAX_SCALE = 1.0f ;
    public static final float MIN_SCALE = 0.9f ;

    private float excursion = 0;

    public ScalePageTransformer(float excursion){
        this.excursion = excursion;
    }

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        position = position + excursion;
        //Log.e(TAG, "ViewId="+view.toString()+", position="+String.valueOf(position)+", offset="+String.valueOf(excursion));
        if(position < -1){ // [-Infinity, -1)
            position = -1;
        }else if(position > 1 ){  // (+1, Infinity]
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;
        float scaleValue = MIN_SCALE + tempScale * slope;
        view.setScaleX(scaleValue);
        view.setScaleY(scaleValue);
    }
}
