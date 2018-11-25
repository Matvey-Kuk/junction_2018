package com.gneo.fgurbanov.junctionhealth.presentation.camera

import com.gneo.fgurbanov.junctionhealth.navigation.DetailRoute
import com.gneo.fgurbanov.junctionhealth.presentation.camera.data.CameraNetworkStore
import com.gneo.fgurbanov.junctionhealth.presentation.camera.data.CameraNetworkStoreImpl
import com.gneo.fgurbanov.junctionhealth.utils.viewModel
import dagger.*

@Module
abstract class CameraActivityModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideCameraViewModel(
            activity: CameraActivity,
            viewModel: Lazy<CameraViewModelImpl>
        ): CameraViewModel = activity.viewModel { viewModel.get() }

        @Provides
        @JvmStatic
        @Reusable
        fun provideCameraDetailRoute(
            activity: CameraActivity
        ): DetailRoute = DetailRoute(activity)
    }

    @Binds
    @Reusable
    abstract fun provideCameraNetworkStore(impl: CameraNetworkStoreImpl): CameraNetworkStore
}