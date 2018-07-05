package com.github.dailyarts.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.dailyarts.config.Constant;
import com.github.dailyarts.entity.ImageModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by legao005426 on 2018/6/12.
 * sharedPreferences数据管理类
 */

public class SharedPreferencesUtils {
    public static boolean saveCollectImage(Context context, ImageModel model) {
        List<ImageModel> modelList = getCollectImages(context);
        if (modelList == null) return false;
        for (ImageModel item : modelList) {
            if (item.getId().equals(model.getId())) {
                return true;
            }
        }
        modelList.add(model);
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return preferences.edit().putString(Constant.SHAREPREFRENCE_COLLECTION_IMAGES, gson.toJson(modelList)).commit();
    }

    public static boolean saveCollectImages(Context context, List<ImageModel> list) {
        if (list == null || list.size() < 0) return false;
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return preferences.edit().putString(Constant.SHAREPREFRENCE_COLLECTION_IMAGES, gson.toJson(list)).commit();
    }

    public static List<ImageModel> getCollectImages(Context context) {
        List<ImageModel> list = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        String strJson = preferences.getString(Constant.SHAREPREFRENCE_COLLECTION_IMAGES, null);
        if (strJson == null) {
            return list;
        }
        Gson gson = new Gson();
        list = gson.fromJson(strJson, new TypeToken<List<ImageModel>>() {
        }.getType());
        return list;
    }

    public static boolean checkCollect(Context context, ImageModel imageModel) {
        List<ImageModel> list = getCollectImages(context);
        for (ImageModel item : list) {
            if (item.getId().equals(imageModel.getId())) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteCollectImage(Context context, ImageModel imageModel) {
        List<ImageModel> list = getCollectImages(context);
        if (list == null || list.size() <= 0) return false;
        for (ImageModel item : list) {
            if (item.getId().equals(imageModel.getId())) {
                list.remove(item);
                return saveCollectImages(context, list);
            }
        }
        return false;
    }

    public static void saveUserProfile(Context context, String uri) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        preferences.edit().putString(Constant.SHAREPREFRENCE_USER_PROFILE, uri).apply();
    }

    public static String getUserProfile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        return preferences.getString(Constant.SHAREPREFRENCE_USER_PROFILE, null);
    }
}
