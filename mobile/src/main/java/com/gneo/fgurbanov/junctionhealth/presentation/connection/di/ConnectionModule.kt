package com.gneo.fgurbanov.junctionhealth.presentation.connection.di

//import com.gneo.fgurbanov.junctionhealth.presentation.connection.data.BLEDataStore
//import com.gneo.fgurbanov.junctionhealth.presentation.connection.data.BLEDataStoreImpl
import com.gneo.fgurbanov.junctionhealth.presentation.connection.ui.ConnectionActivity
import com.gneo.fgurbanov.junctionhealth.presentation.connection.ui.ConnectionViewModel
import com.gneo.fgurbanov.junctionhealth.presentation.connection.ui.ConnectionViewModelImpl
import com.gneo.fgurbanov.junctionhealth.utils.viewModel
import dagger.*

@Module
abstract class ConnectionModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideConnectionViewModel(
            activity: ConnectionActivity,
            viewModel: Lazy<ConnectionViewModelImpl>
        ): ConnectionViewModel = activity.viewModel { viewModel.get() }
    }

//    @Binds
//    @Reusable
//    abstract fun provideLocalDataStore(impl: BLEDataStoreImpl): BLEDataStore
}