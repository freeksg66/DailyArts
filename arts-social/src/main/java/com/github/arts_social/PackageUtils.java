package com.github.arts_social;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class PackageUtils {
    public final static String PACKAGE_NAME_SINA_WEIBO = "com.sina.weibo";
    public final static String PACKAGE_NAME_MOBILE_QQ = "com.tencent.mobileqq";
    public final static String PACKAGE_NAME_WECHAT = "com.tencent.mm";
    public final static String PACKAGE_NAME_MOBILE_QZONE = "com.qzone";

    public static boolean isApplicationExist(Context context, String packageName){
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo temp:packageInfos) {
            if (packageName.equals(temp.packageName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否安装某个应用
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean isAppInstalled(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    //判断某一个类是否存在任务栈里面
    public static boolean isActivityInBackStack(Activity sourceActivity, String activity) {
        try {
            Class clazz = Class.forName(activity);
            Intent intent = new Intent(sourceActivity, clazz);
            ComponentName cmpName = intent.resolveActivity(sourceActivity.getPackageManager());
            boolean flag = false;
            if (cmpName != null) { // 说明系统中存在这个activity
                ActivityManager am = (ActivityManager) sourceActivity.getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
                for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                    if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                        flag = true;
                        break;  //跳出循环，优化效率
                    }
                }
            }

            return flag;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
