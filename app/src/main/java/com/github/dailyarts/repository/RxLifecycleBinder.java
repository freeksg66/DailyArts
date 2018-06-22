package com.github.dailyarts.repository;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * Created by legao005426 on 2018/6/11.
 */

public interface RxLifecycleBinder {
    <T> LifecycleTransformer<T> bindLifecycle();
}
