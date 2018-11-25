package com.gneo.fgurbanov.junctionhealth.presentation.connection.ui

data class ConnectionScreenVO(
    val list: List<ConnectionVO>?,
    val changedItem: ConnectionVO?
)

data class ConnectionVO(
    val name: String,
    val status: String,
    val macAddress: String,
    val isConnected: Boolean
)