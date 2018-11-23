package com.gneo.fgurbanov.junctionhealth.navigation

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.gneo.fgurbanov.junctionhealth.R
import javax.inject.Inject

class AuthRoute @Inject constructor(private val context: Context) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data): Uri =
        Route.builder(context, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_auth)
            .let { data.appendAsQuery(it) }
            .build()

    fun start(activity: Activity, data: Data, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)

}

class MainRoute @Inject constructor(private val context: Context) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data): Uri =
        Route.builder(context, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_main)
            .let { data.appendAsQuery(it) }
            .build()

    fun start(activity: Activity, data: Data, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}