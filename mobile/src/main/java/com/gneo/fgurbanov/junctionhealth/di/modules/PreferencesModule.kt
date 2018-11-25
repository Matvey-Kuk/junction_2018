package com.gneo.fgurbanov.junctionhealth.di.modules

import android.content.Context
import android.preference.PreferenceManager
import com.gneo.fgurbanov.junctionhealth.data.Preferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule {

    @Provides
    @Singleton
    fun providePreferences(context: Context): Preferences =
        Preferences(PreferenceManager.getDefaultSharedPreferences(context))
}