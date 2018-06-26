package com.github.dailyarts.event;

import com.github.dailyarts.entity.DateModel;

/**
 * Created by legao005426 on 2018/6/26.
 */

public class UpdateInfoEvent {
    public DateModel today;

    public UpdateInfoEvent(DateModel dateModel){
        today = dateModel;
    }
}
