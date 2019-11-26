package com.dykj.zhonganxiao.base.http.convert;

import com.dykj.zhonganxiao.base.http.ApiException;
import com.dykj.zhonganxiao.bean.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;


/**
 * File descripition:   重写gson 判断返回值  状态划分
 * <p>
 * 此处很重要
 * 为何这样写：因为开发中有这样的需求   当服务器返回假如0是正常 1是不正常  0我们gson 或 fastJson解析数据
 * 1我们不想解析（可能返回值出现以前是对象 数据为空变成了数组等等，于是在不改后台代码的情况下  我们前端需要处理）
 * 但是用了插件之后没有很有效的方法控制解析 所以处理方式为  当服务器返回不等于0时候  其他状态都抛出异常 然后提示
 * <p>
 * <p>
 * 此处为如果在解析这一步拦截  可采取这种方式
 *
 * @author gk
 * @date 2019/6/24
 */

final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        String str;
        BaseResponse re = gson.fromJson(response, BaseResponse.class);
        JSONObject json = null;
        //关注的重点，自定义响应码中非0的情况，一律抛出ApiException异常。
        //这样，我们就成功的将该异常交给onError()去处理了。
        try {
            json = new JSONObject(response);
            if (re.error()) {
                json.remove("data");//返回码不为1，所有的data字段都为空，所以将data删除
            }else if (!re.success()&&!re.error()){
                value.close();
                throw new ApiException(re.getErrcode(), re.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        str = json.toString();

        MediaType mediaType = value.contentType();
        Charset charset = mediaType != null ? mediaType.charset(UTF_8) : UTF_8;
        ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
        InputStreamReader reader = new InputStreamReader(bis, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);

        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }


//        Reader reader = value.charStream();
//        String response = readerToString(reader);
//
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(response);
//            if ("0".equals(jsonObject.getString("code")) || "3204".equals(jsonObject.getString("code"))) {//code为0或者为3204
//                response = jsonObject.toString();
//            }else if (!"0".equals(jsonObject.getString("code"))) {//code不为0
//                jsonObject.remove("data");
//                response = jsonObject.toString();
//            }
//        } catch (JSONException e) {
//            response = jsonObject.toString();
//        }
//        reader = new CustomResponseBodyConverter.BomAwareReader(new Buffer().readFrom(new ByteArrayInputStream(response.getBytes("UTF-8"))), Charset.forName("UTF-8"));
//        JsonReader jsonReader = gson.newJsonReader(reader);
//        try {
//            return adapter.read(jsonReader);
//        } finally {
//            value.close();
//        }
    }

    private String readerToString(Reader reader) {
        BufferedReader in = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
