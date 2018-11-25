package com.gneo.fgurbanov.junctionhealth.navigation

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.gneo.fgurbanov.junctionhealth.R
import javax.inject.Inject

class AuthRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_auth)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)

}

class ConnectionRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_connection)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}

class MainRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_main)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}

class OldRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_old)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}

class CameraRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_camera)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}

class DetailRoute @Inject constructor(private val activity: Activity) : Route {

    class Data(uri: Uri? = null) : UriData(uri) {
        var id by map
    }

    private fun uri(data: Data?): Uri =
        Route.builder(activity, R.string.navigator_scheme, R.string.host_navigator, R.string.navigator_path_detail)
            .let { data?.appendAsQuery(it) ?: it }
            .build()

    fun start(data: Data? = null, bundle: Bundle = Bundle()) =
        Route.start(activity, uri(data), bundle)
}