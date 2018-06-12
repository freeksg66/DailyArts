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
        if(modelList == null) return false;
        else modelList.add(model);
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        return preferences.edit().putString(Constant.SHAREPREFRENCE_COLLECTION_IMAGES, gson.toJson(modelList)).commit();
    }

    public static List<ImageModel> getCollectImages(Context context){
        List<ImageModel> list = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHAREPREFRENCE, Context.MODE_PRIVATE);
        String strJson = preferences.getString(Constant.SHAREPREFRENCE_COLLECTION_IMAGES, null);
        if(strJson == null){
            return  list;
        }
        Gson gson = new Gson();
        list = gson.fromJson(strJson, new TypeToken<List<ImageModel>>(){}.getType());
        return list;
    }

    public static boolean checkCollect(Context context, ImageModel imageModel){
        List<ImageModel> list = getCollectImages(context);
        return list.contains(imageModel);
    }

    public static boolean deleteCollectImage(Context context, ImageModel imageModel){
        List<ImageModel> list = getCollectImages(context);
        if(list == null || list.size() <= 0) return false;
        for (ImageModel item:list) {
            if(item.getId().equals(imageModel.getId())){
                list.remove(item);
                return true;
            }
        }
        return false;
    }
}
