package com.dykj.zhonganxiao;

import android.content.Context;
import android.text.TextUtils;
import android.view.WindowManager;

import com.dykj.zhonganxiao.base.BaseApp;
import com.dykj.zhonganxiao.constants.AppConfig;
import com.dykj.zhonganxiao.constants.UserComm;
import com.dykj.zhonganxiao.util.CrashHandler;
import com.dykj.zhonganxiao.util.LogUtils;
import com.dykj.zhonganxiao.util.SpUtils;
import com.dykj.zhonganxiao.util.net.NetworkManager;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

public class App extends BaseApp {
    private static App mApp;

    public static App getInstance() {
        return mApp;
    }

    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
    public WindowManager.LayoutParams getMywmParams(){
        return wmParams;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        CrashHandler.getInstance().init(this);//初始化全局异常管理
        LogUtils.logInit(BuildConfig.DEBUG);//初始化打印工具
        SpUtils.initSP(this);//初始化保存工具
//        ZXingLibrary.initDisplayOpinion(this);//初始化扫码
        UserComm.ReadUserInfo();
        //初始化美洽
//        if (BuildConfig.DEBUG){
//            MQManager.setDebugMode(true);
//        }

        //App Key : ce4bd83904ed0ca9fdfe2c31dc197756
        //Secret Key : $2a$12$hImrHOhHPI7LvBFoOtIzougGMWIqNHctv3M5D1pL4xCtkE6mVrKdi
        //初始化美洽
//        MQConfig.init(this, "ce4bd83904ed0ca9fdfe2c31dc197756", new OnInitCallback() {
//            @Override
//            public void onSuccess(String clientId) {
////                Toast.makeText(MainActivity.this, "init success", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
////                Toast.makeText(MainActivity.this, "int failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//        MQConfig.isShowClientAvatar = true;//是否显示用户头像


        NetworkManager.getDefault().init(this);//初始化网络链接检测a21fbd09e31fb740871472a184c454a3

//        BugtagsOptions options = new BugtagsOptions.Builder().
//                trackingLocation(true).       //是否获取位置，默认 true
//                trackingCrashLog(true).       //是否收集闪退，默认 true
//                trackingConsoleLog(true).     //是否收集控制台日志，默认 true
//                trackingUserSteps(true).      //是否跟踪用户操作步骤，默认 true
//                crashWithScreenshot(true).    //收集闪退是否附带截图，默认 true
//                trackingAnr(true).              //收集 ANR，默认 false
//                trackingBackgroundCrash(true).  //收集 独立进程 crash，默认 false
//                versionName(BuildConfig.VERSION_NAME).         //自定义版本名称，默认 app versionName
//                versionCode(BuildConfig.VERSION_CODE).              //自定义版本号，默认 app versionCode
//                trackingNetworkURLFilter("(.*)").//自定义网络请求跟踪的 url 规则，默认 null
//                enableUserSignIn(true).            //是否允许显示用户登录按钮，默认 true
//                startAsync(false).    //设置 为 true 则 SDK 会在异步线程初始化，节省主线程时间，默认 false
//                startCallback(null).            //初始化成功回调，默认 null
//                remoteConfigDataMode(Bugtags.BTGDataModeProduction).//设置远程配置数据模式，默认Bugtags.BTGDataModeProduction 参见[文档](https://docs.bugtags.cn/zh/remoteconfig/android/index.html)
//                remoteConfigCallback(null).//设置远程配置的回调函数，详见[文档](https://docs.bugtags.cn/zh/remoteconfig/android/index.html)
//                enableCapturePlus(false).        //是否开启手动截屏监控，默认 false，参见[文档](https://docs.bugtags.cn/zh/faq/android/capture-plus.html)
////                extraOptions(key,value).                //设置 log 记录的行数，详见下文
//                build();

//        extraOptions(Bugtags.BTGConsoleLogCapacityKey, 500).//设置控制台日志行数的控制 value > 0，默认500
//                extraOptions(Bugtags.BTGBugtagsLogCapacityKey, 1000).//控制 Bugtags log 行数的控制 value > 0，默认1000
//                extraOptions(Bugtags.BTGUserStepLogCapacityKey, 1000).//控制用户操作步骤行数的控制 value > 0，默认1000
//                extraOptions(Bugtags.BTGNetworkLogCapacityKey, 20).//控制网络请求数据行数的控制 value > 0，默认20
//        Bugtags.addUserStep("custom step");
//        if (BuildConfig.DEBUG) {
//            //BTGInvocationEventNone    // 静默模式，只收集 Crash 信息（如果允许，默认为允许）
//            //BTGInvocationEventShake   // 通过摇一摇呼出 Bugtags
//            //BTGInvocationEventBubble  // 通过悬浮小球呼出 Bugtags
//            Bugtags.start("a21fbd09e31fb740871472a184c454a3", this, Bugtags.BTGInvocationEventBubble, options);
//        }

    }

    //用户token
    public String getToken(){
        String token = (String) SpUtils.getParam(AppConfig.TAG_TOKEN,"");
        return token;
    }
    //判断是否登录
   public boolean isLogin(){
      String token = (String) SpUtils.getParam(AppConfig.TAG_TOKEN,"");
      boolean flag = false;
       if(!TextUtils.isEmpty(token)){
           flag = true;
       }else {
           flag = false;
       }
        return flag;
   }
    //退出登录清空token
    public void outLogin(){
        SpUtils.setParam(AppConfig.TAG_TOKEN,"");
        UserComm.OutLogin();
    }

//    static {
//        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多";
//        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
//        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
//        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
//        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
//        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
//        ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据";
//    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setDragRate(0.7f);//修改阻尼效果（0 - 1），越小阻尼越大，默认0.5
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
//                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
                return new MaterialHeader(context);
//                return new StoreHouseHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter

                layout.setEnableNestedScroll(false);//是否启用嵌套滚动

                ClassicsFooter classicsFooter = new ClassicsFooter(context);
                classicsFooter.setFinishDuration(0);
                classicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据";
                return classicsFooter;
                //setSpinnerStyle设置变形样式，Scale为拉伸，Translate平行移动
                //FixedBehind固定在背后，FixedFront固定在前面，MatchLayout填满布局
//                return new BallPulseFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
//                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

}
