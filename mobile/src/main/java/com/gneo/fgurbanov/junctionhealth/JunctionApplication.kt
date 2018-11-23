package com.gneo.fgurbanov.junctionhealth

import android.content.Context
import android.os.StrictMode
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.gneo.fgurbanov.junctionhealth.di.ApplicationComponent
import com.gneo.fgurbanov.junctionhealth.di.application.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Android Main Application
 */
class JunctionApplication : DaggerApplication() {

    private val applicationComponent: ApplicationComponent =
        DaggerApplicationComponent.builder()
            .application(this)
            .build()

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        applicationComponent.apply { inject(this@JunctionApplication) }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        fun appComponent(context: Context) = (context.applicationContext as JunctionApplication).applicationComponent
    }

}
