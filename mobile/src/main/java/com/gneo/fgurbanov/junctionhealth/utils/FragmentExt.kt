package com.gneo.fgurbanov.junctionhealth.utils

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment


fun Fragment.isPermissionGranted(permission: String) =
    ActivityCompat.checkSelfPermission(this.activity!!, permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.shouldShowPermissionRationale(permission: String) =
    shouldShowRequestPermissionRationale(permission)

fun Fragment.requestPermission(permission: String, requestId: Int) =
    requestPermissions(arrayOf(permission), requestId)

fun Fragment.batchRequestPermissions(permissions: Array<String>, requestId: Int) =
    requestPermissions(permissions, requestId)


fun IntArray.containsOnly(num: Int): Boolean = any { it == num }


inline fun <T : Fragment> T.withArgs(
    argsBuilder: Bundle.() -> Unit): T =
    this.apply {
        arguments = Bundle().apply(argsBuilder)
    }