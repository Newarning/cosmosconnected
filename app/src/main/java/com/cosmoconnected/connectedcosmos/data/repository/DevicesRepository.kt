package com.cosmoconnected.connectedcosmos.data.repository

import com.cosmoconnected.connectedcosmos.domain.model.Device
import kotlinx.coroutines.flow.Flow

interface DevicesRepository {

    suspend fun getDeviceList(): Flow<Pair<Boolean, List<Device>>>
    fun getSelectedDeviceForMacAddress(macAddress: String):  Flow<Device?>
}