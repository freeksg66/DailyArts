package com.github.dailyarts.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.UUID;

import github.nisrulz.easydeviceinfo.EasyNetworkMod;

/**
 * Created by legao005426 on 2018/5/7.
 */

public class DeviceInfo {
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

//    public static String getIP() {
//        return new EasyNetworkMod(BaseApplication.getBaseApplication()).getIPv4Address();
//    }

    /**
     * 受限于各种因素 该方案是不可靠的
     * 比如权限变化时  取到的两次可能不一致
     *
     * @param context
     * @return
     */

    @SuppressWarnings("MissingPermission")
    public static String getDeviceId(Context context) {
        String uniqueID = null;
        //读取imei
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                uniqueID = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
        }
//            读取mac   http://wetest.qq.com/lab/view/116.html  mac问题太多暂时屏蔽
//            if (StringUtils.isNull(result)) {
//                result = getMac(context);
//            }
//            读AndroidID
        if (StringUtils.isNull(uniqueID)) {
            uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
//            读UUID
        if (StringUtils.isNull(uniqueID)) {
            uniqueID = getUUID(context);
        }
        return uniqueID;
    }
//    /**
    //  * 美团代码 后续可考察可行性
    //**/
//    static String deviceId(Context context) {
//        try {
//            String imei = null;
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                // 1 compute IMEI
//                TelephonyManager TelephonyMgr = (TelephonyManager) context
//                        .getSystemService(Context.TELEPHONY_SERVICE);
//
//                if (TelephonyMgr != null) {
//                    imei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
//                }
//                if (!TextUtils.isEmpty(imei)) {
//                    // got imei, return it
//                    return imei.trim();
//                }
//            }
//            imei = null;
//            // 2 compute DEVICE ID
//            String devIDShort = “35”
//            + // we make this look like a valid IMEI
//                    Build.BOARD.length() % 10 + Build.BRAND.length() % 10
//                    + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
//                    + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
//                    + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
//                    + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
//                    + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
//                    + Build.USER.length() % 10; // 13 digits
//
//            // 3 android ID - unreliable
//            String androidId = Settings.Secure.getString(
//                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
//
//            // 4 wifi manager read MAC address - requires
//            // android.permission.ACCESS_WIFI_STATE or comes as null
//            WifiManager wm = (WifiManager) context
//                    .getSystemService(Context.WIFI_SERVICE);
//            String wlanMac = null;
//            if (wm != null) {
//                try {
//                    wlanMac = wm.getConnectionInfo().getMacAddress();
//                } catch (Exception e) {
//                    UUIDHelper.getInstance().getEventLogProvider().throwableReport(e);
////                    Log.d(“UUID”, e.getMessage(), e);
//                }
//            }
//
//            // 5 Bluetooth MAC address android.permission.BLUETOOTH required, so
//            // currenty just comment out, in case we use this method later
//            String btMac = null;
//
//            /*
//             * BluetoothAdapter bluetoothAdapter = null; // Local Bluetooth
//             * adapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//             * if (bluetoothAdapter != null) { try { btMac =
//             * bluetoothAdapter.getAddress(); } catch (Exception e) {
//             * Log.d(“UUID”, e.getMessage(), e); } }
//             */
//
//            // 6 SUM THE IDs
//            String devIdLong = imei + devIDShort + androidId + wlanMac + btMac;
//            MessageDigest m = null;
//            try {
//                m = MessageDigest.getInstance(“MD5");
//            } catch (NoSuchAlgorithmException e) {
//                UUIDHelper.getInstance().getEventLogProvider().throwableReport(e);
////                Log.d(“UUID”, e.getMessage(), e);
//            }
//            m.update(devIdLong.getBytes(), 0, devIdLong.length());
//            byte md5Data[] = m.digest();
//
//            String uniqueId = “”;
//            for (int i = 0, len = md5Data.length; i < len; i++) {
//                int b = (0xFF & md5Data[i]);
//                // if it is a single digit, make sure it have 0 in front (proper
//                // padding)
//                if (b <= 0xF)
//                    uniqueId += “0”;
//                // add number to string
//                uniqueId += Integer.toHexString(b);
//            }
//            uniqueId = uniqueId.toUpperCase();
//            if (uniqueId.length() > 15) {
//                uniqueId = uniqueId.substring(0, 15);
//            }
//            return uniqueId.trim();
//        } catch (Throwable t) {
//            UUIDHelper.getInstance().getEventLogProvider().throwableReport(t);
//        }
//        return “DeviceId0";
//    }

    private static String getUUID(Context context) {
        String uniqueID;
        SharedPreferences sharedPrefs = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString(PREF_UNIQUE_ID, uniqueID);
            editor.apply();
        }
        return uniqueID;
    }

    /**
     * MAC地址通过非系统接口可采集(6.0以下通过系统接口采集)，采集场景仅限于处于wifi连接状态。
     * 非系统接口，其实指的就是直接读取文件的方式
     * 下面是6.0以下的方式
     *
     * @param context
     * @return
     */
    @SuppressWarnings("MissingPermission")
    private static String getMac(Context context) {
        try {
            WifiManager localWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_WIFI_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
                return localWifiInfo.getMacAddress();
            }
            return "";
        } catch (Exception localException) {

        }
        return "";
    }

    public static boolean isNetworkAvailable(Context context) {
        if (null == context) return false;
        return new EasyNetworkMod(context).isNetworkAvailable();
    }

    public static boolean isWifiEnabled(Context context) {
        if (null == context) return false;
        return new EasyNetworkMod(context).isWifiEnabled();
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    public static int getNetworkType(Context context) {
        return new EasyNetworkMod(context).getNetworkType();
    }


    /**
     * 根据dip返回当前设备上的px值
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2Px(Context context, int dip) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        float density = dm.density;
        return (int) (dip * density);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
