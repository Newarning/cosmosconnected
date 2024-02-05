package com.cosmoconnected.connectedcosmos.data.repository

import com.cosmoconnected.connectedcosmos.data.datasource.DeviceDataSource
import com.cosmoconnected.connectedcosmos.di.IoDispatcher
import com.cosmoconnected.connectedcosmos.di.Local
import com.cosmoconnected.connectedcosmos.di.Remote
import com.cosmoconnected.connectedcosmos.domain.model.Device
import core.connexion.ConnexionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val connexionManager: ConnexionManager,
    @Local private val databaseDeviceDatSource: DeviceDataSource,
    @Remote private val remoteDeviceDatasource: DeviceDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): DevicesRepository {

    private val coroutineScope = CoroutineScope(ioDispatcher)
    private val _currentDeviceList = MutableStateFlow<List<Device>>(arrayListOf())

    private val currentDeviceList: StateFlow<List<Device>> =
        _currentDeviceList.stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = arrayListOf()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getDeviceList(): Flow<Pair<Boolean, List<Device>>> {
        return connexionManager.networkIsAvailable()
            .flatMapConcat { networkIsAvailable ->
                if(networkIsAvailable){
                    remoteDeviceDatasource.getAllDevice()
                        .map { deviceList ->
                            _currentDeviceList.update { deviceList }
                            databaseDeviceDatSource.saveAllDevice(deviceList)
                            Pair(true, deviceList)
                        }
                } else {
                    databaseDeviceDatSource.getAllDevice()
                        .map { deviceList ->
                            _currentDeviceList.update { deviceList }
                            Pair(false, deviceList )
                        }
                }
            }
    }

    override fun getSelectedDeviceForMacAddress(macAddress: String) =  currentDeviceList
        .map {
            it.firstOrNull { device -> device.macAddress == macAddress }
        }
}