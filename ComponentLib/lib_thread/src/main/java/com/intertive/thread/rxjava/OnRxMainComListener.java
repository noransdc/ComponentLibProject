package com.intertive.thread.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Nevio
 * on 2022/2/7
 */
public interface OnRxMainComListener extends Observer<Object> {


    @Override
    default void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    default void onNext(@NonNull Object t) {

    }

    @Override
    default void onError(@NonNull Throwable e) {

    }

    @Override
    default void onComplete() {
        onMainComplete();
    }

    void onMainComplete();
}
