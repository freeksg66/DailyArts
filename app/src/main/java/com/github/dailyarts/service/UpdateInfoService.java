package com.github.dailyarts.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.dailyarts.entity.DateModel;
import com.github.dailyarts.event.UpdateInfoEvent;
import com.github.dailyarts.receiver.AlarmReceiver;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by legao005426 on 2018/6/26.
 */

public class UpdateInfoService extends Service{
    private static final String TAG = "UpdateInfoService";
    private int num = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        new Thread(() -> updateInfo()).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = getRemainingTime(); // 一天的剩余时间
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateInfo(){
        Log.e(TAG, "num = " + new Date().toString());
        Calendar c = Calendar.getInstance();
        DateModel dateModel = new DateModel(c.get(Calendar.YEAR) - 4, c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        EventBus.getDefault().post(new UpdateInfoEvent(dateModel));
    }

    private long getRemainingTime(){
        Calendar cal = Calendar.getInstance();
        long l1 = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long l0 = cal.getTimeInMillis();
        return l0 - l1;
    }
}
