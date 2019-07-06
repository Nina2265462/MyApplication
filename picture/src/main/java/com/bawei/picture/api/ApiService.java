package com.bawei.picture.api;

import com.bawei.picture.entity.HeadEntity;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*
 *@Auther:cln
 *@Date: 2019/7/6
 *@Description:功能
 * */
public interface ApiService {
    @POST(Api.HEADPIC_URL)
    @Multipart
    Observable<HeadEntity> getHeadPic(@HeaderMap HashMap<String, String> map, @Part MultipartBody.Part filepart);
}
