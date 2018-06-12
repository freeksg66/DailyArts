package com.github.dailyarts.utils;

/**
 * Created by legao005426 on 2018/5/4.
 */

public class StringUtils {
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNull(CharSequence cs) {
        return cs == null || cs.length() == 0 || cs.equals("null");
    }
}
