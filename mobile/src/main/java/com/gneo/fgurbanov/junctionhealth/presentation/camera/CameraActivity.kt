package com.gneo.fgurbanov.junctionhealth.presentation.camera

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.showOrGone
import com.gneo.fgurbanov.junctionhealth.utils.snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.SessionType
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.layout_progressbar.*
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class CameraActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: CameraViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)

        viewModel.cameraInfoVO.observe(this, Observer { dState ->
            dState?.let {
                handleDataState(it)
            }
        })

        with(camera) {
            setLifecycleOwner(this@CameraActivity)
            addCameraListener(object : CameraListener() {
                override fun onCameraOpened(options: CameraOptions?) {
//                onOpened()
                }

                override fun onPictureTaken(jpeg: ByteArray?) {
//                onPicture(jpeg)
                }

                override fun onVideoTaken(video: File) {
                    super.onVideoTaken(video)
                    onVideo(video)
                }
            })
            videoMaxSize = 10 * 1000 * 1000
            videoMaxDuration = 30 * 1000 * 1000
            sessionType = SessionType.VIDEO
            playSounds = false
            isSoundEffectsEnabled = false
        }
        buttonRecord.setOnClickListener { captureVideo() }
    }

    private fun handleDataState(dState: DState<CameraScreenVO>) {
        when (dState) {
            is DState.Loading -> updateLoading(true)
            is DState.Error -> {
                updateLoading(false)
                snackbar(container, dState.description)
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        progressBar.post {
            progressBar.showOrGone(isLoading)
        }
    }

    private fun onVideo(video: File) {
        viewModel.uploadVideo(video.absolutePath)
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
    }

    private fun captureVideo() {
        if (camera.isCapturingVideo) {
            camera.stopCapturingVideo()
            Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "start", Toast.LENGTH_SHORT).show()

            val directory = createDirectoryIfNeeded()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "A_" + timeStamp + "_"

            val image = File.createTempFile(
                imageFileName, // prefix
                ".mp4", // suffix
                directory      // directory
            )
            image.createNewFile()
            camera.startCapturingVideo(image)
        }
    }

    private fun createDirectoryIfNeeded(): File {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
            ), "JunctionCat"
        )
        if (!directory.isDirectory) {
            if (!directory.mkdirs()) {
                Timber.e("Directory not created")
            }
        } else {
            Timber.e("Directory exist")
        }
        return directory
    }
}