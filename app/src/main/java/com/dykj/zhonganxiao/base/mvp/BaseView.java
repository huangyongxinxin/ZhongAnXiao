package com.dykj.zhonganxiao.base.mvp;


import com.dykj.zhonganxiao.bean.BaseResponse;

/**
 * File descripition:   基本回调 可自定义添加所需回调
 *
 * @author gk
 * @date 2019/6/19
 */

public interface BaseView {
    /**
     * 显示dialog
     */
    void showLoading();
    /**
     * 隐藏 dialog
     */

    void hideLoading();
    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);
    /**
     * 错误码
     */
    void onErrorCode(BaseResponse data);
}
