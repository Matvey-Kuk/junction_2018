package com.gneo.fgurbanov.junctionhealth.presentation.camera.data

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

interface CameraNetworkStore {
    fun uploadVideo(video: MultipartBody.Part): Observable<Int>
}

class CameraNetworkStoreImpl @Inject constructor(
    retrofit: Retrofit
) : CameraNetworkStore {
    private val apiClient = retrofit.create(CameraNetworkApiService::class.java)


    override fun uploadVideo(video: MultipartBody.Part): Observable<Int> =
        apiClient.uploadVideo(video)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.videoId }

}


/**
 * Service to work with main API
 */
interface CameraNetworkApiService {

    @Multipart
    @PUT("publish_video/")
    fun uploadVideo(@Part file: MultipartBody.Part): Observable<UploadResponse>

}

data class UploadResponse(
    val videoId : Int
)