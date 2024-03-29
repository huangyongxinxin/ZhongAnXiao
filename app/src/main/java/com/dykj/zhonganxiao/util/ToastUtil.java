package com.dykj.zhonganxiao.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.dykj.zhonganxiao.App;


/**
 * Toast统一管理类
 */
public class ToastUtil {


    private static Toast toast;

    private static Toast initToast(CharSequence message, int duration) {
        if (toast == null) {
            toast = Toast.makeText(App.getAppContext(), message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        if (TextUtils.isEmpty(message))
            return;
        initToast(message, Toast.LENGTH_SHORT).show();
    }


    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(@StringRes int strResId) {
//		Toast.makeText(context, strResId, Toast.LENGTH_SHORT).show();
        CharSequence msg = App.getAppContext().getResources().getText(strResId);
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        initToast(msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(@StringRes int strResId) {
        CharSequence msg = App.getAppContext().getResources().getText(strResId);
        if (TextUtils.isEmpty(msg))
            return;
        initToast(msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        if (TextUtils.isEmpty(message))
            return;
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, @StringRes int strResId, int duration) {
        CharSequence msg = context.getResources().getText(strResId);
        if (TextUtils.isEmpty(msg))
            return;
        initToast(msg, duration).show();
    }

//    /**
//     * 显示有image的toast
//     *
//     * @param tvStr
//     * @param imageResource
//     * @return
//     */
//    public static Toast showToastWithImg(final String tvStr, final int imageResource) {
//        if (toast2 == null) {
//            toast2 = new Toast(getAppContext());
//        }
//        View view = LayoutInflater.from(getAppContext()).inflate(R.layout.toast_custom, null);
//        TextView tv = (TextView) view.findViewById(R.id.toast_custom_tv);
//        tv.setText(TextUtils.isEmpty(tvStr) ? "" : tvStr);
//        ImageView iv = (ImageView) view.findViewById(R.id.toast_custom_iv);
//        if (imageResource > 0) {
//            iv.setVisibility(View.VISIBLE);
//            iv.setImageResource(imageResource);
//        } else {
//            iv.setVisibility(View.GONE);
//        }
//        toast2.setView(view);
//        toast2.setGravity(Gravity.CENTER, 0, 0);
//        toast2.show();
//        return toast2;
//
//    }
}
