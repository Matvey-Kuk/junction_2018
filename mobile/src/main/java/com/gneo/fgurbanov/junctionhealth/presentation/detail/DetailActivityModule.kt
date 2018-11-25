package com.gneo.fgurbanov.junctionhealth.presentation.detail

import com.gneo.fgurbanov.junctionhealth.navigation.MainRoute
import com.gneo.fgurbanov.junctionhealth.utils.viewModel
import dagger.*

@Module
abstract class DetailActivityModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideDetailViewModel(
            activity: DetailActivity,
            viewModel: Lazy<DetailViewModelImpl>
        ): DetailViewModel = activity.viewModel { viewModel.get() }

        @Provides
        @JvmStatic
        @Reusable
        fun provideMainRoute(
            activity: DetailActivity
        ): MainRoute = MainRoute(activity)

        @Provides
        @JvmStatic
        @Reusable
        fun provideInitialScreenData(
            activity: DetailActivity
        ): InitialScreenData = InitialScreenData(
            id = activity.intent?.data?.getQueryParameter("id")?.toIntOrNull() ?: 2
        )
    }

    @Binds
    @Reusable
    abstract fun provideDetailNetworkStore(impl: DetailNetworkStoreImpl): DetailNetworkStore
}