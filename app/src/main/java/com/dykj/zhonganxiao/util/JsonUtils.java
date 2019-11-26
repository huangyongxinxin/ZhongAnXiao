package com.dykj.zhonganxiao.util;


import android.content.res.AssetManager;
import android.text.TextUtils;

import com.dykj.zhonganxiao.App;
import com.dykj.zhonganxiao.base.http.gson.DoubleDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.IntegerDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.LongDefaultAdapter;
import com.dykj.zhonganxiao.base.http.gson.StringNullAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON解析二次封装
 */
public class JsonUtils {
    //线程安全的
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .serializeNulls() //是否序列化空值
                .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                .registerTypeAdapter(long.class, new LongDefaultAdapter())
                .registerTypeAdapter(String.class, new StringNullAdapter())
                .enableComplexMapKeySerialization()
                .create();
    }

    /**
     * @param json
     * @param clazz
     * @param <T>
     * @return
     * @brief 将JSON转为实体
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(json, clazz);
        }
        return t;
    }

    /**
     * @param obj
     * @return
     * @brief 将一个对象装为Json格式的字符串
     */

    public static String bean2json(Object obj) {
        String data = null;
        if (gson != null) {
            data = gson.toJson(obj);
        }
        return data;
    }

    public static <T> ArrayList<T> json2List(String json, Class<T> clazz) {
        Type type = new TypeToken<List<JsonObject>>() {
        }.getType();
        if (gson != null) {
            List<JsonObject> jsonObjects = gson.fromJson(json, type);

            ArrayList<T> arrayList = new ArrayList<>();
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(gson.fromJson(jsonObject, clazz));
            }
            return arrayList;
        } else {
            return null;
        }

    }


    public static <T> T fromJsonForType(String json, Type typeOfT) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }
        if (gson != null) {
            return gson.fromJson(json, typeOfT);
        } else {
            return null;
        }


    }

    public static String getJson(String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = App.getInstance().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
