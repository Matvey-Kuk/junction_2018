package com.gneo.fgurbanov.junctionhealth.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes

interface Route {

    companion object {
        fun builder(
            context: Context, @StringRes scheme: Int, @StringRes host: Int, @StringRes pathPrefix: Int
        ): Uri.Builder =
            Uri.Builder().scheme(
                context.getString(scheme)
            )
                .authority(context.getString(host))
                .appendEncodedPath(
                    context.getString(pathPrefix).removePrefix("/")
                )

        fun builder(scheme: String, host: String, pathPrefix: String): Uri.Builder =
            Uri.Builder().scheme(scheme)
                .authority(host)
                .appendEncodedPath(pathPrefix.removePrefix("/"))

        fun start(
            context: Context, uri: Uri, options: Bundle = Bundle(),
            intentEditor: (Intent) -> Unit = {}
        ) {
            context.startActivity(
                intent(
                    context, uri,
                    intentEditor
                ), options
            )
        }

        fun startForResult(
            activity: Activity, uri: Uri,
            requestCode: Int = -1, options: Bundle = Bundle(),
            intentEditor: (Intent) -> Unit = {}
        ) {
            activity.startActivityForResult(
                intent(
                    activity, uri,
                    intentEditor
                ), requestCode, options
            )
        }

        fun intent(
            context: Context, uri: Uri,
            intentEditor: (Intent) -> Unit = {}
        ): Intent {
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.`package` = context.packageName
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intentEditor.invoke(intent)
            return intent
        }

    }
}

abstract class UriData(uri: Uri? = null) {
    protected val map: MutableMap<String, String?> = mutableMapOf<String, String?>().withDefault { null }

    init {
        uri?.let { it.queryParameterNames.forEach { map[it] = uri.getQueryParameter(it) } }
    }

    fun appendAsQuery(builder: Uri.Builder): Uri.Builder {
        map.forEach { it -> builder.appendQueryParameter(it.key, it.value) }
        return builder
    }

    fun buildUri(): Uri {
        return Uri.Builder().let { appendAsQuery(it) }.build()
    }
}

