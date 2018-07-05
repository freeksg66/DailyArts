package com.github.dailyarts.ui.widget.imagespickers;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.github.dailyarts.R;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.ui.widget.imagespickers.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class ImageSelectorActivity extends FragmentActivity implements ImageSelectorFragment.Callback {
    public static final String EXTRA_RESULT = "select_result";

    private ArrayList<String> pathList = new ArrayList<>();

    private ImageConfig imageConfig;
    private AppActionBar mActionbar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.image_selector_activity);

        imageConfig = ImageSelector.getImageConfig();

        Utils.hideTitleBar(this, R.id.imageselector_activity_layout, imageConfig.getSteepToolBarColor());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, ImageSelectorFragment.class.getName(), null))
                .commit();
        mActionbar = (AppActionBar) findViewById(R.id.action_bar);
        init();
    }

    private void init() {
        mActionbar.setBackgroundColor(imageConfig.getTitleBgColor());

        pathList = imageConfig.getPathList();

        mActionbar.setListener(new AppActionBar.ActionBarListener() {
            @Override
            public void onEvent(AppActionBar.ActionBarEvent event) {
                if (event == AppActionBar.ActionBarEvent.LEFT_TEXT_CLICK) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else if (event == AppActionBar.ActionBarEvent.RIGHT_TEXT_CLICK) {
                    if (pathList != null && pathList.size() > 0) {
                        SelectedFinish();
                    }
                }
            }
        });


        if (pathList == null || pathList.size() <= 0) {
            mActionbar.setRightTx(getResources().getString(R.string.finish));
        } else {
            mActionbar.setRightTx(getResources().getString(R.string.finish) + "(" + pathList.size() + "/" + imageConfig.getMaxSize() + ")");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //裁剪返回
        if (requestCode == ImageSelector.IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            pathList.add(cropImagePath);
            SelectedFinish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SelectedFinish() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT, pathList);
        setResult(RESULT_OK, intent);

        //改变gridview的内容
        if (imageConfig.getContainerAdapter() != null) {
            imageConfig.getContainerAdapter().refreshData(pathList, imageConfig.getImageLoader());
        }
        finish();
    }

    private String cropImagePath;

    private void crop(String imagePath, int aspectX, int aspectY, int outputX, int outputY) {
        File file;
        if (Utils.existSDCard()) {
            file = new File(Environment.getExternalStorageDirectory() + imageConfig.getFilePath(), Utils.getImageName());
        } else {
            file = new File(getCacheDir(), Utils.getImageName());
        }


        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, ImageSelector.IMAGE_CROP_CODE);
    }


    @Override
    public void onChangeAlbum(String albumName) {
        mActionbar.setTitle(albumName);
    }

    @Override
    public void onSingleImageSelected(String path) {
        if (imageConfig.isCrop()) {
            crop(path, imageConfig.getAspectX(), imageConfig.getAspectY(), imageConfig.getOutputX(), imageConfig.getOutputY());
        } else {
            pathList.add(path);
            SelectedFinish();
        }
    }

    @Override
    public void onImageSelected(String path) {
        if (!pathList.contains(path)) {
            pathList.add(path);
        }
        if (pathList.size() > 0) {
            mActionbar.setRightTx(getResources().getString(R.string.finish) + "(" + pathList.size() + "/" + imageConfig.getMaxSize() + ")");
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (pathList.contains(path)) {
            pathList.remove(path);
            mActionbar.setRightTx(getResources().getString(R.string.finish) + "(" + pathList.size() + "/" + imageConfig.getMaxSize() + ")");
        } else {
            mActionbar.setRightTx(getResources().getString(R.string.finish) + "(" + pathList.size() + "/" + imageConfig.getMaxSize() + ")");
        }
        if (pathList.size() == 0) {
            mActionbar.setRightTx(getResources().getString(R.string.finish));
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            if (imageConfig.isCrop()) {
                crop(imageFile.getAbsolutePath(), imageConfig.getAspectX(), imageConfig.getAspectY(), imageConfig.getOutputX(), imageConfig.getOutputY());
            } else {
                pathList.add(imageFile.getAbsolutePath());
                SelectedFinish();
            }
        }
    }

}
