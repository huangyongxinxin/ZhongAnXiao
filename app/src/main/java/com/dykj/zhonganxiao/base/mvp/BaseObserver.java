package com.dykj.zhonganxiao.base.mvp;

import android.text.TextUtils;

import com.dykj.zhonganxiao.base.http.BaseUrl;
import com.dykj.zhonganxiao.bean.BaseResponse;
import com.dykj.zhonganxiao.util.net.NetWorkUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * File descripition:   数据处理基类
 *
 * @author gk
 * @date 2019/6/19
 */

public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {
    protected BaseView view;
    private boolean showDialogEnable = true;
    /**
     * 网络连接失败  无网
     */
    public static final int NETWORK_ERROR = 100000;
    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;

    /**
     * 其他所有情况
     */
    public static final int NOT_TRUE_OVER = 1004;


    public BaseObserver(BaseView view) {
        this.view = view;
    }

    public BaseObserver(BaseView view, boolean showDialogEnable) {
        this.view = view;
        this.showDialogEnable = showDialogEnable;
    }

    @Override
    protected void onStart() {
        if (view != null && NetWorkUtils.isNetWorkAvailable()&&showDialogEnable) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(BaseResponse<T> data) {
        try {
            if (view != null && showDialogEnable) {
                view.hideLoading();
            }
            if (data.success()) {//请求成功
                onSuccess(data);
            } else {//请求失败
                view.onErrorCode(data);

                if (!TextUtils.isEmpty(data.getErrcode())) {
                    if (data.getErrcode().equals(BaseUrl.LOGIN_OVERDUE)) {

                    }else if (data.getErrcode().equals(BaseUrl.SPECIAL)){

                    }else {
                        onError(data.getMessage());
                    }

                } else {
                    onException(PARSE_ERROR, data.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.toString());
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null&&showDialogEnable) {
            view.hideLoading();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK, "");
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR, "");
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT, "");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR, "");
            e.printStackTrace();
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("未知错误");
            }
        }
    }


    private void onException(int unknownError, String message) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError("连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError("连接超时");
                break;
            case BAD_NETWORK:
                onError("网络超时");
                break;
            case PARSE_ERROR:
                onError("数据解析失败");
                break;
            //非true的所有情况
            case NOT_TRUE_OVER:
                onError(message);
                break;
            default:
                break;
        }
    }

    //消失写到这 有一定的延迟  对dialog显示有影响
    @Override
    public void onComplete() {
        if (view != null&&showDialogEnable) {
            view.hideLoading();
        }
    }

    public abstract void onSuccess(BaseResponse<T> mBase) throws IOException;

    public abstract void onError(String msg);
}
