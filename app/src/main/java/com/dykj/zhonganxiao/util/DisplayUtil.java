package com.dykj.zhonganxiao.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import com.dykj.zhonganxiao.App;


/**
 * Created by zachary
 */

public class DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxValue, App.getAppResources().getDisplayMetrics());
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = App.getAppResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = App.getAppResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = App.getAppResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取屏幕信息，
     *
     * @return 数组0为宽，数组1为高
     */
    public static int[] getDisplayInfo(Context context) {
        int[] info = new int[2];
        if (context != null) {
            DisplayMetrics dm = new DisplayMetrics();//创建屏幕显示度量对象
            ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);//获取屏幕管理器--->获取默认的显示对象--->获取度量对象
            int w = dm.widthPixels;//宽
            int h = dm.heightPixels;//高
            info[0] = w;
            info[1] = h;
        }
        return info;
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
