package com.gneo.fgurbanov.junctionhealth.presentation.camera

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gneo.fgurbanov.junctionhealth.navigation.DetailRoute
import com.gneo.fgurbanov.junctionhealth.presentation.camera.data.CameraNetworkStore
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.multipartFromData
import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
import okhttp3.MultipartBody
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.io.File
import javax.inject.Inject

interface CameraViewModel {

    val cameraInfoVO: LiveData<DState<CameraScreenVO>>
    fun uploadVideo(absolutePath: String?)
}

class CameraViewModelImpl @Inject constructor(
    private val networkStore: CameraNetworkStore,
    private val route: DetailRoute
) : ViewModel(), CameraViewModel {
    private val disposable = CompositeSubscription()

    override val cameraInfoVO = MutableLiveData<DState<CameraScreenVO>>()

    override fun uploadVideo(absolutePath: String?) {
        absolutePath?.let {
            disposable += networkStore.uploadVideo(convertUrlToMultipart(it))
                .doOnSubscribe { cameraInfoVO.postValue(DState.Loading()) }
                .subscribe({
                    route.start(DetailRoute.Data().apply {
                        this.id = id.toString()
                    })
                }, { error ->
                    cameraInfoVO.postValue(DState.Error(description = error.message ?: error.localizedMessage))
                    Timber.e(error)
                })
        }
    }

    private fun convertUrlToMultipart(url: String): MultipartBody.Part =
        File(url).multipartFromData(url)

    override fun onCleared() {
        disposable.clear()
    }
}