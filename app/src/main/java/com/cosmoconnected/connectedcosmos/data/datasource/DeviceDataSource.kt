package com.cosmoconnected.connectedcosmos.data.datasource

import com.cosmoconnected.connectedcosmos.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceDataSource {

    suspend fun getAllDevice(): Flow<List<Device>>
    suspend fun saveAllDevice(deviceList: List<Device>)
}