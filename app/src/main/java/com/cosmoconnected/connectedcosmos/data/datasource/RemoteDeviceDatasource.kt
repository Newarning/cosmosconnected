package com.cosmoconnected.connectedcosmos.data.datasource

import com.cosmoconnected.connectedcosmos.data.network.ConnectedCosmosApiService
import com.cosmoconnected.connectedcosmos.data.toDevice
import com.cosmoconnected.connectedcosmos.domain.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDeviceDatasource @Inject constructor(
    private var connectedCosmosApiService: ConnectedCosmosApiService
): DeviceDataSource {

    override suspend fun getAllDevice(): Flow<List<Device>>{
        return flow {
            emit(connectedCosmosApiService.getAll().devices.map { it.toDevice() })
        }
    }
    override suspend fun saveAllDevice(deviceList: List<Device>) {}
}