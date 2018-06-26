package com.github.dailyarts.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.dailyarts.service.UpdateInfoService;

/**
 * Created by legao005426 on 2018/6/26.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UpdateInfoService.class);
        context.startService(i);
    }
}
