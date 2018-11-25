package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity

import com.gneo.fgurbanov.junctionhealth.data.Detail
import retrofit2.Retrofit
import retrofit2.http.GET
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

interface MainNetworkStore {
    fun getDetails(): Observable<List<Detail>>
}

class MainNetworkStoreImpl @Inject constructor(
    retrofit: Retrofit
) : MainNetworkStore {
    private val apiClient = retrofit.create(MainNetworkApiService::class.java)

    override fun getDetails(): Observable<List<Detail>> =
        apiClient.getDetails()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}

interface MainNetworkApiService {

    @GET("video/")
    fun getDetails(): Observable<List<Detail>>

}