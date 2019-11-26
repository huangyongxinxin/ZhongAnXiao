package com.dykj.zhonganxiao.widget.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatDialog;

import com.dykj.zhonganxiao.R;
import com.dykj.zhonganxiao.util.DisplayUtil;

import io.reactivex.annotations.NonNull;


public abstract class NormalDialog<D extends NormalDialog> extends SmartDialog<AppCompatDialog> {

    protected boolean mDarkAroundWhenShow;
    protected boolean mCancelableOnTouchOutside;
    protected boolean mCancelable;
    @DrawableRes
    protected int mWindowBackground;

    protected DialogInterface.OnShowListener mOnShowListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;
    protected DialogInterface.OnCancelListener mOnCancelListener;

    protected View mContentView;
    Activity activity;

    public NormalDialog() {
        mWindowBackground = R.drawable.smart_show_round_dialog_bg;
        mCancelable = true;
        mCancelableOnTouchOutside = true;
        mDarkAroundWhenShow = true;
    }

    public D darkAroundWhenShow(boolean dim) {
        mDarkAroundWhenShow = dim;
        applyDarkAround(mNestedDialog);
        return (D) this;
    }

    protected void applyDarkAround(AppCompatDialog dialog) {
        if (dialog == null) {
            return;
        }
        if (mDarkAroundWhenShow) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    public D windowBackground(@DrawableRes int bgRes) {
        mWindowBackground = bgRes;
        applyWindowBackground(mNestedDialog);
        return (D) this;
    }

    protected void applyWindowBackground(AppCompatDialog dialog) {
        if (dialog != null && mWindowBackground != 0) {
            dialog.getWindow().setBackgroundDrawableResource(mWindowBackground);
        }
    }

    public D cancelable(boolean b) {
        mCancelable = b;
        if (!b) {
            mCancelableOnTouchOutside = false;
        }
        applyCancelable(mNestedDialog);
        return (D) this;
    }

    protected void applyCancelable(AppCompatDialog dialog) {
        if (dialog != null) {
            dialog.setCancelable(mCancelable);
        }
    }

    public D cancelableOnTouchOutside(boolean b) {
        mCancelableOnTouchOutside = b;
        applyCancelableOnTouchOutside(mNestedDialog);
        return (D) this;
    }

    protected void applyCancelableOnTouchOutside(AppCompatDialog dialog) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(mCancelable ? mCancelableOnTouchOutside : false);
        }
    }

    public D dialogCancelListener(DialogInterface.OnCancelListener cancelListener) {
        mOnCancelListener = cancelListener;
        applyOnCancelListener(mNestedDialog);
        return (D) this;
    }

    protected void applyOnCancelListener(AppCompatDialog dialog) {
        if (dialog != null) {
            dialog.setOnCancelListener(mOnCancelListener);
        }
    }

    public D dialogShowListener(DialogInterface.OnShowListener showListener) {
        mOnShowListener = showListener;
        applyOnShowListener(mNestedDialog);
        return (D) this;
    }

    protected void applyOnShowListener(AppCompatDialog dialog) {
        if (dialog != null) {
            dialog.setOnShowListener(mOnShowListener);
        }
    }

    public D dialogDismissListener(DialogInterface.OnDismissListener dismissListener) {
        mOnDismissListener = dismissListener;
        applyOnDismissListener(mNestedDialog);
        return (D) this;
    }

    protected void applyOnDismissListener(AppCompatDialog dialog) {
        if (dialog != null) {
            dialog.setOnDismissListener(mOnDismissListener);
        }
    }

    @NonNull
    @Override
    protected AppCompatDialog createDialog(Activity activity) {
        this.activity = activity;
        AppCompatDialog dialog = new AppCompatDialog(activity, provideDialogStyle());
        mContentView = LayoutInflater.from(activity).inflate(provideContentLayout(), null);
        initView(dialog, mContentView);
        applySetting(dialog);
        ViewGroup.MarginLayoutParams rootLp = new ViewGroup.MarginLayoutParams(provideDialogWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(mContentView, rootLp);
        return dialog;
    }

    protected void applySetting(AppCompatDialog dialog) {
        applyDarkAround(dialog);
        applyWindowBackground(dialog);
        applyCancelableOnTouchOutside(dialog);
        applyCancelable(dialog);
        applyOnShowListener(dialog);
        applyOnDismissListener(dialog);
        applyOnCancelListener(dialog);
    }


    protected int provideDialogStyle() {
        return R.style.smart_show_dialog;
    }

    protected void initView(AppCompatDialog dialog, View contentView) {

    }

    protected int provideDialogWidth() {
        return Math.min(DisplayUtil.getScreenWidth(activity) - DisplayUtil.dip2px(70), DisplayUtil.dip2px(290));
    }

    @LayoutRes
    protected abstract int provideContentLayout();
}
