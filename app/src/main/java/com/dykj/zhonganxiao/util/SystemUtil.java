package com.dykj.zhonganxiao.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @file: SystemUtil 获取当前应用版本号与版本名
 * @author: guokang
 * @date: 2019-08-06
 */
public class SystemUtil {

    //获取apk的版本名 currentVersionName
    public static String getAPPLocalVersionName(Context ctx) {
        String name = "";
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            name = info.versionName;// 版本名
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    //获取apk的版本号 currentVersionCode
    public static int getAPPLocalVersionCode(Context ctx) {
        int name = 0 ;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            name = info.versionCode;// 版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

}
