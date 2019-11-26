package com.dykj.zhonganxiao.constants;

/**
 * @author 周竹
 * @file UpdatePersonal
 * @brief
 * @date 2018/5/10 下午8:09
 * Copyright (c) 2017
 * All rights reserved.
 */
public class RefreshEvent<T> {
    /**
     * what
     * 0 ,代表登录成功，刷新个人信息
     * 1 ,退出登录
     */
    public int what;
    public static final int LOGIN = 0;//登录成功
    public static final int LOGOUT = 1;//退出登录
    public static final int VIDEO_SUCCESS = 2;//视频裁剪成功
    public static final int OPEN_INPUT = 3;//打开评论软键盘
    public static final int POS = 4;//跳转登录索引
    public static final int REFRESH_USER_INFO = 5;//刷新用户信息
    public static final int REFRESH_MINE = 6;//刷新我的页面数据
    public static final int REFRESH_REFIF_INFO = 7;//刷新改装详情页面数据
    public static final int CHOOSE_ADDRESS = 8;//选择收货地址
    public static final int CHOOSE_INVOICE = 9;//选择发票
    public static final int REFRESH_SHOP_CART = 10;//刷新购物车
    public static final int REFRESH_ORDER = 11;//刷新订单列表
    public static final int REFRESH_ORDER_DETAIL = 12;//刷新订单详情
    public static final int REFRESH_ORDER_TWO = 13;//支付成功后刷新订单列表
    public static final int OPEN_SCAN = 14;//打开扫码
    public static final int REFRESH_HOME_PRODUCT = 15;//刷新首页所有商品列表
    public static final int GO_HOME = 16;//回首页
    public static final int GET_SPEC_PRICE = 17;//获取规格价格
    public static final int INVOICE = 18;//传递发票
    public static final int GO_CART = 19;//去购物车
    public static final int GO_PHOTO_SEARCH = 20;//选择图片去搜索
    public static final int GO_SERVER = 21;//打开客服
    public static final int CHOOSE_TIME = 22;//选择日期
    public static final int REFRESH_FORUM_LIST = 23;//刷新以商会友列表
    public static final int REFRESH_FORUM_END = 24;//刷新以商会友列表完成
    public static final int REFRESH_FORUM_home_LIST = 25;//刷新以商会友个人主页列表
    public static final int REFRESH_FORUM_home_END = 26;//刷新以商会友列表个人主页完成
    public static final int REFRESH_STORE_CLASS = 27;//店铺 跳转到商品 并刷新
    public static final int REFRESH_CLASS = 28;//分类页面刷新


    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RefreshEvent(int what,T data) {
        this.what = what;
        this.data = data;
    }
}
