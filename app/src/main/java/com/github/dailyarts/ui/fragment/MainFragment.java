package com.github.dailyarts.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.dailyarts.R;
import com.github.dailyarts.ui.activity.ImageDetailsActivity;
import com.github.dailyarts.ui.transformation.GlideCircleTransform;
import com.github.dailyarts.contract.FindArtsContract;
import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.event.UpdateInfoEvent;
import com.github.dailyarts.presenter.FindArtsPresenter;
import com.github.dailyarts.repository.FindArtsRepository;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.adapter.FindArtsAdapter;
import com.github.dailyarts.ui.transformation.ScalePageTransformer;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.ui.widget.TipsDialog;
import com.github.dailyarts.ui.widget.imagespickers.ImageConfig;
import com.github.dailyarts.ui.widget.imagespickers.ImageSelector;
import com.github.dailyarts.ui.widget.imagespickers.ImageSelectorActivity;
import com.github.dailyarts.utils.CacheUtils;
import com.github.dailyarts.utils.DeviceInfo;
import com.github.dailyarts.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class MainFragment extends BaseFragment implements FindArtsContract.IView, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "MainFragment";

    private DrawerLayout mDrawer;

    // contentView
    private AppActionBar appActionBar;
    private ImageView ivHomeUserSetting, ivHomeFindArts;
    private ViewPager mGalleryViewPager;
    public ImageDetailsFragment imageDetailsFragment;

    private IdiotGalleryAdapter mAdapter;
    private List<Fragment> mFragments;
    private int mFragmentsLength = 100; // 长度必须大于3

    // leftView
    private TextView tvDate, tvMyGallery, tvOfflineCache, tvPaintingDemand, tvAbout;
    private ImageView ivBack, ivUserProfile;

    // rightRightView
    private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;
    private RadioButton rb11;
    private RadioButton rb12;
    private RadioButton rb13;
    private RadioButton rb14;
    private RadioButton rb21;
    private RadioButton rb22;
    private RadioButton rb23;
    private RadioButton rb31;
    private RadioButton rb32;
    private RadioButton rb33;
    private ImageView ivClear;
    private EditText etName;
    private RelativeLayout rlFindNothing;
    private RecyclerView rvFindArts;
    private FindArtsAdapter mFindArtsAdapter;

    private static final int LEFT_BTN = -1;
    private static final int RIGHT_BTN = 1;
    private static final int MID_BTN = 0;

    private int swipeStatus = MID_BTN;
    private boolean isFirstOpen = true;

    private FindArtsContract.IPresenter mPresenter;

    public static final int REQUEST_CODE = 1001;
    private ArrayList<String> mDatas;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    protected void onInitView() {
        mDatas = new ArrayList<>();
        mPresenter = new FindArtsPresenter(new FindArtsRepository(getHoldingActivity()), this);

        mDrawer = rootView.findViewById(R.id.dl_main_drawer);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initContentLayout(R.layout.fragment_home);
        initLeftLayout(R.layout.fragment_user_setting);
        initRightLayout(R.layout.fragment_find_arts);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.i(TAG, "onDrawerSlide slideOffset=" + String.valueOf(slideOffset));
                View content = mDrawer.getChildAt(0);
                View menu = drawerView;
                float slideLength = 0;
                switch (swipeStatus) {
                    case LEFT_BTN:
                        slideLength = menu.getWidth() * slideOffset;
                        break;
                    case RIGHT_BTN:
                        slideLength = menu.getWidth() * slideOffset * -1;
                        break;
                    default:
                        slideLength = 0;
                }
                content.setTranslationX(slideLength);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.i(TAG, "onDrawerOpened");
                //菜单打开后，打开手势滑动操作，使可以手势滑回菜单
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.i(TAG, "onDrawerClosed");
                //菜单关闭后，再次关闭手势滑动操作，使不能手势滑出
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                swipeStatus = MID_BTN;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.i(TAG, "onDrawerStateChanged");
            }
        });
    }

    private FrameLayout mContenttContainer;
    private RelativeLayout mLeftContainer;
    private RelativeLayout mRightContainer;

    private void initLeftLayout(@LayoutRes int layoutResID) {
        mLeftContainer = rootView.findViewById(R.id.rl_left_drawer_layout);
        View leftView = getLayoutInflater().inflate(layoutResID, null);
        leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvDate = leftView.findViewById(R.id.tv_user_setting_date);
        initDate();
        tvMyGallery = leftView.findViewById(R.id.tv_my_gallery);
        tvOfflineCache = leftView.findViewById(R.id.tv_offline_cache);
        tvPaintingDemand = leftView.findViewById(R.id.tv_painting_demand);
        tvAbout = leftView.findViewById(R.id.tv_about);
        ivUserProfile = leftView.findViewById(R.id.iv_profile);

        tvMyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().startActivity(RouterConstant.MyGalleryActivityConst.PATH);
            }
        });
        tvOfflineCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String content = "目前缓存" + CacheUtils.getTotalCacheSize(getContext()) + "，\n您确定要清除缓存吗？";
                    TipsDialog.ButtonClickListener listener = new TipsDialog.ButtonClickListener() {
                        @Override
                        public void leftButtonClick() {
                        }

                        @Override
                        public void rightButtonClick() {
                            new Thread(() -> {
                                CacheUtils.clearAllCache(getContext());
                            }).start();
                        }
                    };
                    TipsDialog dialog = TipsDialog.getInstance("提示", content, "取消", "确定", listener);
                    dialog.show(getChildFragmentManager(), dialog.getClass().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tvPaintingDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().startActivity(RouterConstant.PaintingDemandActivityConst.PATH);
            }
        });
        tvAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterManager.getInstance().startActivity(RouterConstant.AboutActivityConst.PATH);
            }
        });
        ivUserProfile.setOnClickListener(v ->getUserProfile());
        String profilePath = SharedPreferencesUtils.getUserProfile(getContext());
        if(profilePath != null) {
            Glide.with(this)
                    .load(profilePath)
                    .placeholder(R.drawable.v_account_head_picture)
                    .bitmapTransform(new GlideCircleTransform(getContext()))
                    .into(ivUserProfile);
        }

        mLeftContainer.addView(leftView);
    }

    private void initRightLayout(@LayoutRes int layoutResID) {
        mRightContainer = rootView.findViewById(R.id.rl_right_drawer_layout);
        View rightView = getLayoutInflater().inflate(layoutResID, null);
        rightView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rg1 = rightView.findViewById(R.id.rg_find_arts_r1);
        rg2 = rightView.findViewById(R.id.rg_find_arts_r2);
        rg3 = rightView.findViewById(R.id.rg_find_arts_r3);
        rb11 = rightView.findViewById(R.id.rb_find_arts_r1_c1);
        rb12 = rightView.findViewById(R.id.rb_find_arts_r1_c2);
        rb13 = rightView.findViewById(R.id.rb_find_arts_r1_c3);
        rb14 = rightView.findViewById(R.id.rb_find_arts_r1_c4);
        rb21 = rightView.findViewById(R.id.rb_find_arts_r2_c1);
        rb22 = rightView.findViewById(R.id.rb_find_arts_r2_c2);
        rb23 = rightView.findViewById(R.id.rb_find_arts_r2_c3);
        rb31 = rightView.findViewById(R.id.rb_find_arts_r3_c1);
        rb32 = rightView.findViewById(R.id.rb_find_arts_r3_c2);
        rb33 = rightView.findViewById(R.id.rb_find_arts_r3_c3);
        rb11.setText("梵高");
        rb12.setText("毕加索");
        rb13.setText("莫奈");
        rb14.setText("杜尚");
        rb21.setText("达芬奇");
        rb22.setText("米开朗基罗");
        rb23.setText("陈丹青");
        rb31.setText("张大千");
        rb32.setText("齐白石");
        rb33.setText("徐悲鸿");
        rb11.setOnCheckedChangeListener(this);
        rb12.setOnCheckedChangeListener(this);
        rb13.setOnCheckedChangeListener(this);
        rb14.setOnCheckedChangeListener(this);
        rb21.setOnCheckedChangeListener(this);
        rb22.setOnCheckedChangeListener(this);
        rb23.setOnCheckedChangeListener(this);
        rb31.setOnCheckedChangeListener(this);
        rb32.setOnCheckedChangeListener(this);
        rb33.setOnCheckedChangeListener(this);
        etName = rightView.findViewById(R.id.et_find_name);
        ivClear = rightView.findViewById(R.id.iv_find_arts_clear);
        rlFindNothing = rightView.findViewById(R.id.rl_find_nothing);
        rvFindArts = rightView.findViewById(R.id.rv_find_arts);

        rvFindArts.setLayoutManager(new LinearLayoutManager(getContext()));
        mFindArtsAdapter = new FindArtsAdapter(getContext(), new ArrayList<>());
        mFindArtsAdapter.setOnItemClickListener((model, shareView) -> {
            if (model == null || model.getBigImg() == null || model.getBigImg().equals("")) return;
            // 进入图片详情页
            toImageDetailFragment(model, shareView);
        });
        rvFindArts.setAdapter(mFindArtsAdapter);
        etName.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(getContext());
                String key = etName.getText().toString();
                if (key == null || key.equals("")) return false;
                mPresenter.searchImages(key);
                return true;
            }
            return false;
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.length() == 0) {
                    ivClear.setVisibility(View.GONE);
                    clearFindArtsItem();
                }else {
                    ivClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivClear.setOnClickListener(v -> etName.setText(""));
        mRightContainer.addView(rightView);
    }

    private void initContentLayout(@LayoutRes int layoutResID) {
        mFragments = new ArrayList<>();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1 - mFragmentsLength);
        GalleryItemFragment item;
        int offset = 1 - mFragmentsLength; // 与今天日期的偏差，早于今天日期的offset<0，否则offset>=0
        int yearOffset = getYearOffset(c.get(Calendar.YEAR));
        for (int i = 0; i < mFragmentsLength; i++) {
            item = new GalleryItemFragment();
            item.setOnImageItemClick((imageModel, shareView) -> toImageDetailFragment(imageModel, shareView));
            c.add(Calendar.DAY_OF_MONTH, 1);
            offset += 1;
            item.setData(new DateModel(c.get(Calendar.YEAR) - yearOffset, c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)), offset);
            mFragments.add(item);
        }

        mContenttContainer = rootView.findViewById(R.id.fl_content_drawer_layout);
        View contentView = getLayoutInflater().inflate(layoutResID, null);
        mContenttContainer.addView(contentView);
        appActionBar = contentView.findViewById(R.id.mine_toolbar);
        ivHomeUserSetting = appActionBar.findViewById(R.id.iv_user_setting);
        ivHomeFindArts = appActionBar.findViewById(R.id.iv_find_arts);
        mGalleryViewPager = contentView.findViewById(R.id.vp_home_gallery);

        ivHomeUserSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(mLeftContainer)) {
                    mDrawer.closeDrawer(mLeftContainer);
                    swipeStatus = MID_BTN;
                } else {
                    swipeStatus = LEFT_BTN;
                    mDrawer.openDrawer(mLeftContainer);
                }
            }
        });

        ivHomeFindArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerOpen(mRightContainer)) {
                    mDrawer.closeDrawer(mRightContainer);
                    swipeStatus = MID_BTN;
                } else {
                    mDrawer.openDrawer(mRightContainer);
                    swipeStatus = RIGHT_BTN;
                }
            }
        });
        ViewGroup.LayoutParams params = mGalleryViewPager.getLayoutParams();
        params.width = DeviceInfo.getDisplayMetrics(getContext()).widthPixels;
        params.height = DeviceInfo.getDisplayMetrics(getContext()).heightPixels - (int) getContext().getResources().getDimension(R.dimen.action_bar_height);
        mGalleryViewPager.setLayoutParams(params);
        mAdapter = new IdiotGalleryAdapter(getChildFragmentManager(), mFragments);
        mGalleryViewPager.setAdapter(mAdapter);
        mGalleryViewPager.setOffscreenPageLimit(1); // 默认值
        mGalleryViewPager.setCurrentItem(mFragmentsLength - 2);
        int vrPaddingLeft = mGalleryViewPager.getPaddingLeft();
        int vrPageWidth = params.width - vrPaddingLeft * 2;
        float excursion = -(float) vrPaddingLeft / (float) vrPageWidth;
        mGalleryViewPager.setPageTransformer(true, new ScalePageTransformer(excursion));

        appActionBar.hideLeftBtn();
        appActionBar.showUserSetting();
        appActionBar.showFindArts();

        contentView.findViewById(R.id.btn_update).setOnClickListener(v -> updateInfo());
    }

    private void initDate() {
        Calendar c = Calendar.getInstance();
        String content = c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DATE) + "日";
        tvDate.setText(content);
    }

    private void getUserProfile() {
        ImageConfig imageConfig = new ImageConfig.Builder()
                .steepToolBarColor(getContext().getResources().getColor(R.color.transparent))
                .titleBgColor(getContext().getResources().getColor(R.color.white))
                .titleSubmitTextColor(getContext().getResources().getColor(R.color.black))
                .titleTextColor(getContext().getResources().getColor(R.color.black))
                // 开启拍照功能 （默认关闭）
                .pathList(mDatas)
                .requestCode(MainFragment.REQUEST_CODE)
                .mutiSelectMaxSize(1)
                .showCamera()
                //设置显示容器
                .build();
        ImageSelector.open(this, imageConfig);
    }

    private void hideSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 获取软键盘的显示状态
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void toImageDetailFragment(ImageModel model, ImageView shareView) {
        if (mDrawer.isDrawerOpen(mRightContainer)) {
            //mDrawer.closeDrawer(mRightContainer);
            swipeStatus = RIGHT_BTN;
        }
        if(mDrawer.isDrawerOpen(mLeftContainer)) {
            mDrawer.closeDrawer(mLeftContainer);
            swipeStatus = MID_BTN;
        }

        Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
        intent.putExtra("BigImage", model);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), shareView, getContext().getResources().getString(R.string.image_transition));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setExitTransition(new Fade().setDuration(500));
        }
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    @Override
    public void showImageList(List<ImageModel> list) {
        rlFindNothing.setVisibility(View.GONE);
        rvFindArts.setVisibility(View.VISIBLE);
        mFindArtsAdapter.setData(list);
    }

    @Override
    public void showNothing() {
        rlFindNothing.setVisibility(View.VISIBLE);
        rvFindArts.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if(pathList != null && pathList.size() == 1) {
                Glide.with(this)
                        .load(pathList.get(0))
                        .placeholder(R.drawable.v_account_head_picture)
                        .bitmapTransform(new GlideCircleTransform(getContext()))
                        .into(ivUserProfile);
                SharedPreferencesUtils.saveUserProfile(getContext(), pathList.get(0));
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            String text = buttonView.getText().toString();
            etName.setText(text);
            etName.setSelection(text.length());
            mPresenter.searchImages(text);
        }
        switch (buttonView.getId()) {
            case R.id.rb_find_arts_r1_c1:
                rg2.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r1_c2:
                rg2.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r1_c3:
                rg2.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r1_c4:
                rg2.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r2_c1:
                rg1.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r2_c2:
                rg1.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r2_c3:
                rg1.clearCheck();
                rg3.clearCheck();
                break;
            case R.id.rb_find_arts_r3_c1:
                rg1.clearCheck();
                rg2.clearCheck();
                break;
            case R.id.rb_find_arts_r3_c2:
                rg1.clearCheck();
                rg2.clearCheck();
                break;
            case R.id.rb_find_arts_r3_c3:
                rg1.clearCheck();
                rg2.clearCheck();
                break;
            default:
                break;
        }
    }

    class IdiotGalleryAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragmentList;

        public IdiotGalleryAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return null == mFragmentList ? 0 : mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        public void setData(int pos, Fragment fragment){
            if(pos < mFragmentList.size()){
                mFragmentList.set(pos, fragment);
            }else {
                mFragmentList.add(fragment);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateInfo(UpdateInfoEvent event){
        if(isFirstOpen){
            isFirstOpen = false;
        }else {
            updateInfo();
        }
    }

    private void updateInfo() {
        // 更新左边界面的时间
        initDate();
        // 更新主界面的图像
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        GalleryItemFragment item = new GalleryItemFragment();
        int yearOffset = getYearOffset(c.get(Calendar.YEAR));
        item.setData(new DateModel(c.get(Calendar.YEAR) - yearOffset, c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)), 1);
        for(int i = mAdapter.getCount() - 1; i >= 0; i--) {
            GalleryItemFragment fragment = (GalleryItemFragment) mAdapter.getItem(i);
            c.add(Calendar.DAY_OF_MONTH, -1);
            fragment.updateData(new DateModel(c.get(Calendar.YEAR) - yearOffset, c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)), fragment.getOffset() - 1);
            mAdapter.setData(i, fragment);
        }
        mAdapter.setData(mAdapter.getCount(), item);
        mAdapter.notifyDataSetChanged();
    }

    private int getYearOffset(int year) {
        int offset = 0;
        while(year >= 2019) {
            year -= 5;
            offset += 5;
        }
        return offset;
    }

    private void clearFindArtsItem() {
        if(rlFindNothing.getVisibility() == View.VISIBLE){
            rlFindNothing.setVisibility(View.GONE);
            return;
        }
        if(mFindArtsAdapter == null || mFindArtsAdapter.getItemCount() == 0) return;
        mFindArtsAdapter.clearData();
        rg1.clearCheck();
        rg2.clearCheck();
        rg3.clearCheck();
    }
}
