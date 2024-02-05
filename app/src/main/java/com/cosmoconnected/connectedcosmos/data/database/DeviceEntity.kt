package com.cosmoconnected.connectedcosmos.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
class DeviceEntity(
    @PrimaryKey(autoGenerate = true) var roomId: Int,
    val macAddress: String,
    val model: String,
    val product: String?,
    val firmwareVersion: String,
    val serial: String?,
    val installationMode: String?,
    val brakeLight: Boolean,
    val lightMode: String?,
    val lightAuto: Boolean,
    val lightValue: Int,
)
