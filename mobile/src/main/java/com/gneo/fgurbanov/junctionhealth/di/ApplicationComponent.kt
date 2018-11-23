package com.gneo.fgurbanov.junctionhealth.di

import android.content.Context
import android.content.res.Resources
import com.gneo.fgurbanov.junctionhealth.JunctionApplication
import com.gneo.fgurbanov.junctionhealth.data.DataRepository
import com.gneo.fgurbanov.junctionhealth.di.modules.*
import com.google.gson.GsonBuilder
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AppModule::class,
        NetworkModule::class,
        PreferencesModule::class,
        RepositoryModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<JunctionApplication> {
    override fun inject(instance: JunctionApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: JunctionApplication): Builder

        fun build(): ApplicationComponent
    }
    val context: Context
    val resources: Resources

    val mainRepository: DataRepository

    val gsonBuilder: GsonBuilder
}