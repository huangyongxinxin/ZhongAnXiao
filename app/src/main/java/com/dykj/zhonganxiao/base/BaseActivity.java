package com.dykj.zhonganxiao.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.dykj.zhonganxiao.App;
import com.dykj.zhonganxiao.R;
import com.dykj.zhonganxiao.base.http.BaseUrl;
import com.dykj.zhonganxiao.base.mvp.BasePresenter;
import com.dykj.zhonganxiao.base.mvp.BaseView;
import com.dykj.zhonganxiao.bean.BaseResponse;
import com.dykj.zhonganxiao.constants.ErrorNetUIEnum;
import com.dykj.zhonganxiao.constants.RefreshEvent;
import com.dykj.zhonganxiao.util.LogUtils;
import com.dykj.zhonganxiao.util.ToastUtil;
import com.dykj.zhonganxiao.util.net.NetType;
import com.dykj.zhonganxiao.util.net.NetworkListener;
import com.dykj.zhonganxiao.util.net.NetworkManager;
import com.dykj.zhonganxiao.util.rxbus.RxBus;
import com.dykj.zhonganxiao.util.rxbus.Subscribe;
import com.dykj.zhonganxiao.util.rxbus.ThreadMode;
import com.dykj.zhonganxiao.util.statusBar.StatusBarUtils;
import com.dykj.zhonganxiao.widget.dialog.LoadingDialog;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public abstract class BaseActivity<P extends BasePresenter> extends SwipeBackActivity implements ISupportActivity, BaseView {
    private SwipeBackLayout mSwipeBackLayout;
    final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);
    protected P mPresenter;

    private CompositeDisposable mDisposables = new CompositeDisposable();

    /**
     * 网络请求失败的提示
     */
    private View mErrorNet;
    public boolean canSwipeBack = true;
    private Unbinder unbinder;
    protected ImmersionBar mImmersionBar;
    public Context mContext;
    public boolean isActive = true;//是否在前台

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏, 禁用横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDelegate.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //注册广播
        NetworkManager.getDefault().registerObserver(this);

        unbinder = ButterKnife.bind(this);
        mImmersionBar = ImmersionBar.with(this);
        if (isImmersionBarEnabled())
            initImmersionBar();
        mContext = this;
        mPresenter = createPresenter();
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            getBundleExtras(bundle);
        }
        //rxbus注册
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
        bindView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
