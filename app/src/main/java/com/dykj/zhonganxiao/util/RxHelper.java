package com.dykj.zhonganxiao.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author 周竹
 * @file RxHelper
 * @brief
 * @date 2018/4/16 下午4:48
 * Copyright (c) 2017
 * All rights reserved.
 */
public class RxHelper {


    /**
     * 倒计时
     *
     * @param time
     * @return
     */
    public static Disposable countdown(final long time, final onCountdownOnClickListener listener) {
        return Flowable.intervalRange(0, time + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (listener != null)
                            listener.onCountdown(time - aLong);

                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (listener != null)
                            listener.onFinish();

                    }
                })
                .subscribe();
    }

    public interface onCountdownOnClickListener {
        void onCountdown(long num);

        void onFinish();
    }
}
