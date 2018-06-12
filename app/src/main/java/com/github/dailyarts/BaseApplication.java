package com.github.dailyarts;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.dailyarts.config.DebugConfig;
import com.github.dailyarts.router.RouterManager;

import java.io.Serializable;

/**
 * Created by legao005426 on 2018/6/11.
 */

public abstract class BaseApplication extends Application implements Serializable{
    private static Application mApplication;

    private static Application getBaseApplication(){
        return mApplication;
    }

    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
        //将MultiDex install方法写在此方法内，此方法在onCreate调用之前调用
        MultiDex.install(this);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mApplication = this;
        initInMainProcess();
    }

    protected void initInMainProcess(){
        // 初始化用户登陆信息
        // 获取Device ID传入Activity，方便动态权限管理
        // 初始化路由
        RouterManager.init(DebugConfig.DEBUG, this);
    }
}
