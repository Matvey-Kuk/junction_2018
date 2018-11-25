package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity

import com.gneo.fgurbanov.junctionhealth.navigation.DetailRoute
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details.DetailsFragment
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details.DetailsModule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {


    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideDetailRoute(
            activity: MainActivity
        ): DetailRoute = DetailRoute(activity)
    }

    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun injectDetailsFragment(): DetailsFragment

    @Binds
    @Reusable
    abstract fun provideMainNetworkStore(impl: MainNetworkStoreImpl): MainNetworkStore
}