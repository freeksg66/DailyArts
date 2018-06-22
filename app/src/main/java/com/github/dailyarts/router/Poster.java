package com.github.dailyarts.router;

import com.alibaba.android.arouter.facade.Postcard;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class Poster {
    private Postcard mPostcard;

    Poster(Postcard postcard) {
        mPostcard = postcard;
    }

    public Postcard addFlag(int... flags) {
        int addingFlag = mPostcard.getFlags();
        for (int temp : flags) {
            addingFlag |= temp;
        }
        return mPostcard.withFlags(addingFlag);
    }
}
