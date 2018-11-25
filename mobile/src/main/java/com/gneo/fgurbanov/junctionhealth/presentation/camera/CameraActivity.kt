package com.gneo.fgurbanov.junctionhealth.presentation.camera

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.presentation.camera.data.HRResponse
import com.gneo.fgurbanov.junctionhealth.presentation.camera.data.LinearAcceleration
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.plusAssign
import com.gneo.fgurbanov.junctionhealth.utils.showOrGone
import com.gneo.fgurbanov.junctionhealth.utils.snackbar
import com.google.gson.Gson
import com.movesense.mds.*
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.SessionType
import com.polidea.rxandroidble.RxBleClient
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.layout_progressbar.*
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.roundToInt


class CameraActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModel: CameraViewModel

    private val mMds: Mds by lazy { Mds.builder().build(this) }
    private var mBleClient: RxBleClient? = null


    private var mHRSubscription: MdsSubscription? = null
    private var mLinearSubscription: MdsSubscription? = null
    private var subscriptions = CompositeSubscription()
    private val statusSubject = BehaviorSubject.create<Boolean>(true)

    companion object {
        private const val MAC = "0C:8C:DC:22:2A:DF"
        private const val SERIAL = "175030000145"

        private const val URI_EVENTLISTENER = "suunto://MDS/EventListener"
        private const val URI_MEAS_HR = "/Meas/HR"
        private const val LINEAR_ACC_PATH = "Meas/Acc/26"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)

        viewModel.cameraInfoVO.observe(this, Observer { dState ->
            dState?.let {
                handleDataState(it)
            }
        })
        updateLoading(false)
        with(camera) {
            setLifecycleOwner(this@CameraActivity)
            addCameraListener(object : CameraListener() {
                override fun onCameraOpened(options: CameraOptions?) {
                }

                override fun onPictureTaken(jpeg: ByteArray?) {
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
        firstDeviceTV.setOnClickListener { connectBLEDevice() }

        connectBLEDevice()
    }

    private fun getBleClient(): RxBleClient {
        // Init RxAndroidBle (Ble helper library) if not yet initialized
        if (mBleClient == null) {
            mBleClient = RxBleClient.create(this)
        }

        return mBleClient!!
    }

    private fun connectBLEDevice() {
        val bleDevice = getBleClient().getBleDevice(MAC)
        mMds.connect(bleDevice.macAddress, object : MdsConnectionListener {
            override fun onConnect(s: String) {
                firstDeviceTV.text = "$MAC\nonConnect:$s"
                Timber.e("onConnect:$s")
            }

            override fun onConnectionComplete(macAddress: String, serial: String) {
                firstDeviceTV.text = "$MAC\nCONNECT"
                snackbar(container, "CONNECT")
                subscribeOnHR()
            }

            override fun onError(e: MdsException) {
                firstDeviceTV.text = "$MAC\nERROR: ${e.localizedMessage}"
                snackbar(container, "CONNECTION ERROR")
            }

            override fun onDisconnect(bleAddress: String) {
                firstDeviceTV.text = "$MAC\nonDisconnect: $bleAddress"
                snackbar(container, "DISCONNECT")
            }
        })
    }

    fun subscribeOnHR() {
        unsubscribeHR()

        subscriptions += statusSubject.subscribe({ status ->
            statusBar.setBackgroundColor(ContextCompat.getColor(this, if (status) R.color.green else R.color.red))
            if (!status)
                statusBar.text = ""
        }, { e ->
            snackbar(container, e.localizedMessage)
        })

        val sb = StringBuilder()
        val strContract = sb.append("{\"Uri\": \"").append(SERIAL).append(URI_MEAS_HR).append("\"}").toString()
        Timber.d(strContract)

        mHRSubscription = Mds.builder()
            .operationTimeoutMs(100)
            .build(this)
            .subscribe(URI_EVENTLISTENER,
                strContract,
                object : MdsNotificationListener {
                    override fun onNotification(data: String) {
                        Timber.d("onNotification(): $data")

                        updateHeartRate(data)
                    }

                    override fun onError(error: MdsException) {
                        unsubscribeHR()
                    }
                })

        mLinearSubscription = Mds.builder()
            .operationTimeoutMs(100)
            .build(this)
            .subscribe("suunto://MDS/EventListener",
                formatContractToJson(SERIAL, LINEAR_ACC_PATH),
                object : MdsNotificationListener {
                    override fun onNotification(s: String) {
                        val linearAccelerationData = Gson().fromJson(
                            s, LinearAcceleration::class.java
                        )

                        if (linearAccelerationData != null) {
                            val arrayData = linearAccelerationData.body.array[0]
                            updateAccuracy(arrayData.x, arrayData.y, arrayData.z)
                        }
                    }

                    override fun onError(e: MdsException) {
                        snackbar(container, e.localizedMessage)
                    }
                })
    }

    private fun updateAccuracy(x: Double, y: Double, z: Double) {
        val acInPoint = Math.sqrt(x.pow(2) + y.pow(2) + z.pow(2)).absoluteValue
        val data = StringBuilder()
            .append(
                String.format(
                    Locale.getDefault(),
                    "x: %.3f", x
                )
            )
            .append("\n")
            .append(
                String.format(
                    Locale.getDefault(),
                    "y: %.3f", y
                )
            ).append("\n")
            .append(
                String.format(
                    Locale.getDefault(),
                    "z: %.3f", z
                )
            )
            .append("\n")
            .append("ac = $acInPoint")
            .toString()
        firstDeviceLinearTV.text = data

        when {
            acInPoint < 7.5 || acInPoint > 12.5 -> {
                statusBar.text = "High intensity"
                statusSubject.onNext(false)
            }
            acInPoint in 9.0..9.5 || acInPoint in 10.1..10.6 -> {
                statusBar.text = "Low intensity"
                statusSubject.onNext(false)
            }
            acInPoint in 9.5..10.1 -> statusBar.showOrGone(false)
            else -> statusSubject.onNext(true)
        }
    }

    private fun updateHeartRate(data: String) {
        val hrResponse = Gson().fromJson(
            data, HRResponse::class.java
        )

        firstDeviceHRTV.text = "HR:${hrResponse.body.average.roundToInt()}"
        if (hrResponse.body.average > 160) {
            snackbar(container, "This weight is too high, hr > 160")
            statusSubject.onNext(false)
        } else {
            statusSubject.onNext(true)
        }
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

    override fun onStop() {
        unsubscribeHR()
        super.onStop()
    }


    private fun unsubscribeHR() {
        mHRSubscription?.unsubscribe()
        mHRSubscription = null
        mLinearSubscription?.unsubscribe()
        mLinearSubscription = null
        subscriptions.unsubscribe()

        statusBar.showOrGone(false)
    }


    fun formatContractToJson(serial: String, uri: String): String {
        val sb = StringBuilder()
        return sb.append("{\"Uri\": \"").append(serial).append("/").append(uri).append("\"}").toString()
    }
}