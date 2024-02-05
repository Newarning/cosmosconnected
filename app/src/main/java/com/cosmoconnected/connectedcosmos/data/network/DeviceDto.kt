package com.cosmoconnected.connectedcosmos.data.network

data class DeviceDto(
    val macAddress: String,//"4921201e38d5"
    val model: String, //"RIDE/VISION/REMOTE"
    val product: String?,//"RIDE/ RIDE_LITE / REMOTE_V1"
    val firmwareVersion: String,//"2.2.2"
    val serial: String?,//"BC892C9C-047D-8402-A9FD-7B2CC0048736"
    val installationMode: String?,//"helmet/seat"
    val brakeLight: Boolean, //
    val lightMode: String?, //"OFF/ BOTH/ WARNING/ POSITION"
    val lightAuto: Boolean,
    val lightValue: Int,//0--> 100
    )