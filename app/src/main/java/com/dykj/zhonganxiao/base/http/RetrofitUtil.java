package com.dykj.zhonganxiao.base.http;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * File descripition:   RetrofitUtil工具类
 *
 * @author gk
 * @date 2019/6/25
 */

public class RetrofitUtil {
    /**
     * 将String 字符串转换为Rrtorfit: requestBody类型的value
     */
    public static RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = null;
        if (param!=null)
        requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    /**
     * 将所有的File图片集合转化为retorfit上传图片所需的： MultipartBody.Part类型的集合
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files, String key) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        if (files.size() > 0) {
            for (File file : files) {
                parts.add(filesToMultipartBodyParts(file, key));
            }
        }else {
            MultipartBody.Part part = MultipartBody.Part.createFormData("","");
            parts.add(part);
        }
        return parts;
    }

    /**
     * 将单个File图片转化为retorfit上传图片所需的： MultipartBody.Part类型
     */
    public static MultipartBody.Part filesToMultipartBodyParts(File file, String key) {
        if (file!=null){
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
            return part;
        }else {
            return null;
        }
    }

    public static List<File> initImages(List<String> mImages) {
        List<File> listPicture = new ArrayList<>();
        listPicture.clear();
        Iterator<String> stuIter = mImages.iterator();
        while (stuIter.hasNext()) {
            String mUrl = stuIter.next();
            listPicture.add(new File(mUrl));
        }
        return listPicture;
    }
}