//        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
//        Bugtags.onPause(this);
    }

    protected void initImmersionBar() {
//        mImmersionBar.statusBarDarkFont(true, 0.2f);//如果无法改变白色状态栏字体，默认给状态栏加上0.2透明度
        mImmersionBar.keyboardEnable(true);
        if (isSetStatusBarDarkFont())
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        mImmersionBar.init();
    }

    public void subscribe(Disposable disposable) {
        mDisposables.add(disposable);
    }

    public void unsubscribe() {
        if (mDisposables != null && !mDisposables.isDisposed()) {
            mDisposables.dispose();
            mDisposables.clear();
        }
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected boolean isSetStatusBarDarkFont() {

        return true;
    }

    protected abstract void getBundleExtras(Bundle bundle);

    //view 绑定事件
    protected abstract void bindView();


    public boolean isCanSwipeBack() {
        return canSwipeBack;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mSwipeBackLayout = getSwipeBackLayout();
//        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEdgeSize(80);
        mSwipeBackLayout.setEnableGesture(isCanSwipeBack());
    }


    protected abstract P createPresenter();

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(msg);
    }

    /**
     * 返回所有状态  除去指定的值  可设置所有（根据需求）
     *
     * @param data
     */
    @Override
    public void onErrorCode(BaseResponse data) {
        if (data.errcode.equals(BaseUrl.SPECIAL)
                || data.errcode.equals(BaseUrl.LOGIN_OVERDUE)) {//特殊状态
            //处理些后续逻辑   如果某个页面不想实现  子类重写这个方法  将super去掉  自定义方法

            RxBus.getDefault().post(new RefreshEvent(RefreshEvent.LOGOUT,null));
            App.getInstance().outLogin();
            dialog(data.message);
        }
    }

    //共用提示dialog
    public void dialog(String msg){
//        AlertDialog builder = new AlertDialog.Builder(this)
//                .setMessage(msg)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("classStr",this.getClass().getSimpleName());
//                        startActivity(LoginActivity.class);
//                        dialog.dismiss();
////                        finish();
//                    }
//                })
////                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                    public void onClick(DialogInterface dialog, int id) {
////
////                    }
////                })
//                .show();
//        builder.setCancelable(false);
//        builder.setCanceledOnTouchOutside(false);
//        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(14);
//        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.color_e60d0d));
//        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
//        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.color_e60d0d));

    }


    @Override
    public void showLoading() {
        showMiddleLoading();
    }

    @Override
    public void hideLoading() {
        dismissMiddleLoading();
    }

    //带文字的Loading
    private LoadingDialog mMiddleLoadingDialog;

    public void showMiddleLoading() {
        if (mMiddleLoadingDialog == null) {
            mMiddleLoadingDialog = new LoadingDialog()
                    .middle()
                    .withMsg(true)
                    .message("正在加载");
        }
        mMiddleLoadingDialog.showInActivity(this);
    }

    public void dismissMiddleLoading() {
        if (mMiddleLoadingDialog == null) return;
        mMiddleLoadingDialog.dismiss();
    }

    //不带文字的Loading
    private LoadingDialog mSmallLoadingDialog;

    public void showSmallLoading() {
        if (mSmallLoadingDialog == null) {
            mSmallLoadingDialog = new LoadingDialog()
                    .small();
        }
        mSmallLoadingDialog.showInActivity(this);
    }

    public void dismissSmallLoading() {
        if (mSmallLoadingDialog == null) return;
        mSmallLoadingDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDelegate.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        if (mMiddleLoadingDialog != null) {
            mMiddleLoadingDialog.dismiss();
        }
        if (mSmallLoadingDialog != null) {
            mSmallLoadingDialog.dismiss();
        }
        unsubscribe();

        RxBus.getDefault().unregister(this);

        //注销目标广播
        NetworkManager.getDefault().unRegisterObserver(this);
        //注销所有广播
//        NetworkManager.getDefault().unRegisterAllObserver();
    }


    //网络监听
    @NetworkListener(type = NetType.WIFI)
    public void network(@NetType String type){
        switch (type){
            case NetType.AUTO:
//                WindowUtils.hidePopupWindow();
//                Log.i(Constants.TAG,"AUTO");
                break;
            case NetType.CMNET:
//                WindowUtils.hidePopupWindow();
//                Log.i(Constants.TAG,"CMNET");
                break;
            case NetType.CMWAP:
//                WindowUtils.hidePopupWindow();
//                Log.i(Constants.TAG,"CMWAP");
                break;
            case NetType.WIFI:
//                WindowUtils.hidePopupWindow();
//                Log.i(Constants.TAG,"WIFI");
                break;
            case NetType.NONE:
//                WindowUtils.showPopupWindow(this);
//                Log.i(Constants.TAG,"NONE");
                break;
        }
    }



    //网络监听
    @NetworkListener(type = NetType.WIFI)
    public void networkListen(@NetType String type){
        switch (type){
            case NetType.AUTO:
//                Log.i(Constants.TAG,"AUTO*");
                break;
            case NetType.CMNET:
//                Log.i(Constants.TAG,"CMNET*");
                break;
            case NetType.CMWAP:
//                Log.i(Constants.TAG,"CMWAP*");
                break;
            case NetType.WIFI:
//                Log.i(Constants.TAG,"WIFI*");
                break;
            case NetType.NONE:
//                Log.i(Constants.TAG,"NONE*");
                break;
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(RefreshEvent event) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;

    }




    /**
     * 网络失败点重试后进入的回调
     */
    protected void onResetNet() {
    }

    protected ErrorNetUIEnum initConfigErrorNet() {
        return ErrorNetUIEnum.TopBar;
    }

    public void showErrorNet() {
//        if (mErrorNet == null) {
//            mErrorNet = View.inflate(this, R.layout.layout_error_network, null);
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//            //5.0以下和60以上 addContextView 从屏幕的起点开始算 ，当5.0到6.0之间(7.0也是)从状态栏以下开始计算
//            int top = Build.VERSION_CODES.M > Build.VERSION.SDK_INT && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP ? 0 : StatusBarUtils.getStatusBarHeight(this);
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) top = 0;
//            LogUtils.logi("add Top height:" + top);
//            switch (initConfigErrorNet()) {
//                case TopBar:
//                    params.setMargins(0, top + (int) getResources().getDimension(R.dimen.dp_45), 0, 0);
//                    break;
//                case TopBarTab:
//                    params.setMargins(0, top + (int) getResources().getDimension(R.dimen.dp_45) + (int) getResources().getDimension(R.dimen.dp_41), 0, 0);
//                    break;
//                case TopBarBottomButton:
//                    params.setMargins(0, top + (int) getResources().getDimension(R.dimen.dp_45), 0, (int) getResources().getDimension(R.dimen.dp_47));
//                    break;
//            }
//            mErrorNet.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mErrorNet.setVisibility(View.GONE);
//                    onResetNet();
//                }
//            });
//            addContentView(mErrorNet, params);
//        }
//        mErrorNet.setVisibility(View.VISIBLE);
    }


    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }
    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDelegate.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (App.getInstance().isAppRunningBackground()) {//程序进入后台
            isActive = false;
        }
