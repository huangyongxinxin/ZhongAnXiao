package com.dykj.zhonganxiao.base.http;


import com.dykj.zhonganxiao.bean.BaseResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * File descripition:
 *
 * @author gk
 * @date 2019/6/19
 */

public interface ApiServer {
/********************演示事例开始***********************/
    /**
     * 第一种写法
     *
     * @param requestType
     * @return
     */
    @POST("api/Activity/get_activities?")
    Observable<BaseResponse<List<String>>> getMain(@Query("time") String requestType);

    /**
     * 第二种写法
     *
     * @param requestType
     * @return
     */
    @FormUrlEncoded
    @POST("api/Activity/get_activities?")
    Observable<BaseResponse<List<String>>> getMain2(@Field("time") String requestType);

    /**
     * 第三种写法
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("api/Activity/get_activities?")
    Observable<BaseResponse<HashMap<String, String>>> getMain3(@FieldMap HashMap<String, String> params);


/********************分类模块结束***********************/


//    @Streaming
//    @GET
//    Call<ResponseBody> getImage(@Url String url);


}
