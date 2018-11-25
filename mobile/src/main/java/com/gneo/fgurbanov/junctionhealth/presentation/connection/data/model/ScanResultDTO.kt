package com.gneo.fgurbanov.junctionhealth.presentation.connection.data.model

data class ScanResultDTO(
    val rssi: Int,
    val macAddress: String,
    val name: String,
    val connectedSerial: String
)