package com.gneo.fgurbanov.junctionhealth.presentation.connection.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gneo.fgurbanov.junctionhealth.utils.DState
//import com.gneo.fgurbanov.junctionhealth.presentation.connection.data.BLEDataStore
//import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
//import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

interface ConnectionViewModel {
    val checkoutInfoVO: LiveData<DState<ConnectionScreenVO>>
    fun updateScan()
    fun connectToItem(mac: String)
}

class ConnectionViewModelImpl @Inject constructor(
//    private val BLEStore: BLEDataStore
) : ViewModel(), ConnectionViewModel {
    private val disposable = CompositeSubscription()

    override val checkoutInfoVO = MutableLiveData<DState<ConnectionScreenVO>>()

    override fun updateScan() {
//        disposable += BLEStore.scanDevice()
//            .doOnSubscribe { checkoutInfoVO.postValue(DState.Loading()) }
//            .map { ConnectionScreenVO(it, null) }
//            .subscribe({
//                checkoutInfoVO.postValue(DState.Success(it))
//            }, {
//                checkoutInfoVO.postValue(DState.Error(description = it.message ?: it.localizedMessage))
//            })

    }

    override fun connectToItem(mac: String) {
//        disposable += BLEStore.connectToDevice(mac)
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.computation())
//            .doOnSubscribe { checkoutInfoVO.postValue(DState.Loading()) }
//            .map { ConnectionScreenVO(null, it) }
//            .subscribe({
//                checkoutInfoVO.postValue(DState.Success(it))
//            }, {
//                checkoutInfoVO.postValue(DState.Error(description = it.message ?: it.localizedMessage))
//            })
    }

    override fun onCleared() {
        disposable.clear()
    }
}