package com.github.arts_social;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ShareDialog extends Dialog {

    private int[] mPlatformIcons = {R.drawable.v_share_weixin,
            R.drawable.v_share_contect,
            R.drawable.v_share_qq,
            R.drawable.v_share_qq_zone,
            R.drawable.v_share_weibo,
            R.drawable.v_share_copy_link};

    private int[] mPlatformUnInstallIcons = {R.drawable.v_share_weixin_uninstall,
            R.drawable.v_share_contect_uninstall,
            R.drawable.v_share_qq_uninstall,
            R.drawable.v_share_qq_zone_uninstall,
            R.drawable.v_share_weibo_uninstall,
            R.drawable.v_share_copy_link};

    private String[] mPlatformNames = {"微信", "朋友圈", "QQ", "QQ空间", "微博", "复制链接"};

    private String[] mPkgNames = {"com.tencent.mm", "com.tencent.mm",
            "com.tencent.mobileqq", "com.tencent.mobileqq", "com.sina.weibo", "com.sohu.auto.sohuauto"};

    private static ShareDialog INSTANCE;
    private View contentView;
    private RecyclerView rvPlatformView;

    public ShareDialog(Activity context) {
        super(context, R.style.DialogWidthMatchParent);
        initView(context);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.DialogStyleAnimation);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
    }

    private void initView(Context context) {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_select_share_platform, null);
        rvPlatformView = (RecyclerView) contentView.findViewById(R.id.rv_share_platform);
        rvPlatformView.setLayoutManager(new GridLayoutManager(context, 3));
        rvPlatformView.setAdapter(new PlatformAdapter(context));
        setContentView(contentView);

    }

    private ShareContent mShareContent;

    public static ShareDialog getInstance(Activity context) {
        return new ShareDialog(context);
    }

    public ShareDialog withShareContent(ShareContent mShareContent) {
        this.mShareContent = mShareContent;
        return this;
    }

    class PlatformAdapter extends RecyclerView.Adapter {

        private Context mContext;
        private LayoutInflater mInflater;

        public PlatformAdapter(Context mContext) {
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlatformViewHolder(mInflater.inflate(R.layout.item_share_platform, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            PlatformViewHolder platformViewHolder = (PlatformViewHolder) holder;
            platformViewHolder.tvPlatformName.setText(mPlatformNames[position]);
            platformViewHolder.rlPlatform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            ThirdParty.with((Activity) mContext, SocialType.WECHAT).share(mShareContent);
                            break;

                        case 1:
                            ThirdParty.with((Activity) mContext, SocialType.MOMENT).share(mShareContent);
                            break;

                        case 2:
                            ThirdParty.with((Activity) mContext, SocialType.QQ).share(mShareContent);
                            break;

                        case 3:
                            ThirdParty.with((Activity) mContext, SocialType.QZONE).share(mShareContent);
                            break;

                        case 4:
                            ThirdParty.with((Activity) mContext, SocialType.SINA).share(mShareContent);
                            break;

                        case 5:
                            ThirdParty.with((Activity) mContext, SocialType.CLIPBOARD).share(mShareContent);
                            break;
                    }
                    dismiss();
                }
            });
            if ((position < 4 && PackageUtils.isAppInstalled(mContext, mPkgNames[position]))
                    || position == 5 || position == 4) {
                platformViewHolder.ivPlatformIcon.setImageResource(mPlatformIcons[position]);
            } else {
                platformViewHolder.ivPlatformIcon.setImageResource(mPlatformUnInstallIcons[position]);
                platformViewHolder.rlPlatform.setClickable(false);
            }
        }

        @Override
        public int getItemCount() {
            return mPlatformIcons.length;
        }

        class PlatformViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout rlPlatform;
            ImageView ivPlatformIcon;
            TextView tvPlatformName;

            public PlatformViewHolder(View itemView) {
                super(itemView);
                rlPlatform = (RelativeLayout) itemView.findViewById(R.id.rl_platform);
                ivPlatformIcon = (ImageView) itemView.findViewById(R.id.iv_share_platform_icon);
                tvPlatformName = (TextView) itemView.findViewById(R.id.tv_share_platform_name);
            }
        }
    }
}
