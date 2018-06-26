package com.github.dailyarts.entity;

import java.util.Calendar;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class DateModel {
    private Calendar mCalendar;
    public int year, month, day;

    public DateModel(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        mCalendar = Calendar.getInstance();
    }

    public int toInt() {
        return year * 10000 + month * 100 + day;
    }
}
