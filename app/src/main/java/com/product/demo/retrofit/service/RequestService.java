package com.product.demo.retrofit.service;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by huangsheng1 on 2016/7/20.
 */
public interface RequestService {


    @Multipart
    @POST("/hyga/manage/ajaxController/111111/execute?fc=4331010")
    Observable<String> sign(@Query("userId") String userId, @Query("signupType") String signupType,
                                                @Query("longitude") String longitude, @Query("latitude") String latitude, @Query("alarmSituationId") String alarmSituationId,
                                                @Query("signTime") String signTime, @PartMap Map<String, RequestBody> params);

}
