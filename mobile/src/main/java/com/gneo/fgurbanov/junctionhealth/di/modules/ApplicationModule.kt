package com.gneo.fgurbanov.junctionhealth.di.modules

import android.content.Context
import com.gneo.fgurbanov.junctionhealth.JunctionApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: JunctionApplication): Context =
        application.applicationContext
}

