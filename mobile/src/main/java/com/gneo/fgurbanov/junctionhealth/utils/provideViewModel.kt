package com.gneo.fgurbanov.junctionhealth.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity

/**
 * Предоставляет [ViewModel]. Если модель не была ранее инициализирована, будет вызван get у [lazyViewModel]
 */
inline fun <reified VM : ViewModel> AppCompatActivity.provideViewModel(lazyViewModel: DaggerLazy<VM>): VM =
    ViewModelProvider(this, object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return lazyViewModel.get() as T
        }
    }).get(VM::class.java)

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): T = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> Fragment.viewModel(): T = ViewModelProviders.of(this).get(T::class.java)

inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline initializer: () -> T): T {
    return ViewModelProviders.of(this, vmFactory(initializer)).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(crossinline initializer: () -> T): T {
    return ViewModelProviders.of(this, vmFactory(initializer)).get(T::class.java)
}

@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> vmFactory(crossinline initializer: () -> VM) =
    object : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = initializer() as T
    }