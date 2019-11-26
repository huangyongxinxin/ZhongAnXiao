package com.dykj.zhonganxiao.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.text.TextUtils;


import com.dykj.zhonganxiao.App;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 * 设置推送用户别名
 */

public class SetTagAliasUtil {
    private static final int MSG_SET_ALIAS = 10101;
    private static final int MSG_SET_TAGS = 10102;

    private static class SingleInstance {
        public static SetTagAliasUtil INSTANCE = new SetTagAliasUtil();
    }

    public static SetTagAliasUtil getInstance() {
        return SingleInstance.INSTANCE;
    }

    //alias别名  登录后别名UserCode  没登录别名为随机码
    //tag组包含 all全部 ，online 已登录组 , offline未登录组， newbie新手

    public void setTagAndAlias() {
        String alias = "";
        String tag = "all";
        if(App.getInstance().isLogin()){
            alias = App.getInstance().getToken();
            tag += ",online";
//            if (!TextUtils.isEmpty(UserComm.userInfo.getIs_order())){
//                if (UserComm.userInfo.getIs_order().equals(Type.RIGHT_TYPE_NO)){
//                    tag += ",newbie";
//                }else{
//                    tag += "";
//                }
//            }


        }else{
//            alias = JPushInterface.getUdid(App.getInstance());
            tag += ",offline";
        }

        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
        setTag(tag);
    }

    public void setTag(String tag){

        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    LogUtils.logd("Set alias in handler.");
//                    JPushInterface.setAliasAndTags(App.getAppContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    LogUtils.logd("Set tags in handler.");
//                    JPushInterface.setAliasAndTags(App.getAppContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
                    LogUtils.logd("Unhandled msg - " + msg.what);
            }
        }
    };

//    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set alias success";
//                    Log.lifecycle(logs+" "+alias);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.lifecycle(logs);
//                    if (AppUtil.isNetConnected(App.getAppContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
//                    } else {
//                        Log.lifecycle("No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.lifecycle(logs);
//            }
//
//        }
//
//    };

//    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {
//
//        @Override
//        public void gotResult(int code, String alias, Set<String> tags) {
//            String logs;
//            switch (code) {
//                case 0:
//                    logs = "Set tag and alias success";
//                    Log.lifecycle(logs+" "+tags);
//                    break;
//
//                case 6002:
//                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
//                    Log.lifecycle(logs);
//                    if (AppUtil.isNetConnected(App.getAppContext())) {
//                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
//                    } else {
//                        Log.lifecycle("No network");
//                    }
//                    break;
//
//                default:
//                    logs = "Failed with errorCode = " + code;
//                    Log.lifecycle(logs);
//            }
//        }
//
//    };

}
