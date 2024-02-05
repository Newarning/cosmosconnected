package com.cosmoconnected.connectedcosmos.domain.model

data class Device(
    val macAddress: String,
    val model: String,
    val product: String?,
    val firmwareVersion: String,
    val serial: String?,
    val installationMode: String?,
    val brakeLight: Boolean,
    val lightMode: String?,
    val lightAuto: Boolean,
    val lightValue: Int
) {
    companion object {
        val default = Device(
            macAddress = "macAddress",
            model = "model",
            product = "product",
            firmwareVersion = "firmwareVersion",
            serial = "serial",
            installationMode = "installationMode",
            brakeLight = false,
            lightMode = "lightMode",
            lightAuto = false,
            lightValue = 50,
        )
    }
}