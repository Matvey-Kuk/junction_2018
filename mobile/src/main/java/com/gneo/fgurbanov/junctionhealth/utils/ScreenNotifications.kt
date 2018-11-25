package com.gneo.fgurbanov.junctionhealth.utils

import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import com.gneo.fgurbanov.junctionhealth.R

fun <T : Fragment> T.snackbar(message: String, action: (() -> Unit)? = null) {
    view?.let {
        this.requireContext().snackbar(it, message, action)
    }
}

fun Context.snackbar(rootView: View, @StringRes message: Int, action: (() -> Unit)? = null) {
    snackbar(rootView, getString(message), action)
}

fun Context.snackbar(rootView: View, message: String, action: (() -> Unit)? = null) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        .also { snackbar ->
            action?.also { listener ->
                snackbar.setAction(R.string.refresh) { listener.invoke() }
            }
        }.setActionTextColor(ContextCompat.getColor(this, R.color.white))
        .show()
}