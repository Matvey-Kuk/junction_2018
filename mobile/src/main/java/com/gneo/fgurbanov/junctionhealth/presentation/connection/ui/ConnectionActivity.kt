package com.gneo.fgurbanov.junctionhealth.presentation.connection.ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_connection.*
import timber.log.Timber
import javax.inject.Inject

class ConnectionActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: ConnectionViewModel

    private val adapter: ConnectionAdapter = ConnectionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)

        viewModel.checkoutInfoVO.observe(this, Observer { dState ->
            dState?.let {
                handleDataState(it)
            }
        })

        swipeRL.setOnRefreshListener {
            viewModel.updateScan()
        }

        with(connectionRC) {
            layoutManager = android.support.v7.widget.LinearLayoutManager(context)
            adapter = this@ConnectionActivity.adapter.apply {
                onConnectClick = {mac -> viewModel.connectToItem(mac) }
            }
        }

        checkPermissionEnabled()
    }


    private fun checkPermissionEnabled() {
        if (isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            viewModel.updateScan()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            snackbar(container, R.string.permission_location_rationale) {
                requestPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_FINE_LOCATION)
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_FINE_LOCATION
            )
        }
    }

    private fun handleDataState(dState: DState<ConnectionScreenVO>) {
        when (dState) {
            is DState.Loading -> updateLoading(true)
            is DState.Error -> {
                updateLoading(false)
                snackbar(container, dState.description) {
                    viewModel.updateScan()
                }
            }
            is DState.Success -> {
                updateLoading(false)
                dState.data.list?.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        swipeRL.post {
            if (isLoading != swipeRL.isRefreshing) {
                swipeRL.isRefreshing = isLoading
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSIONS_REQUEST_FINE_LOCATION) {
            // Received permission result for camera permission.
            Timber.i("Received response for Location permission request.")

            // Check if the permission has been granted
            if (grantResults.containsOnly(PackageManager.PERMISSION_GRANTED)) {
                // Camera permission has been granted, preview can be displayed
                Timber.i("Location permission has now been granted. Start scanning.")
                snackbar(container, R.string.permissions_available_location)
                viewModel.updateScan()
            } else {
                snackbar(container, R.string.permissions_not_granted)
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_FINE_LOCATION = 100
    }
}
