package com.gneo.fgurbanov.junctionhealth.di.modules

import com.gneo.fgurbanov.junctionhealth.di.CameraScope
import com.gneo.fgurbanov.junctionhealth.di.ConnectionsScope
import com.gneo.fgurbanov.junctionhealth.di.DetailScope
import com.gneo.fgurbanov.junctionhealth.di.MainScope
import com.gneo.fgurbanov.junctionhealth.presentation.camera.CameraActivity
import com.gneo.fgurbanov.junctionhealth.presentation.camera.CameraActivityModule
import com.gneo.fgurbanov.junctionhealth.presentation.connection.di.ConnectionModule
import com.gneo.fgurbanov.junctionhealth.presentation.connection.ui.ConnectionActivity
import com.gneo.fgurbanov.junctionhealth.presentation.detail.DetailActivity
import com.gneo.fgurbanov.junctionhealth.presentation.detail.DetailActivityModule
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.MainActivity
import com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ScreenModule {
    @ConnectionsScope
    @ContributesAndroidInjector(modules = [ConnectionModule::class])
    fun injectConnectionActivity(): ConnectionActivity

    @CameraScope
    @ContributesAndroidInjector(modules = [CameraActivityModule::class])
    fun injectCameraActivity(): CameraActivity

    @DetailScope
    @ContributesAndroidInjector(modules = [DetailActivityModule::class])
    fun injectDetailActivity(): DetailActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun injectMainActivity(): MainActivity
}