package com.dykj.zhonganxiao.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * @file: GsonUtil
 * @author: guokang
 * @date: 2019-08-05
 */
public class GsonUtil {
    protected static GsonUtil Instance;
    private Gson gson;

    protected GsonUtil() {
        gson = new Gson();
    }

    public static GsonUtil getInstance() {
        if (Instance == null) {
            createObj();
        }
        return Instance;
    }

    protected synchronized static void createObj() {
        if (Instance == null) {
            Instance = new GsonUtil();
        }
    }

    public <T> T jsonToObj(String json, Class<T> classObj) {
        T result = null;
        try {
            result = gson.fromJson(json, classObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T jsonToObj(String json, Type type) {
        T result = null;
        try {
            result = gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ObjToJson(Object object, Class<?> classObj) {
        String result = "";
        try {
            result = gson.toJson(object, classObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ObjToJson(Object object) {
        String result = "";
        try {
            result = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ObjToJson(Object object, Type type) {
        String result = "";
        try {
            result = gson.toJson(object, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> List<T> jsonToList(String jsonString, Class<T[]> type) {
//        ArrayList<T> list = new ArrayList<T>();
//        try {
//            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
//            }.getType());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
        T[] list = gson.fromJson(jsonString, type);
        return Arrays.asList(list);
    }
}
