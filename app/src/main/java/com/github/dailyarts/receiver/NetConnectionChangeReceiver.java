package com.github.dailyarts.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.dailyarts.event.NetConnectionChangeEvent;
import com.github.dailyarts.utils.DeviceInfo;

import org.greenrobot.eventbus.EventBus;

import github.nisrulz.easydeviceinfo.EasyNetworkMod;

/**
 * Created by legao005426 on 2018/6/20.
 */

public class NetConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int networkType = DeviceInfo.getNetworkType(context);
        boolean isNetworkAvailable = DeviceInfo.isNetworkAvailable(context);

        NetConnectionChangeEvent event = new NetConnectionChangeEvent();
        event.isAvailable = isNetworkAvailable;

        if (!isNetworkAvailable){
            event.networkState = NetConnectionChangeEvent.DISABLED;
            EventBus.getDefault().post(event);
            return;
        }

        switch (networkType){
            case EasyNetworkMod.CELLULAR_UNKNOWN:
                event.networkState = NetConnectionChangeEvent.NET_UNKNOWN;
                break;

            case EasyNetworkMod.CELLULAR_UNIDENTIFIED_GEN:
                event.networkState = NetConnectionChangeEvent.UNIDENTIFIED;
                break;

            case EasyNetworkMod.CELLULAR_2G:
                event.networkState = NetConnectionChangeEvent.G2;
                break;

            case EasyNetworkMod.CELLULAR_3G:
                event.networkState = NetConnectionChangeEvent.G3;
                break;

            case EasyNetworkMod.CELLULAR_4G:
                event.networkState = NetConnectionChangeEvent.G4;

                break;
            case EasyNetworkMod.WIFI_WIFIMAX:
                event.networkState = NetConnectionChangeEvent.WIFI;

                break;

            default:
                event.networkState = NetConnectionChangeEvent.UNKNOWN;
                break;
        }

        EventBus.getDefault().post(event);
    }
}
