package com.gneo.fgurbanov.junctionhealth.presentation.detail

import com.gneo.fgurbanov.junctionhealth.data.Detail
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.Path
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface DetailNetworkStore {
    fun getDetails(id: Int): Observable<Detail>
}

class DetailNetworkStoreImpl @Inject constructor(
    retrofit: Retrofit
) : DetailNetworkStore {
    private val apiClient = retrofit.create(DetailNetworkApiService::class.java)

    override fun getDetails(id: Int): Observable<Detail> =
        apiClient.getDetails(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}

interface DetailNetworkApiService {

    @GET("video/{id}/")
    fun getDetails(@Path("id") id: Int): Observable<Detail>

}