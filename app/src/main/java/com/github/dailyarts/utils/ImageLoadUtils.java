package com.github.dailyarts.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.dailyarts.config.Constant;
import com.github.dailyarts.ui.transformation.GlideCircleTransform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by legao005426 on 2018/5/4.
 * 图片加载工具类
 */

public class ImageLoadUtils {
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void loadCircle(Context context, @DrawableRes int id, String url, ImageView img) {
        if (img == null)
            return;
        if (StringUtils.isEmpty(url))
            return;
        if (!url.startsWith(Constant.HTTP) && !url.startsWith(Constant.HTTPS)) {
            url = Constant.HTTPS + ":" + url;
        }

        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new GlideCircleTransform(context))
                .into(img);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void load(Context context, @DrawableRes int placeHolder, String url, ImageView img) {
        if (img == null)
            return;
        if (StringUtils.isEmpty(url))
            return;
        if (!url.startsWith(Constant.HTTP) && !url.startsWith(Constant.HTTPS)) {
            url = Constant.HTTPS + ":" + url;
        }

        Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(img);
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        //如果没有存储环境，取消下载
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.ShowCenter(context, "图片保存失败");
            return;
        }
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "dailyArtsPic");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (context == null)
            return;

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }
}
