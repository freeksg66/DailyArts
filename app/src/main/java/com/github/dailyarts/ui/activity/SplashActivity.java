package com.github.dailyarts.ui.activity;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;

/**
 * Created by legao215985 on 2018/7/9.
 */

public class SplashActivity extends BaseActivity {
    ImageView ivSplash;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onInitView() {
        ivSplash = findViewById(R.id.iv_splash);
        iniImage();
    }

    private void iniImage() {
        ivSplash.setImageResource(R.drawable.splash);

        ScaleAnimation scaleAnim = new ScaleAnimation(
                1.0f,
                1.2f,
                1.0f,
                1.2f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );

        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(1000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //在这里做一些初始化的操作
                //跳转到指定的Activity
                RouterManager.getInstance().startActivity(RouterConstant.MainActivityConst.PATH);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ivSplash.startAnimation(scaleAnim);
    }
}
