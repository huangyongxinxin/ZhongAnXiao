package com.dykj.zhonganxiao.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.dykj.zhonganxiao.R;
import com.dykj.zhonganxiao.base.http.HttpsTrustManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @file: AppUpdateManager  检查版本更新
 * @author: guokang
 * @date: 2019-10-12
 */
public class AppUpdateManager {
    /* 文字参数 */
    private String soft_update_no = "已经是最新版本";
    private String soft_update_title = "软件更新";
    private String soft_update_info = "检测到新版本，立即更新吗?";
    private String soft_update_updatebtn = "更新";
    private String soft_update_later = "稍后更新";
    private String soft_updating = "软件正在下载中，请耐心等待";
    private String soft_update_cancel = "取消";
    private String soft_update_erro = "网络错误";

    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 保存解析的XML信息 */
//    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;

    private Context mContext;
    /* 下载地址 */
    private String DownloadUrl;
    /* 是否提示信息 */
    private Boolean isToas = true;
    /* 是否强制更新1是 0否 */
    private int is_require;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public AppUpdateManager(Context context, String downloadUrl, Boolean isToas, int is_require) {
        this.mContext = context;
        this.DownloadUrl = downloadUrl;
        this.isToas = isToas;
        this.is_require = is_require;

    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        // 获取当前软件版本
        final int versionCode = getVersionCode(mContext);
        // 显示提示对话框
        showNoticeDialog();

//            OkGo.<String>get(DownloadUrl)
//                    .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
//                    .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
//                    .cacheMode(CacheMode.DEFAULT)    // 缓存模式，详细请看缓存介绍                        .headers(HttpHeaders.HEAD_KEY_USER_AGENT, "abcd")//
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                            // s 即为所需要的结果
//                            InputStream inStream = new ByteArrayInputStream(response.body().toString().getBytes());
//                            // 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
//                            ParseXmlService service = new ParseXmlService();
//                            try {
//                                mHashMap = service.parseXml(inStream);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            if (null != mHashMap) {
//                                int serviceCode = Integer.valueOf(mHashMap.get("version"));
//                                // 版本判断
//                                if (serviceCode > versionCode) {
//                                    // 显示提示对话框
//                                    showNoticeDialog();
//                                } else {
//                                    if (isToas) {
//                                        Toast.makeText(mContext, soft_update_no, Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }
//
//                        }
//                    });


    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    @SuppressLint("NewApi")
    private void showNoticeDialog() {
        int Theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
        int version = Build.VERSION.SDK_INT;
        if (version > 21) {
            Theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
        } else {
            Theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
        }

        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, Theme);
        builder.setTitle(soft_update_title);
//        String msg = mHashMap.get("content");
        String msg = soft_update_info;
        if (msg.equals("")) {
            msg = soft_update_info;
        }
        builder.setMessage(msg);

        // 更新
        builder.setPositiveButton(soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
                // 下载文件
//                downloadApk();
            }
        });
        // 稍后更新
        builder.setNegativeButton(soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });




        Dialog noticeDialog = builder.create();
        noticeDialog.show();

        if (is_require == 1){//强制更新
            noticeDialog.setCancelable(false);
            noticeDialog.setCanceledOnTouchOutside(false);
            ((AlertDialog) noticeDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        }

//        ((AlertDialog) noticeDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(14);
        ((AlertDialog) noticeDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_333333));
//        ((AlertDialog) noticeDialog).getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(14);
        ((AlertDialog) noticeDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.color_333333));

    }


    /**
     * 显示软件下载对话框
     */
    @SuppressLint("NewApi")
    private void showDownloadDialog() {
        int Theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
        int version = Build.VERSION.SDK_INT;
        if (version > 21) {
            Theme = android.R.style.Theme_DeviceDefault_Light_Dialog;
        } else {
            Theme = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;//AlertDialog.THEME_HOLO_LIGHT
        }

        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, Theme);
        builder.setTitle(soft_updating);
        // 给下载对话框增加进度条
        FrameLayout framelayout = new FrameLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT //创建保存布局参数的对象
        );
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;//设置居中显示
        /*mProgress条形进度条初始化*/
        mProgress = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);//水平条形
        mProgress.setMax(100);
        mProgress.setPadding(30, 60, 30, 60);
        mProgress.setLayoutParams(params);//设置布局参数
        framelayout.addView(mProgress);//将元素添加到布局管理器中

        builder.setCancelable(false);
            builder.setView(framelayout);
        // 取消更新
        builder.setNegativeButton(soft_update_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });

        mDownloadDialog = builder.create();
        mDownloadDialog.show();

        if (is_require == 1){//强制更新
            mDownloadDialog.setCancelable(false);
            mDownloadDialog.setCanceledOnTouchOutside(false);
            ((AlertDialog) mDownloadDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        }

        // 下载文件
        downloadApk();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                HttpsTrustManager.allowAllSSL();//支持https

                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(DownloadUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, "yidianniu");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
        }
    }


    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, "yidianniu");
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent();
        Uri data;
        // 判断版本大于等于7.0
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
////            data = FileProvider.getUriForFile(mContext, "com.dykj.huaxin", apkfile);
//            data =   FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID+".provider",apkfile);
//
//        } else {
        data = Uri.parse("file://" + apkfile.toString());
//        }
        i.setDataAndType(data, "application/vnd.android.package-archive");
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        CtrAppImpl.getContext().startActivity(intent);
//        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        i.setDataAndType(data, "application/vnd.android.package-archive");
        mContext.startActivity(i);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//        mContext.startActivity(i);
    }

    /**
     * 解析XML基类
     */
    public class ParseXmlService {
        public HashMap<String, String> parseXml(InputStream inStream) throws Exception {
            HashMap<String, String> hashMap = new HashMap<String, String>();

            // 实例化一个文档构建器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // 通过文档构建器工厂获取一个文档构建器
            DocumentBuilder builder = factory.newDocumentBuilder();
            // 通过文档通过文档构建器构建一个文档实例
            Document document = builder.parse(inStream);
            // 获取XML文件根节点
            Element root = document.getDocumentElement();
            // 获得所有子节点
            NodeList childNodes = root.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                // 遍历子节点
                Node childNode = (Node) childNodes.item(j);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    // 版本号
                    if ("version".equals(childElement.getNodeName())) {
                        hashMap.put("version", childElement.getFirstChild().getNodeValue());
                    }
                    // 软件名称
                    else if (("name".equals(childElement.getNodeName()))) {
                        hashMap.put("name", childElement.getFirstChild().getNodeValue());
                    }
                    // 下载地址
                    else if (("url".equals(childElement.getNodeName()))) {
                        hashMap.put("url", childElement.getFirstChild().getNodeValue());
                    }
                    // 内容
                    else if (("content".equals(childElement.getNodeName()))) {
                        hashMap.put("content", childElement.getFirstChild().getNodeValue());
                    }
                }
            }
            return hashMap;
        }
    }
}
