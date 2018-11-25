package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details

//import com.gneo.fgurbanov.junctionhealth.presentation.connection.data.BLEDataStore
//import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
//import rx.schedulers.Schedulers
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gneo.fgurbanov.junctionhealth.data.Detail
import com.gneo.fgurbanov.junctionhealth.navigation.DetailRoute
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.MainNetworkStore
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

interface DetailsViewModel {
    val checkoutInfoVO: LiveData<DState<List<Detail>>>
    fun updateScan()
    fun showDetail(id: Int)
}

class DetailsViewModelImpl @Inject constructor(
    private val store: MainNetworkStore,
    private val route: DetailRoute
) : ViewModel(), DetailsViewModel {
    private val disposable = CompositeSubscription()

    override val checkoutInfoVO = MutableLiveData<DState<List<Detail>>>()

    init {
        updateScan()
    }

    override fun updateScan() {
        disposable += store.getDetails()
            .doOnSubscribe { checkoutInfoVO.postValue(DState.Loading()) }
            .subscribe({
                checkoutInfoVO.postValue(DState.Success(it))
            }, {
                checkoutInfoVO.postValue(DState.Error(description = it.message ?: it.localizedMessage))
            })

    }

    override fun showDetail(id: Int) {
        route.start(DetailRoute.Data().apply {
            this.id = id.toString()
        })
    }

    override fun onCleared() {
        disposable.clear()
    }
}