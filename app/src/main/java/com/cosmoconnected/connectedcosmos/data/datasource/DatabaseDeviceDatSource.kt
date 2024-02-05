package com.cosmoconnected.connectedcosmos.data.datasource

import com.cosmoconnected.connectedcosmos.data.database.AppDatabase
import com.cosmoconnected.connectedcosmos.data.toDevice
import com.cosmoconnected.connectedcosmos.data.toDeviceEntity
import com.cosmoconnected.connectedcosmos.domain.model.Device
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DatabaseDeviceDatSource @Inject constructor(
    private val appDatabase: AppDatabase
): DeviceDataSource {

    override suspend fun getAllDevice(): Flow<List<Device>>{
        return flow {
            emit(appDatabase.devicesDao().getAll().map { it.toDevice() })
        }
    }

    override suspend fun saveAllDevice(deviceList: List<Device>) {
        appDatabase.devicesDao().insertAll(deviceList.map { it.toDeviceEntity() })
    }
}