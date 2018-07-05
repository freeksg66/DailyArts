package com.github.dailyarts.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dailyarts.R;
import com.github.dailyarts.utils.StringUtils;

/**
 * Created by legao005426 on 2018/6/12.
 */

public class AppActionBar extends FrameLayout {
    private View mTitleLine;
    private Context mContext;
    private ImageView ivLeftBtn;
    private ImageView ivUserSetting;
    private TextView tvTitle;
    private TextView tvDownload;
    private ImageView ivFindImages;
    private TextView tvSubmit;
    private TextView mRightTv;

    private boolean mInterruptBackEvent;

    private ActionBarListener mListener;

    public AppActionBar(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AppActionBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AppActionBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater.from(getContext()).inflate(R.layout.actionbar_pinned, this, true);
        mTitleLine = findViewById(R.id.title_line);
        ivLeftBtn = (ImageView) findViewById(R.id.iv_left_btn);
        ivUserSetting = (ImageView) findViewById(R.id.iv_user_setting);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDownload = (TextView) findViewById(R.id.tv_download_image);
        ivFindImages = (ImageView) findViewById(R.id.iv_find_arts);
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        mRightTv = (TextView) findViewById(R.id.tv_right_btn);

        mInterruptBackEvent = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                (int) getResources().getDimension(R.dimen.action_bar_height), MeasureSpec.EXACTLY));
    }

    /**
     * 内容设置结束.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mContext instanceof Activity) {
            ivLeftBtn.setOnClickListener(v -> ((Activity) mContext).finish());
        }
    }

    public interface ActionBarListener {
        /**
         * 布局点击回调接口
         *
         * @param event 使用ActionBarEvent枚举分类，客户端通过event进行相应回调事件调用
         */
        void onEvent(final ActionBarEvent event);
    }

    public enum ActionBarEvent {
        LEFT_TEXT_CLICK,
        LEFT_IMG_CLICK,
        RIGHT_TEXT_CLICK,
        RIGHT_IMG_CLICK,
        RIGHT_IMG_CLICK1,
        CLOSE_CLICK
    }

    public void setListener(final ActionBarListener listener) {
        if (listener == null) {
            return;
        }
        mListener = listener;

        mRightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEvent(ActionBarEvent.RIGHT_TEXT_CLICK);
            }
        });
    }

    public void hideLeftBtn() {
        if (ivLeftBtn != null) {
            ivLeftBtn.setVisibility(GONE);
        }
    }

    public void showUserSetting() {
        ivLeftBtn.setVisibility(GONE);
        if (ivUserSetting != null) {
            ivUserSetting.setVisibility(VISIBLE);
        }
    }

    public void showFindArts() {
        if (ivFindImages != null) {
            ivFindImages.setVisibility(VISIBLE);
        }
    }

    public void showDownload() {
        if (tvDownload != null) {
            tvDownload.setVisibility(VISIBLE);
        }
    }

    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void showSubmit() {
        if (tvSubmit != null) {
            tvSubmit.setVisibility(VISIBLE);
        }
    }

    public void setLeftBackBtnClickListener(OnClickListener listener) {
        ivLeftBtn.setOnClickListener(listener);
    }

    public void setRightTx(CharSequence text) {
        if (!StringUtils.isEmpty(text) && mRightTv != null) {
            mRightTv.setVisibility(View.VISIBLE);
            mRightTv.setText(text);
        }
    }
}
