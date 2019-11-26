package com.dykj.zhonganxiao.base.http;

/**
 * File descripition:   异常处理基类
 *
 * @author gk
 * @date 2019/6/24
 */

public class ApiException extends RuntimeException {
    private String status;

    public ApiException(String status, String msg) {
        super(msg);
        this.status = status;
    }

    public String getErrorCode() {
        return status;
    }

}
