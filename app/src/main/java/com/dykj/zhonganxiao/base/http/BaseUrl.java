package com.dykj.zhonganxiao.base.http;

/**
 * 接口地址
 * created by gk
 * date 2019-06-25
 */
public class BaseUrl {
    //base Ip
    public static String baseUrl = "http://anjigoo.missmaa.com/Api/";

    //服务器返回成功的 code
    //-101：系统密钥错误
    // -102：API接口异常1(请求类型错误)
    // -103：API接口异常2(header数据中的密钥错误)
    // -104：签名错误
    // -105 ：请先登录
    // -106：登录失效
    // -107：账号冻结
    public static final String SUCCESS_CODE = "1";//接口返回成功状态码
    public static final String ERROR_CODE = "0";//接口返回失败状态码
    public static final String SPECIAL = "-105";//请先登录
    public static final String LOGIN_OVERDUE = "-106";//登录失效
    public static final String PAY_PWD_ERROR = "-100";//支付密码错误
}
