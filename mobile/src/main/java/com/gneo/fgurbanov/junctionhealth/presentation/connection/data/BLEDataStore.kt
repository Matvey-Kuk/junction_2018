//package com.gneo.fgurbanov.junctionhealth.presentation.connection.data
//
//import android.content.Context
//import com.gneo.fgurbanov.junctionhealth.presentation.connection.ui.ConnectionVO
//import com.movesense.mds.Mds
//import com.movesense.mds.MdsConnectionListener
//import com.movesense.mds.MdsException
//import com.polidea.rxandroidble.RxBleClient
//import com.polidea.rxandroidble.RxBleDevice
//import com.polidea.rxandroidble.scan.ScanResult
//import com.polidea.rxandroidble.scan.ScanSettings
//import rx.Single
//import rx.schedulers.Schedulers
//import javax.inject.Inject
//
//
//interface BLEDataStore {
//    fun scanDevice(): Single<List<ConnectionVO>>
//    fun getBleDevice(mac: String): ConnectionVO?
//    fun connectToDevice(mac: String): Single<ConnectionVO>
//}
//
//class BLEDataStoreImpl @Inject constructor(
//    context: Context
//) : BLEDataStore {
//    private var rxBleClient = RxBleClient.create(context)
//    private var mds = Mds.builder().build(context)
//
//    private val connectedMacDevices = mutableSetOf<String>()
//    private val devices = mutableSetOf<ConnectionVO>()
//
//    override fun scanDevice(): Single<List<ConnectionVO>> =
//        rxBleClient.scanBleDevices(ScanSettings.Builder().build())
//            .subscribeOn(Schedulers.io())
//            .observeOn(Schedulers.computation())
//            .filter { scanResult ->
//                scanResult.bleDevice?.name?.startsWith("Movesense") ?: false
//            }
//            .map { result: ScanResult ->
//                mapBLEDevToConnectionVO(result.bleDevice)
//            }
//            .filter {  it.name.contains("175030000145") }
//            .distinct()
//            .take(1)
//            .toList()
//            .toSingle()
//
//
//    override fun getBleDevice(mac: String): ConnectionVO =
//        mapBLEDevToConnectionVO(rxBleClient.getBleDevice(mac))
//
//
//    private fun mapBLEDevToConnectionVO(device: RxBleDevice) = with(device) {
//        ConnectionVO(
//            name = name ?: macAddress,
//            status = "status ${if (connectedMacDevices.find { it == macAddress } != null) "CONNECTED" else " DISCONNECTED"}",
//            macAddress = macAddress,
//            isConnected = connectedMacDevices.find { it == macAddress } != null
//        )
//    }
//
//    override fun connectToDevice(mac: String): Single<ConnectionVO> {
//        val device = getBleDevice(mac)
//
//        return Single.create { emitter ->
//            mds.connect(mac, object : MdsConnectionListener {
//                override fun onConnect(s: String) {
//                }
//
//                override fun onConnectionComplete(macAddress: String, serial: String) {
//                    val connectedDevice = device.copy(isConnected = true)
//                    connectedMacDevices.add(connectedDevice.macAddress)
//                    emitter.onSuccess(connectedDevice)
//                }
//
//                override fun onError(e: MdsException) {
//                    emitter.onError(e)
//                }
//
//                override fun onDisconnect(bleAddress: String) {
//                    val connectedDevice = device.copy(isConnected = false)
//                    connectedMacDevices.remove(connectedDevice.macAddress)
//                    emitter.onSuccess(connectedDevice)
//                }
//            })
//        }
//    }
//
//}