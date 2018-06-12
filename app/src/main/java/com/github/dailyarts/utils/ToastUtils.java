package com.github.dailyarts.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dailyarts.R;

/**
 * Created by legao005426 on 2018/6/12.
 */

public class ToastUtils {
    private static Toast mToast;

    private static Toast mInnerToast;

    public static void show(Context context, String text) {
        getToast(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String text) {
        getToast(context, text, Toast.LENGTH_LONG).show();
    }

    private static Toast getToast(Context context, String text, int duration) {
        if (null == mInnerToast) {
            mInnerToast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
        }
        mInnerToast.setText(text);
        mInnerToast.setDuration(duration);
        return mInnerToast;
    }

    public static void ShowCenter(Context context, String content) {
        if (context == null) {
            return;
        }
        if (context instanceof AppCompatActivity) {
            //将context替换为applicationContext 防止内存泄露
            context = context.getApplicationContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast_daily_arts, null);
        TextView txtView = (TextView) view.findViewById(R.id.message);
        txtView.setText(content);

        if (mToast == null) {
            mToast = Toast.makeText(context,
                    content, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setView(view);
        } else {
            mToast.setView(view);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
