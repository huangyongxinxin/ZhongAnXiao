package com.dykj.zhonganxiao.constants;

import android.content.Context;
import android.util.Base64;


import com.dykj.zhonganxiao.App;
import com.dykj.zhonganxiao.bean.UserInfo;
import com.dykj.zhonganxiao.util.GsonUtil;
import com.dykj.zhonganxiao.util.SetTagAliasUtil;
import com.dykj.zhonganxiao.util.SpUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 保存用户信息
 */

public class UserComm {
    protected static String userToken;
    public static UserInfo userInfo = null;

    public static boolean isLogin() {
        return userInfo != null;
//        return ReadUserInfo();//取出缓存的用户信息再判断是否登录
    }


//    public static String userToken() {
//        if (isLogin()) {
//            userToken = userInfo.getToken();
////            userToken = (String) SpUtils.getParam(SP.TAG_USER_TOKEN,"");
//        } else {
//            userToken = "";
//        }
//        return userToken;
//    }

    //保存用户信息
    public static boolean SaveUserInfo() {
        return SaveUserInfo(userInfo, App.getInstance());
    }

    public static boolean SaveUserInfo(UserInfo userInfo, Context context) {
        boolean result = false;
        if (isLogin()) {
            String data = GsonUtil.getInstance().ObjToJson(userInfo, userInfo.getClass());

            data = Base64.encodeToString(data.getBytes(), AppConfig.TAG_USER_INFO.hashCode());
            SpUtils.setParam(AppConfig.TAG_USER_INFO,data);
            result = true;
        }
        return result;
    }

    public static void SetUserInfo(UserInfo Info) {
        userInfo = Info;
        SaveUserInfo();
    }

    public static void SetUserInfo(UserInfo info, boolean isSave) {
        userInfo = info;
        if (isSave) {
//            SetUserInfo(info);
            SaveUserInfo();
            SetTagAliasUtil.getInstance().setTagAndAlias();
        }
    }



    /**
     * 清除本地缓存的用户登录信息
     */
    public static void ClearUserInfo() {
        ClearUserInfo(App.getInstance());
    }

    /**
     * 清除本地缓存的用户登录信息
     */
    public static void ClearUserInfo(Context context) {
        SpUtils.setParam(AppConfig.TAG_USER_INFO,"");
        SetTagAliasUtil.getInstance().setTagAndAlias();
    }


//    public static HashMap<String, String> getCommonRequest() {
//        HashMap<String, String> commonRequest = new HashMap<>();
//        commonRequest.put("appVersion", currVersionCode());
//        commonRequest.put("source", "Android");
//        commonRequest.put("channelCode", getChannelCode());
//        if (!StringUtils.isEmpty(UserComm.userToken())) {
//            commonRequest.put("token", userToken());
//        }
//        return commonRequest;
//    }

public static HashMap<String, String> getCommonRequest() {
    HashMap<String, String> commonRequest = new HashMap<>();
//    commonRequest.put("signature", getMd5Str() );
    commonRequest.put("timestamp", getStringToDate());
//    commonRequest.put("merchant_id", getMerchant_id());
//    commonRequest.put("shop_id", getShop_id());
//    Log.d("时间戳：：","timestamp--"+getStringToDate()+"::加密串--"+getMd5Str());

    return commonRequest;
}


//public static String getMd5Str(){
//        return MD5Utils.MD5Encode(getStringToDate() + "zhuanlinghua|" + getStrData(),false);
//}

    /*当前日期*/
    public static String getStrData(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /*时间戳*/
    public static String getStringToDate() {
        // 获取系统的时间
        Long longTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        // 转换成字符串
        String strTime = sdf.format(new Timestamp(longTime));
        Date date = new Date();
        try{
            date = sdf.parse(strTime);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        String dateStr = String.valueOf(date.getTime()).substring(0, String.valueOf(date.getTime()).length()-3);//去掉时间戳后三位
        return dateStr;
    }

//    public static String getChannelCode() {
//
//        if (TextUtils.isEmpty(Globel.channelCode) || Globel.channelCode.equals("debug") ||Globel.channelCode.equals("developer-default")) {
//            return "";
//        }
//
//        return Globel.channelCode;
//    }

    /**
     * 用户退出，自动清空本地用户免登录信息
     * <p>
     * //把红点清楚
     */
    public static void OutLogin() {
        //清除首页和我的消息红点
//        if (isCleanGesturePwd) {
//            SpUtils.remove(SP.TAG_SPF_GESTURE_PWD);
//            SpUtils.setParam(SP.TAG_SAFE_STATE,false);
//        }
//        userInfo = null;
//        userIndexIndex = null;
        ClearUserInfo();
    }
    /**
     * 主动读取用户数据到内存，进行快速登录功能的实现
     * 并且会自动调用刷新用户信息
     */
    public static boolean ReadUserInfo() {
        return ReadUserInfo(App.getInstance());
    }
    /**
     * 主动读取用户数据到内存，进行快速登录功能的实现
     * 并且会自动调用刷新用户信息
     */
    public static boolean ReadUserInfo(Context context) {
        boolean result = false;
        String data = (String) SpUtils.getParam(AppConfig.TAG_USER_INFO, "");
        if (data != null) {
            try {
                data = new String(Base64.decode(data, AppConfig.TAG_USER_INFO.hashCode()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            result = true;
            userInfo = GsonUtil.getInstance().jsonToObj(data, UserInfo.class);
        }
        return result;
    }
}
