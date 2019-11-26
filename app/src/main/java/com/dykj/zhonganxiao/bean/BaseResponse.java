package com.dykj.zhonganxiao.bean;

import com.dykj.zhonganxiao.base.http.BaseUrl;

public class BaseResponse<T> {
    public String errcode;
    public String message;
    public T data;


    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //status == 0请求成功
    public boolean success() {
        return BaseUrl.SUCCESS_CODE.equals(errcode);
    }
    //请求中errcode请求失败码
    public boolean error() {
        return BaseUrl.SPECIAL.equals(errcode)||
                BaseUrl.ERROR_CODE.equals(errcode)||
                BaseUrl.PAY_PWD_ERROR.equals(errcode)||
                BaseUrl.LOGIN_OVERDUE.equals(errcode);
    }





    @Override
    public String toString() {
        return "BaseResponse{" +
                "errcode='" + errcode + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}