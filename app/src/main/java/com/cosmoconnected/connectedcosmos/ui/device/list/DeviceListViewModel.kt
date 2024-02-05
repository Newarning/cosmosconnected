package com.cosmoconnected.connectedcosmos.ui.device.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmoconnected.connectedcosmos.data.repository.DevicesRepository
import core.connexion.NetworkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeviceListViewModel @Inject constructor(
    private val devicesRepository: DevicesRepository,
    private val networkManager: NetworkManager
):ViewModel() {

    private var collectNetworkJob: Job? = null
    private var collectJob: Job? = null
    private val _deviceListScreenState= MutableStateFlow(DeviceListState.initialState)
    val deviceListScreenState = _deviceListScreenState.asStateFlow()

    fun getAllDevices(){
        starCollectNetworkStatus()
        collectJob =viewModelScope.launch {
            devicesRepository.getDeviceList()
                .catch {
                    _deviceListScreenState.value = _deviceListScreenState.value
                        .copy(deviceListScreenState = DeviceListScreenState.Error)
                }
                .collect {
                    _deviceListScreenState.value = _deviceListScreenState.value.copy(
                        deviceListScreenState = DeviceListScreenState.Success,
                        isConnectedData = it.first,
                        deviceList = it.second
                    )
                }
        }
    }

    private fun starCollectNetworkStatus(){
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.networkIsAvailable().collect { isOnline ->
                when(isOnline){
                    true -> _deviceListScreenState.value = _deviceListScreenState.value.copy(
                        isConnectedData = true,
                    )
                    false -> _deviceListScreenState.value = _deviceListScreenState.value.copy(
                        isConnectedData = false,
                    )
                }
            }
        }
    }

    fun pauseJob() {
        collectNetworkJob?.cancelChildren()
    }

    fun clear() {
        collectNetworkJob?.cancel()
        collectNetworkJob?.cancel()
    }
}
