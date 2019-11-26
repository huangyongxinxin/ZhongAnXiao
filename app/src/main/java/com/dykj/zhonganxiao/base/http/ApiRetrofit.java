package com.dykj.zhonganxiao.base.http;


import com.dykj.zhonganxiao.App;
import com.dykj.zhonganxiao.base.http.convert.MyGsonConverterFactory;
import com.dykj.zhonganxiao.base.http.cookie.CookieManger;
import com.dykj.zhonganxiao.base.http.gson.DoubleDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.IntegerDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.LongDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.StringNullAdapter;
import com.dykj.zhonganxiao.util.AESUtil;
import com.dykj.zhonganxiao.util.LogUtils;
import com.dykj.zhonganxiao.util.SystemUtil;
import com.dykj.zhonganxiao.util.net.NetWorkUtils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * File descripition:   创建Retrofit
 *
 * @author gk
 * @date 2019/6/19
 */

public class ApiRetrofit {
    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private ApiServer apiServer;

    private Gson gson;
    private static final int DEFAULT_TIMEOUT = 15;


    public ApiRetrofit() {
        File cacheFile = new File(App.getInstance().getCacheDir(), "caheData");//设置缓存
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 14);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .cookieJar(new CookieManger(App.getInstance()))
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .retryOnConnectionFailure(true);//错误重联


        /**
         * 处理一些识别识别不了 ipv6手机，如小米  实现方案  将ipv6与ipv4置换位置，首先用ipv4解析
         */
        httpClientBuilder.dns(new ApiDns());

        /**
         * 添加cookie管理
         * 方法1：第三方框架
         */
        PersistentCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(App.getInstance()));
        httpClientBuilder.cookieJar(cookieJar);

        /**
         * 添加cookie管理
         * 方法2：手动封装cookie管理
         */
//        httpClientBuilder.cookieJar(new CookieManger(App.getContext()));

        /**
         * 添加请求头
         */
//        httpClientBuilder.addInterceptor(new HeadUrlInterceptor());
        httpClientBuilder.addInterceptor(new HttpParamsInterceptor());//公共请求参数

        retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.baseUrl)
//                .addConverterFactory(GsonConverterFactory.create(buildGson()))//添加json转换框架(正常转换框架)
                .addConverterFactory(MyGsonConverterFactory.create(buildGson()))//添加json自定义（根据需求，此种方法是拦截gson解析所做操作）
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();

        apiServer = retrofit.create(ApiServer.class);
    }

    /**
     * 增加后台返回""和"null"的处理,如果后台返回格式正常，此处不需要添加
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     * 4.String=>""
     *
     * @return
     */
    public Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(String.class, new StringNullAdapter())
                    .create();
        }
        return gson;
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

    /**
     * 请求访问quest    打印日志
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long startTime = System.currentTimeMillis();
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            Request build = builder.build();
            Response response = chain.proceed(build);
            if (LogUtils.mDebug) {
                printLog(build, response, startTime);
            }

            return response;
        }
    };



    //打印请求日志
    private void printLog(final Request request, final Response response, long startTime) {
        StringBuilder sb = new StringBuilder("HttpTag：");
        sb.append(" <--ServerCode："+ response.code() +"--> Method：").append(request.method());
        sb.append("\n\nHttpTag：URL：").append(request.url());
        sb.append("\n\nHttpTag：请求参数：");
        try {
//            if("POST".equals(response.request().method())){
//                if (response.request().body() instanceof FormBody) {
//                    FormBody body = (FormBody) response.request().body();
//                    for (int i = 0; i < body.size(); i++) {
//                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
//                    }
//                    sb.delete(sb.length() - 1, sb.length());
//                }
//            }else {
                sb.append(bodyToString(request.body()));
//            }

        } catch (IOException e) {
            sb.append("请求参数解析失败");
        }
        sb.append("\n\nHttpTag：返回结果：");
        try {
            //踩坑记录1：
            //这里如果直接使用response.body().string()的方式输出日志
            //会因为response.body().string()之后，response中的流会被关闭，请求返回旧结果，
            //因此，需要创建出一个新的response给应用层处理
            //但是不知道哪里的问题，现在，这里打印的是旧的请求信息，但返回结果无误。暂时这么用。
            //踩坑记录2：
            //之所以记录1中的问题，请求参数打印正常，返回结果打印却是旧的数据
            //是因为之前：request复制了一份，但response却是通过本身的Chain获取的，内存引用没有改变
            //response.peekBody()创建的新对象被重复添加到了流里面
            //导致请求2次
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            sb.append(responseBody.string());
        } catch (Exception e) {
            sb.append("返回结果解析失败");
        }
        long endTime = System.currentTimeMillis();
        sb.append("\n\nHttpTag： End：");
        sb.append(endTime - startTime + "");
        sb.append("毫秒-----");
        LogUtils.logi(sb.toString());
    }

    private String bodyToString(final RequestBody requestBody) throws IOException {
        final Buffer buffer = new Buffer();
        if (requestBody != null) {
            if (requestBody.contentLength() > 2048) {
                return "";
            }
            requestBody.writeTo(buffer);
        } else {
            return "";
        }
        return buffer.readUtf8();
    }

    /**
     * 添加  请求头
     */
    public class HeadUrlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String plac = "{'platform':'android','version':"+ SystemUtil.getAPPLocalVersionName(App.getInstance())+"}";
            String key = "www.doing.net.cn";
            String placStr = AESUtil.encrypt(plac, key);//AES加密
            placStr = placStr.replaceAll("\r|\n", "");//去除换行符
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "text/html; charset=UTF-8")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("Server", "Apache")
//                    .addHeader("Pragma", "no-cache")
//                    .addHeader("Cookie", "add cookies here")
//                    .addHeader("_identity",  cookie_value)
                    .addHeader("apihandshakekey","46580a18f5f991c452ee7dc5d236d36b0a30436d")
                    .addHeader("plac",placStr)
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 获取HTTP 添加公共参数的拦截器
     * 暂时支持get、head请求&Post put patch的表单数据请求
     *
     * @return
     */
    public class HttpParamsInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            HashMap<String, String> params = new HashMap<>();
            params.put("platform","android");
            params.put("version",SystemUtil.getAPPLocalVersionName(App.getInstance()));
