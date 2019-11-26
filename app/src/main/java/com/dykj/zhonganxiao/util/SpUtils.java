package com.dykj.zhonganxiao.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * 对SharedPreference文件中的各种类型的数据进行存取操作
 */
public class SpUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "yb_date";

    private static String CONFIG = "config";
    static SharedPreferences sp;

    /**
     * 在application 里面调用 防止内存泄漏
     *
     * @param context
     */
    public static void initSP(Context context) {
//        sp = context.getSharedPreferences(FILE_NAME + context.getPackageName(), Context.MODE_PRIVATE);
        sp = context.getSharedPreferences(CONFIG,
                Context.MODE_PRIVATE);
    }



    /**
     * // 保存公告List集合
     */
    public static void saveList(String tag, List<String> noticeItemList) {
        String json = new Gson().toJson(noticeItemList);
        setParam(tag,json);
//        putString("notice_item", json);
    }

    /**
     *获取公告List集合
     */
    public static List<String> getList(String tag) {
        List<String> listTemp = new ArrayList<>();
        try {
            String json = (String) getParam(tag,"");
            if (Utils.isEmpty(json)){
                return listTemp;
            }
            listTemp = new Gson().fromJson(json, new TypeToken<ArrayList<String>>() {}.getType());
        } catch (Exception e) {
//            LogUtil.e(e);
        }
        return listTemp;
    }



    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object) {
        if (sp == null) return;
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, GsonUtil.getInstance().ObjToJson(object, object.getClass()));
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject) {
        if (sp == null) return defaultObject;
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return defaultObject;
    }

    public static <T> T getParamByObj(String key, Class<T> Class) {
        if (sp == null) return (T) Class;

        String data = sp.getString(key, "@null");
        T result;
        if (data.equals("@null")) {
            return null;
        } else {
            result = (T) GsonUtil.getInstance().jsonToObj(data, Class);
            return result;
        }

    }
    /**
     * 移除某指定的key
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences.Editor e = sp.edit();
        e.remove(key);
        e.commit();
        e.apply();
    }


}