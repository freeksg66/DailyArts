package com.github.arts_social;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ShareView extends LinearLayout {

    private int[] mPlatformIcons = {
            R.drawable.v_share_weixin,
            R.drawable.v_share_contect,
            R.drawable.v_share_qq,
            R.drawable.v_share_qq_zone,
            R.drawable.v_share_weibo
    };

    private int[] mPlatformUnInstallIcons = {
            R.drawable.v_share_weixin_uninstall,
            R.drawable.v_share_contect_uninstall,
            R.drawable.v_share_qq_uninstall,
            R.drawable.v_share_qq_zone_uninstall,
            R.drawable.v_share_weibo_uninstall
    };

    private String[] mPkgNames = {"com.tencent.mm", "com.tencent.mm",
            "com.tencent.mobileqq", "com.tencent.mobileqq", "com.sina.weibo", "com.sohu.auto.sohuauto"};

    private ShareContent mShareContent;
    private View vContent;
    private RecyclerView rvPlatformView;

    public ShareView(Context context) {
        super(context);
        init(context);
    }

    public ShareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        vContent = LayoutInflater.from(context).inflate(R.layout.view_select_share_platform, this,false);
        rvPlatformView = (RecyclerView) vContent.findViewById(R.id.rv_view_select_share_platform);
        rvPlatformView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        rvPlatformView.setAdapter(new PlatformAdapter(context));
        addView(vContent);
    }

    public void setShareContent(ShareContent mShareContent) {
        this.mShareContent = mShareContent;
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
            return new PlatformViewHolder(mInflater.inflate(R.layout.item_view_share_platform, parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            PlatformViewHolder platformViewHolder = (PlatformViewHolder) holder;
            platformViewHolder.ivPlatformIcon.setOnClickListener(new OnClickListener() {
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

                    }
                }
            });
            if (isPlatformEnabled(position)) {
                platformViewHolder.ivPlatformIcon.setImageResource(mPlatformIcons[position]);
            } else {
                platformViewHolder.ivPlatformIcon.setImageResource(mPlatformUnInstallIcons[position]);
                platformViewHolder.ivPlatformIcon.setClickable(false);
            }
        }

        private boolean isPlatformEnabled(int position) {
            return (position < 4 && PackageUtils.isAppInstalled(mContext, mPkgNames[position]))
                    || position == 4;
        }

        @Override
        public int getItemCount() {
            return mPlatformIcons.length;
        }

        class PlatformViewHolder extends RecyclerView.ViewHolder {

            ImageView ivPlatformIcon;

            public PlatformViewHolder(View itemView) {
                super(itemView);
                ivPlatformIcon = (ImageView) itemView;
            }
        }
    }
}
