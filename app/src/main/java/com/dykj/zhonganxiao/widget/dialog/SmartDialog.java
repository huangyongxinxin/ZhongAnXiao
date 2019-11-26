package com.dykj.zhonganxiao.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.dykj.zhonganxiao.util.LogUtils;
import com.dykj.zhonganxiao.util.Utils;

import io.reactivex.annotations.NonNull;


public abstract class SmartDialog<NestedDialog extends Dialog> {
    protected NestedDialog mNestedDialog;

    public SmartDialog() {

    }

    public boolean showInActivity(Activity activity) {
        return show(activity, Utils.isUpdateActivityUIPermitted(activity));
    }

    public boolean showInFragment(Fragment fragment) {
        return show(fragment == null ? null : fragment.getActivity(), Utils.isCanShowDialogInFragment(fragment));
    }


    private boolean show(Activity activity, boolean canUpdateUI) {
        if (!canUpdateUI) {
            LogUtils.logi("do nothing but recycle when conditions not available!");
            mNestedDialog = null;
            return false;
        }

        if (mNestedDialog == null) {
            mNestedDialog = Utils.requireNonNull(createDialog(activity), "the method createDialog must return a non-null dialog!");
            LogUtils.logi("create a new dialog:\n " + mNestedDialog);
        } else {
            resetDialogWhenShowAgain(mNestedDialog);
            LogUtils.logi("reuse dialog:\n " + mNestedDialog);
        }

        if (mNestedDialog != null) {
            try {
                mNestedDialog.show();
                return true;
            } catch (WindowManager.BadTokenException e) {
                LogUtils.logi("BadToken has happened when show dialog: \n" + mNestedDialog.getClass().getSimpleName());
                return false;
            }
        }

        return false;
    }

    @NonNull
    protected abstract NestedDialog createDialog(Activity activity);

    protected void resetDialogWhenShowAgain(NestedDialog dialog) {

    }

    public boolean dismiss() {
        if (mNestedDialog == null || !mNestedDialog.isShowing()) {
            LogUtils.logi("do nothing but recycle when conditions not available!");
            return false;
        }

        try {
            mNestedDialog.dismiss();
            return true;
        } catch (IllegalStateException e) {
             LogUtils.logi("IllegalStateException has happened when show dialog:\n" + mNestedDialog);
            return false;
        }
    }

    public boolean isShowing() {
        return mNestedDialog != null && mNestedDialog.isShowing();
    }
}
