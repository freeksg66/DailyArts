package com.github.dailyarts.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class OSUtils {
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";


    public static boolean isEMUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_CONFIG_HW_SYS_VERSION, null) != null;

        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMIUI(final Context context) {
        try {
            final PackageManager manager = context.getPackageManager();
            PackageInfo pkgInfo = manager.getPackageInfo("com.xiaomi.xmsf", PackageManager.GET_SERVICES);
            if (pkgInfo != null && pkgInfo.versionCode >= 105) {
                return true;
            }
        } catch (Throwable e) {
        }
        return false;
    }

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    //读取系统配置信息build.prop类
    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

    }
}
