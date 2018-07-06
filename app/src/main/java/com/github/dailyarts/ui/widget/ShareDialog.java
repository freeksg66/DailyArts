package com.github.dailyarts.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.dailyarts.R;
import com.github.dailyarts.utils.ImageLoadUtils;
import com.github.dailyarts.utils.ToastUtils;

/**
 * Created by legao215985 on 2018/7/5.
 */

public class ShareDialog extends DialogFragment {
    private Context mContext;
    private String mImageUrl;
    private ImageView ivShare;
    private TextView tvTitle, tvTopBtn, tvBottomBtn;

    private Bitmap mResource;

    public static ShareDialog getInstance(Context context, String imageUrl) {
        ShareDialog dialog = new ShareDialog();
        dialog.mImageUrl = imageUrl;
        dialog.mContext = context;
        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_share, container, false);
        ivShare = view.findViewById(R.id.iv_share);
        tvTitle = view.findViewById(R.id.tv_share_title);
        tvTopBtn = view.findViewById(R.id.tv_share_save);
        tvBottomBtn = view.findViewById(R.id.tv_share_cancel);

        Glide.with(mContext)
                .load(mImageUrl)
                .asBitmap()
                .placeholder(R.drawable.image_placeholder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mResource = drawShareImage(resource);
                        ivShare.setImageBitmap(mResource);
                    }
                });
        tvTitle.setText("是否保存图片到相册，\n分享给小伙伴呢？");
        tvTopBtn.setOnClickListener(v -> {
            ImageLoadUtils.saveImageToGallery(getContext(), mResource);
            ToastUtils.show(mContext, "保存成功！");
            dismiss();
        });
        tvBottomBtn.setOnClickListener(v -> dismiss());
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        window.setBackgroundDrawable( new ColorDrawable(0x80000000));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = R.style.shareDialogAnim;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    private void initData() {
        //
    }

    private Bitmap drawShareImage(Bitmap resource) {
        int width = resource.getWidth();
        int height = resource.getHeight();
        int brandHeight = 200; // 背景高度
        int logoHeight = 80; // Android logo尺寸
        int qrCodeHeight = 150; // 二维码尺寸
        int margin = 20; // 边距
        // 画背景
        Bitmap newBitmap = Bitmap.createBitmap(resource.getWidth(), resource.getHeight() + brandHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas = drawBitmap(canvas, resource, 0, 0, resource.getWidth(), resource.getHeight());
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, height + brandHeight, width, height, paint);

        // 放置Android logo
        canvas = drawBitmap(canvas, R.drawable.android_logo, margin, height + margin, logoHeight, logoHeight);


        // 写App名字
        canvas = drawTitleText(canvas, "每日名画(Android版)", "（每天为您推荐一幅世界名画）", margin + logoHeight + margin, height + margin / 2 + logoHeight / 3, ContextCompat.getColor(mContext, R.color.black), 12, ContextCompat.getColor(mContext, R.color.cG1), 8, margin / 4);
        // 防止二维码
        canvas = drawBitmap(canvas, R.drawable.qr_code, width - qrCodeHeight - margin, height + margin / 2, qrCodeHeight, qrCodeHeight);
        // 写第二种下载方式
        drawText(canvas, "扫描二维码下载", margin, height + margin + logoHeight + margin * 3 / 2, ContextCompat.getColor(mContext, R.color.cG2), 7);
        drawText(canvas, "或用手机浏览器输入这个网址 https://fir.im/ej2d", margin, height + brandHeight - 30, ContextCompat.getColor(mContext, R.color.cG2), 7);
        return newBitmap;
    }

    private Canvas drawBitmap(Canvas canvas, int drawable, int leftX, int leftY, int layoutWidth, int layoutHeight){
        Bitmap fg = BitmapFactory.decodeResource(mContext.getResources(), drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) layoutWidth) / fg.getWidth(), ((float) layoutHeight) / fg.getHeight());
        fg = Bitmap.createBitmap(fg, 0, 0, fg.getWidth(), fg.getHeight(), matrix, true);
        canvas.drawBitmap(fg, leftX, leftY, null);
        return canvas;
    }

    private Canvas drawBitmap(Canvas canvas, Bitmap fg, int leftX, int leftY, int layoutWidth, int layoutHeight){
        Matrix matrix = new Matrix();
        matrix.postScale(((float) layoutWidth) / fg.getWidth(), ((float) layoutHeight) / fg.getHeight());
        fg = Bitmap.createBitmap(fg, 0, 0, fg.getWidth(), fg.getHeight(), matrix, true);
        canvas.drawBitmap(fg, leftX, leftY, null);
        return canvas;
    }

    private Canvas drawText(Canvas canvas, String text, int leftX, int leftY, int textColor, int textSize) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize((int)(textSize * scale));

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        canvas.drawText(text, leftX, leftY + bounds.height() / 3, paint);
        return canvas;
    }

    private Canvas drawTitleText(Canvas canvas, String text, String subText, int leftX, int leftY, int textColor, int textSize, int subTextColor, int subTextSize, int margin) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        // 写主标题
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize((int)(textSize * scale));

        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        canvas.drawText(text, leftX, leftY + bounds.height() / 3, paint);

        // 写副标题
        paint.setColor(subTextColor);
        paint.setTextSize((int) (subTextSize * scale));
        Rect subBounds = new Rect();
        paint.getTextBounds(subText, 0, subText.length(), subBounds);
        canvas.drawText(subText, leftX - margin, leftY + bounds.height() + margin + subBounds.height() / 2, paint);
        return canvas;
    }

    public interface ButtonClickListener {
        void topButtonClick();

        void bottomButtonClick();
    }

}
