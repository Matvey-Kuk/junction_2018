package com.gneo.fgurbanov.junctionhealth.data

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable


/**
 * Service to work with main API
 */
interface ApiService {

    @POST("user/first-launch")
    fun firstLaunch(): Observable<Response<ResponseBody>>


    @Multipart
    @POST("product/image")
    fun postProductPhoto(@Part file: MultipartBody.Part): Observable<Response<ResponseBody>>

}