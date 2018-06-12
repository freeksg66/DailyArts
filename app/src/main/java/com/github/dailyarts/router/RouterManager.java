package com.github.dailyarts.router;

import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.dailyarts.ui.fragment.BaseFragment;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class RouterManager {
    RouterUriBuilder builder;

    private RouterManager() {
    }

    public void inject(Object obj) {
        ARouter.getInstance().inject(obj);
    }

    private static class InstanceHolder {
        private static final RouterManager INSTANCE = new RouterManager();
    }

    public static RouterManager init(boolean debug, Application application) {
        if (debug) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化
        return InstanceHolder.INSTANCE;
    }

    public static RouterManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public Poster build(String path) {
        Postcard postcard = ARouter.getInstance().build(path);
        return new Poster(postcard);
    }

    public Poster build(Uri uri) {
        Postcard postcard = ARouter.getInstance().build(uri);
        return new Poster(postcard);
    }

    /**
     * 启动方式
     * 无参数path
     * 外部uri
     * 自己拼接uri
     */

    public void startWithFlag(String path, int... flags) {
        build(path).addFlag(flags).navigation();
    }

    public void startWithFlag(Uri uri, int... flags) {
        build(uri).addFlag(flags).navigation();
    }

    /**
     * 无参数
     * /test/testactivity
     *
     * @param path
     */
    public void startActivity(String path) {
        ARouter.getInstance().build(path).navigation();
    }


    /**
     * 外部uri
     * sohuauto://sohuauto.auto.sohu.com/test/testactivity?key=value&key1=value1
     *
     * @param uri
     */
    public void startActivity(Uri uri) {
        ARouter.getInstance().build(uri).navigation();
    }

    /**
     * 带Parcelable参数
     */
    public void startActivity(String uri, String tag, Parcelable value){
        ARouter.getInstance().build(uri).withParcelable(tag, value).navigation();
    }

    /**
     * 无参数
     * /test/testactivity
     *
     * @param path
     */
    public void startActivityForResult(String path, Activity activity, int requestCode) {
        ARouter.getInstance().build(path).navigation(activity, requestCode);
    }

    /**
     * 外部uri
     * sohuauto://sohuauto.auto.sohu.com/test/testactivity?key=value&key1=value1
     *
     * @param uri
     */
    public void startActivityForResult(Uri uri, Activity activity, int requestCode) {
        ARouter.getInstance().build(uri).navigation(activity, requestCode);
    }

    /**
     * -----------------------------------------------------------------------------------
     * 带参数采用uri形式 构造如下形式
     * sohuauto://sohuauto.auto.sohu.com/test/testactivity?key=value&key1=value1
     *
     * @param baseUrl
     * @return
     */
    public RouterManager createUri(String baseUrl) {
        builder = new RouterUriBuilder()
                .baseUri(baseUrl);
        return this;
    }

    public RouterManager addParams(String key, String value) {
        builder.addParams(key, value);
        return this;
    }


    public void buildByUri() {
        startActivity(builder.buildUri());
    }

    public void buildByUriForResult(Activity activity, int requestCode) {
        startActivityForResult(builder.buildUri(), activity, requestCode);
    }

    public void buildByUriWithFlag(int... flags) {
        startWithFlag(builder.buildUri(), flags);
    }

    public BaseFragment getFragment(String path){
        return (BaseFragment) ARouter.getInstance().build(path).navigation();
    }
}
