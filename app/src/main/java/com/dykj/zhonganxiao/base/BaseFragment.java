package com.dykj.zhonganxiao.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;


import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dykj.zhonganxiao.App;
import com.dykj.zhonganxiao.base.http.BaseUrl;
import com.dykj.zhonganxiao.base.mvp.BasePresenter;
import com.dykj.zhonganxiao.base.mvp.BaseView;
import com.dykj.zhonganxiao.bean.BaseResponse;
import com.dykj.zhonganxiao.constants.RefreshEvent;
import com.dykj.zhonganxiao.util.ToastUtil;
import com.dykj.zhonganxiao.util.rxbus.RxBus;
import com.dykj.zhonganxiao.util.rxbus.Subscribe;
import com.dykj.zhonganxiao.util.rxbus.ThreadMode;
import com.dykj.zhonganxiao.widget.dialog.LoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements ISupportFragment, BaseView {
    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected BaseActivity _mActivity;
    protected P mPresenter;
    protected View rootView;

    protected abstract P createPresenter();

    private Unbinder unbinder;

    /**
     * 视图是否已经初初始化
     */
    //是否初始化完成
    public boolean isInit = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null != getArguments()) {
            getBundleExtras(getArguments());
        }
        /**
         * 优先调用读取view的方法，当view为空的时候getlayoutId返回的id才有效
         * 当嵌套在viewpager的时候会多次获取view
         */
        if (rootView == null)
            rootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, rootView);

        mPresenter = createPresenter();
        /**初始化的时候去加载数据**/
        if (!isInit) {
            if (getUserVisibleHint()) {
                initView();
                isInit = true;
            }
        }
        //rxbus注册
        if (!RxBus.getDefault().isRegistered(this)) {
            RxBus.getDefault().register(this);
        }
//        if (isRegisteredEventBus()) {
//            EventBusUtils.register(this);
//        }
        return rootView;
    }



    /**
     * 获取当前faragment的view
     *
     * @return
     */
    @Override
    public View getView() {
        return rootView;
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
        mMiddleLoadingDialog.showInActivity(getActivity());
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
        mSmallLoadingDialog.showInActivity(getActivity());
    }

    public void dismissSmallLoading() {
        if (mSmallLoadingDialog == null) return;
        mSmallLoadingDialog.dismiss();
    }

    /**
     * 获取bundle信息
     *
     * @param bundle
     */
    protected abstract void getBundleExtras(Bundle bundle);


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    public void showError(String msg) {
        ToastUtil.showShort(msg);
    }

    @Override
    public void onErrorCode(BaseResponse data) {
        if (data.errcode.equals(BaseUrl.SPECIAL)
                || data.errcode.equals(BaseUrl.LOGIN_OVERDUE)) {//特殊状态
            //处理些后续逻辑   如果某个页面不想实现  子类重写这个方法  将super去掉  自定义方法
//            App.put();
//            startActivity(LoginActivity.class);
            dialog(data.message);
        }
    }

    //共用提示dialog
    public void dialog(String msg){
//        AlertDialog builder = new AlertDialog.Builder(_mActivity)
//                .setMessage(msg)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        RxBus.getDefault().post(new RefreshEvent(RefreshEvent.LOGOUT,null));
//                        App.getInstance().outLogin();
//                        startActivity(LoginActivity.class);
//                        _mActivity.finish();
//
//
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
    public SupportFragmentDelegate getSupportDelegate() {
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        _mActivity = (BaseActivity) mDelegate.getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
        isInit = false;
        if (unbinder != null)
            unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mMiddleLoadingDialog != null) {
            mMiddleLoadingDialog.dismiss();
        }
        if (mSmallLoadingDialog != null) {
            mSmallLoadingDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (RxBus.getDefault().isRegistered(this))
//        RxBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
        if (RxBus.getDefault().isRegistered(this))
            RxBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(RefreshEvent event) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //防止数据预加载, 只预加载View，不预加载数据
        if (isVisibleToUser && isVisible() && !isInit) {
            initView();
            isInit = true;
        }
        mDelegate.setUserVisibleHint(isVisibleToUser);
    }


    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     *
     */
    @Deprecated
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
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
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * <p>
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * <p>
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    /****************************************以下为可选方法(Optional methods)******************************************************/

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
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

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
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


    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the child fragment.
     */
    public void popChild() {
        mDelegate.popChild();
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

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
    }

    public void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
        mDelegate.popToChild(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
    }

    /**
     * 得到位于栈顶Fragment
     */
    public ISupportFragment getTopFragment() {
        return SupportHelper.getTopFragment(getFragmentManager());
    }

    public ISupportFragment getTopChildFragment() {
        return SupportHelper.getTopFragment(getChildFragmentManager());
    }

    /**
     * @return 位于当前Fragment的前一个Fragment
     */
    public ISupportFragment getPreFragment() {
        return SupportHelper.getPreFragment(this);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getFragmentManager(), fragmentClass);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
