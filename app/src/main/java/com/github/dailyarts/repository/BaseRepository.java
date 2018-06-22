package com.github.dailyarts.repository;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by legao005426 on 2018/6/12.
 */

public abstract class BaseRepository {
    protected RxLifecycleBinder mLifecycleBinder;

    public BaseRepository(RxLifecycleBinder binder) {
        mLifecycleBinder = binder;
    }

    @Deprecated
    public BaseRepository() {
    }

    public <T> Observable.Transformer<T, T> defaultRxConfig() {
        if (null == mLifecycleBinder) {
            return tObservable -> tObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());
        }
        return tObservable -> tObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(mLifecycleBinder.bindLifecycle());
    }
}