//            params.put("platform","ios");
//            params.put("version","1.0");

            Gson gson=new Gson();
            String json = gson.toJson(params);
            //加密key
            String key = "www.doing.net.cn";
            String placStr = AESUtil.encrypt(json, key);//AES加密
            placStr = placStr.replaceAll("\r|\n", "");//去除换行符
            //添加请求头
            Request originalRequest = chain.request().newBuilder()
                    .addHeader("Content-Type", "text/html; charset=UTF-8")
                    .addHeader("Vary", "Accept-Encoding")
                    .addHeader("Server", "Apache")
//                    .addHeader("Pragma", "no-cache")
//                    .addHeader("Cookie", "add cookies here")
//                    .addHeader("_identity",  cookie_value)
                    .addHeader("apihandshakekey","46580a18f5f991c452ee7dc5d236d36b0a30436d")
                    .addHeader("plac",placStr)
                    .build();

            Request request = null;
            String method = originalRequest.method();//获取请求方法

            //根据不同的请求方式添加不同的参数
            if (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("HEAD")) {
                HttpUrl httpUrl = originalRequest.url().newBuilder()
//                        .addQueryParameter("version", SystemUtil.getAPPLocalVersionName(App.getInstance()))
//                        .addQueryParameter("devices", "android")
                        .build();
                request = originalRequest.newBuilder().url(httpUrl).build();
            } else {
                RequestBody originalBody = originalRequest.body();
                if (originalBody instanceof FormBody) {
                    FormBody.Builder builder = new FormBody.Builder();
                    FormBody formBody = (FormBody) originalBody;
                    for (int i = 0; i < formBody.size(); i++) {
                        builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                    FormBody newFormBody = builder
//                            .addEncoded("version", SystemUtil.getAPPLocalVersionName(App.getInstance()))
//                            .addEncoded("devices", "android")
                            .addEncoded("dyappsecret", "46580a18f5f991c452ee7dc5d236d36b0a30436d")
                            .build();
                    if (method.equalsIgnoreCase("POST")) {
                        request = originalRequest.newBuilder().post(newFormBody).build();
                    } else if (method.equalsIgnoreCase("PATCH")) {
                        request = originalRequest.newBuilder().patch(newFormBody).build();
                    } else if (method.equalsIgnoreCase("PUT")) {
                        request = originalRequest.newBuilder().put(newFormBody).build();
                    }

//                    Response response = chain.proceed(request);
//                    String content =  response.body().string();
//
//                    MediaType mediaType = response.body().contentType();
//                    return response.newBuilder()
//                            .body(ResponseBody.create(mediaType, content))
//                            .build();

                } else if (originalBody instanceof MultipartBody) {
                    MultipartBody.Builder builder = new MultipartBody.Builder();
                    MultipartBody multipartBody = (MultipartBody) originalBody;
                    List<MultipartBody.Part> parts = multipartBody.parts();
                    Buffer buffer1 = new Buffer();
                    multipartBody.writeTo(buffer1);
                    String postParams = buffer1.readUtf8();
//
                    String[] split = postParams.split("\n");
                    List<String> names = new ArrayList<>();
                    builder.setType(MultipartBody.FORM);
                    for (String s : split) {
                        if (s.contains("Content-Disposition")) {
                            names.add(s.replace("Content-Disposition: form-data; name=", "").replace("\"", ""));
                        }
                    }

                    for (int i = 0; i < parts.size(); i++) {
                        MultipartBody.Part part = parts.get(i);
                        RequestBody body1 = part.body();
                        if (body1.contentLength() < 100) {
                            Buffer buffer = new Buffer();
                            body1.writeTo(buffer);
                            String value = buffer.readUtf8();

                            if (names.size() > i) {//将文字参数放入
                                builder.addFormDataPart(names.get(i).substring(0,names.get(i).length()-1),value);
                            }
                        } else {
                            if (names.size() > i) {
                                builder.addPart(part);//将图片放入
                            }
                        }
                    }
                    MultipartBody newMpBody = builder
                            .addFormDataPart("dyappsecret","46580a18f5f991c452ee7dc5d236d36b0a30436d")//添加表单数据
                            .build();

                    if (method.equalsIgnoreCase("POST")) {
                        request = originalRequest.newBuilder().post(newMpBody).build();
                    }

                }

            }
            return chain.proceed(request);
        }
    }

    /**
     * 获得HTTP 缓存的拦截器
     *
     * @return
     */
    public class HttpCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            // 无网络时，始终使用本地Cache
            if (!NetWorkUtils.isNetWorkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (NetWorkUtils.isNetWorkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                return response.newBuilder()
                        //这里的设置的是我们的没有网络的缓存时间，想设置多少就是多少。
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
     */

    /**
     * 特殊返回内容  处理方案
     */
    public class MockInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Gson gson = new Gson();
            Response response = null;
            Response.Builder responseBuilder = new Response.Builder()
                    .code(200)
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .addHeader("content-type", "application/json");
            Request request = chain.request();
            if (request.url().toString().contains(BaseUrl.baseUrl)) { //拦截指定地址
                String responseString = "{\n" +
                        "\t\"success\": true,\n" +
                        "\t\"data\": [{\n" +
                        "\t\t\"id\": 6,\n" +
                        "\t\t\"type\": 2,\n" +
                        "\t\t\"station_id\": 1,\n" +
                        "\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\"factors\": [{\n" +
                        "\t\t\t\"id\": 11,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 6,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 2.225,\n" +
                        "\t\t\t\"value_span\": 5.0,\n" +
                        "\t\t\t\"value_standard\": 4.0,\n" +
                        "\t\t\t\"error_difference\": -1.775,\n" +
                        "\t\t\t\"error_percent\": -44.38,\n" +
                        "\t\t\t\"accept\": false\n" +
                        "\t\t}, {\n" +
                        "\t\t\t\"id\": 12,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 7,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 1.595,\n" +
                        "\t\t\t\"value_span\": 0.5,\n" +
                        "\t\t\t\"value_standard\": 1.6,\n" +
                        "\t\t\t\"error_difference\": -0.005,\n" +
                        "\t\t\t\"error_percent\": -0.31,\n" +
                        "\t\t\t\"accept\": true\n" +
                        "\t\t}, {\n" +
                        "\t\t\t\"id\": 13,\n" +
                        "\t\t\t\"history_id\": 6,\n" +
                        "\t\t\t\"station_id\": 1,\n" +
                        "\t\t\t\"factor_id\": 8,\n" +
                        "\t\t\t\"datatime\": 1559491200000,\n" +
                        "\t\t\t\"value_check\": 8.104,\n" +
                        "\t\t\t\"value_span\": 20.0,\n" +
                        "\t\t\t\"value_standard\": 8.0,\n" +
                        "\t\t\t\"error_difference\": 0.104,\n" +
                        "\t\t\t\"error_percent\": 1.3,\n" +
                        "\t\t\t\"accept\": true\n" +
                        "\t\t},null]\n" +
                        "\t}],\n" +
                        "\t\"additional_data\": {\n" +
                        "\t\t\"totalPage\": 0,\n" +
                        "\t\t\"startPage\": 1,\n" +
                        "\t\t\"limit\": 30,\n" +
                        "\t\t\"total\": 0,\n" +
                        "\t\t\"more_items_in_collection\": false\n" +
                        "\t},\n" +
                        "\t\"related_objects\": [{\n" +
                        "\t\t\"id\": 6,\n" +
                        "\t\t\"name\": \"氨氮\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"nh3n\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 7,\n" +
                        "\t\t\"name\": \"总磷\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"tp\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 8,\n" +
                        "\t\t\"name\": \"总氮\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"tn\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}, {\n" +
                        "\t\t\"id\": 9,\n" +
                        "\t\t\"name\": \"CODMn\",\n" +
                        "\t\t\"unit\": \"mg/L\",\n" +
                        "\t\t\"db_field\": \"codmn\",\n" +
                        "\t\t\"qa_ratio\": true\n" +
                        "\t}],\n" +
                        "\t\"request_time\": \"2019-06-05T16:40:14.915+08:00\"\n" +
                        "}";
                responseBuilder.body(ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()));//将数据设置到body中
                response = responseBuilder.build(); //builder模式构建response
            } else {
                response = chain.proceed(request);
            }
            return response;
        }
    }

}
