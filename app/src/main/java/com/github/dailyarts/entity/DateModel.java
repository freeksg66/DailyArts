package com.github.dailyarts.entity;

import java.util.Calendar;

/**
 * Created by legao005426 on 2018/5/17.
 */

public class DateModel {
    private Calendar mCalendar;
    public int year, month, day, offset;

    public DateModel(int year, int month, int day, int offset) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.offset = offset;
        mCalendar = Calendar.getInstance();
    }

    public DateModel getYesterday() {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return new DateModel(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), offset - 1);
    }

    public DateModel getTomorrow() {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        return new DateModel(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), offset + 1);
    }

    public int toInt() {
        return year * 10000 + month * 100 + day;
    }
}
