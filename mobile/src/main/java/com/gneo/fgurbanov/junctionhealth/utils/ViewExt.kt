package com.gneo.fgurbanov.junctionhealth.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import rx.Subscription
import rx.subscriptions.CompositeSubscription


fun TextView.setTextOrGone(value: CharSequence?) {
    this.visibility = value?.takeIf { it.trim().isNotEmpty() }?.let {
        this.text = it
        View.VISIBLE
    } ?: View.GONE
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}



 operator fun CompositeSubscription.plusAssign(subscribe: Subscription?) {
    this.add(subscribe)
}

fun View.showOrGone(show: Boolean?) {
    visibility = when (show) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}