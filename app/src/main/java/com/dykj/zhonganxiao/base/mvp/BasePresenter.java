package com.dykj.zhonganxiao.base.mvp;



import com.dykj.zhonganxiao.base.http.ApiRetrofit;
import com.dykj.zhonganxiao.base.http.ApiServer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * File descripition:   创建Presenter基类
 *
 * @author gk
 * @date 2019/6/19
 */

public abstract class BasePresenter<V extends BaseView> {
    private CompositeDisposable compositeDisposable;
    public V baseView;
    protected ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    public BasePresenter(V baseView) {
        this.baseView = baseView;
    }

    /**
     * 解除绑定
     */
    public void detachView() {
        removeDisposable();
        baseView = null;
    }

    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return baseView;
    }

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
