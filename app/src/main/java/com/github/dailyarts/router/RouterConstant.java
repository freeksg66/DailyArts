package com.github.dailyarts.router;

import com.github.dailyarts.entity.ImageModel;

/**
 * 路由常数规则：
 * 1、目标页面无请求参数，且无返回参数，则直接写Path常数
 * 2、目标页面有请求参数或者返回参数，则需用接口封装页面所需参数，规则如下：
 * a、页面路径：ACTION
 * b、请求参数：EXTRA_参数值类型_参数名
 * c、返回参数：RESULT_参数值类型_参数名
 * d、接口命名：页面页+Const
 * 注意：页面间参数传递，原则上支支持基本数据类型。
 * 实例{@link AboutActivityConst}
 * 如果复杂类型需要用json传递
 * <p>
 * <p>
 * Created by legao005426 on 2018/6/11.
 */

public interface RouterConstant {
    String MAIN = "/main/MainActivity";

    interface AboutActivityConst {
        String PATH = "/main/AboutActivity";
    }

    interface MainActivityConst {
        String PATH = "/main/MainActivity";
    }

    interface MyGalleryActivityConst {
        String PATH = "/main/MyGalleryActivity";
    }

    interface CommentsActivityConst {
        String PATH = "/main/CommentsActivity";
    }

    interface PaintingDemandActivityConst {
        String PATH = "/main/PaintingDemandActivity";
    }

    interface WatchImageActivityConst {
        String PATH = "/main/WatchImageActivity";
        String BIG_IMAGE_URL = "bigImageUrl";
    }
}
