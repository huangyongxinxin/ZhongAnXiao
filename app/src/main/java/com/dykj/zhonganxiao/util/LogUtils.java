package com.dykj.zhonganxiao.util;


import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 * 使用logger封装
 */
public class LogUtils {

    public static boolean mDebug;

    /**
     * 在application调用初始化
     */
    public static void logInit(final boolean debug) {
        mDebug = debug;
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(0)         // （可选）要显示的方法行数。 默认2
                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。 默认5
//                .logStrategy(customLog) //（可选）更改要打印的日志策略。 默认LogCat
                .tag("CLYLog")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return debug;
            }
        });
    }

    public static void logd(String tag, Object message) {
        Logger.d(tag, message);
    }

    public static void logd(Object message) {
        Logger.d(message);
    }

    public static void loge(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void loge(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void logi(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void logv(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void logw(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void logwtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    public static void logjson(String message) {
        Logger.json(message);
    }

    public static void logxml(String message) {
        Logger.xml(message);
    }
}
