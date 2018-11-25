package com.gneo.fgurbanov.junctionhealth.presentation.detail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gneo.fgurbanov.junctionhealth.data.Detail
import com.gneo.fgurbanov.junctionhealth.navigation.MainRoute
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
import rx.Observable
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface DetailViewModel {
    fun toMainScreen()

    val detailScreenLD: LiveData<DState<Detail>>
}

class DetailViewModelImpl @Inject constructor(
    private val networkStore: DetailNetworkStore,
    private val route: MainRoute,
    private val initialScreenData: InitialScreenData
) : ViewModel(), DetailViewModel {
    private val disposable = CompositeSubscription()

    override val detailScreenLD = MutableLiveData<DState<Detail>>()

    init {
        disposable += Observable.interval(1L, TimeUnit.SECONDS)
            .doOnSubscribe { detailScreenLD.postValue(DState.Loading()) }
            .flatMap {
                networkStore.getDetails(initialScreenData.id)
            }
            .filter { response -> !response.renderedVideo.isNullOrEmpty()}
            .first()
            .subscribe({
                detailScreenLD.postValue(DState.Success(it))
            }, { error ->
                detailScreenLD.postValue(DState.Error(description = error.message ?: error.localizedMessage))
                Timber.e(error)
            })

    }

    override fun toMainScreen() {
        route.start()
    }

    override fun onCleared() {
        disposable.clear()
    }
}