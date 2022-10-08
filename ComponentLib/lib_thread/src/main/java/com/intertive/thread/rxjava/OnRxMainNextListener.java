package com.intertive.thread.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;


/**
 * @author Nevio
 * on 2022/2/7
 */
public interface OnRxMainNextListener<T> extends Observer<T> {


    @Override
    default void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    default void onNext(@NonNull T t) {
        onMain(t);
    }

    @Override
    default void onError(@NonNull Throwable e) {

    }

    @Override
    default void onComplete() {

    }

    void onMain(T t);
}
