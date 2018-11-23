package com.gneo.fgurbanov.junctionhealth.di.modules

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideResources(context: Context): Resources = context.resources
}