//        RxBus.getDefault().unregister(this);
    }


    /**
     * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
     */
    @Override
    final public void onBackPressed() {
        mDelegate.onBackPressed();
    }

    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    @Override
    public void onBackPressedSupport() {
        mDelegate.onBackPressedSupport();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * Set all fragments animation.
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * Set all fragments animation.
     * 构建Fragment转场动画
     * <p/>
     * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
     * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
     *
     * @return FragmentAnimator对象
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnimation) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnimation);
    }

    /**
     * 加载多个同级根Fragment,类似Wechat, QQ主页的场景
     */
    public void loadMultipleRootFragment(int containerId, int showPosition, ISupportFragment... toFragments) {
        mDelegate.loadMultipleRootFragment(containerId, showPosition, toFragments);
    }

    /**
     * show一个Fragment,hide其他同栈所有Fragment
     * 使用该方法时，要确保同级栈内无多余的Fragment,(只有通过loadMultipleRootFragment()载入的Fragment)
     * <p>
     * 建议使用更明确的{@link #showHideFragment(ISupportFragment, ISupportFragment)}
     *
     * @param showFragment 需要show的Fragment
     */
    public void showHideFragment(ISupportFragment showFragment) {
        mDelegate.showHideFragment(showFragment);
    }

    /**
     * show一个Fragment,hide一个Fragment ; 主要用于类似微信主页那种 切换tab的情况
     */
    public void showHideFragment(ISupportFragment showFragment, ISupportFragment hideFragment) {
        mDelegate.showHideFragment(showFragment, hideFragment);
    }

    /**
     */
    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     *
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * Start the target Fragment and pop itself
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    /**
     */
    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    /**
     * Pop the fragment.
     */
    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
     * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 当Fragment根布局 没有 设定background属性时,
     * Fragmentation默认使用Theme的android:windowbackground作为Fragment的背景,
     * 可以通过该方法改变其内所有Fragment的默认背景。
     */
    public void setDefaultFragmentBackground(@DrawableRes int backgroundRes) {
        mDelegate.setDefaultFragmentBackground(backgroundRes);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getSupportFragmentManager());
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
    }

}
