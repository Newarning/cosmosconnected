package com.cosmoconnected.connectedcosmos.ui.device.list

import com.cosmoconnected.connectedcosmos.domain.model.Device

data class DeviceListState (
    val deviceListScreenState: DeviceListScreenState,
    val isConnectedData: Boolean,
    val deviceList : List<Device>
){
    companion object {
        val initialState = DeviceListState(DeviceListScreenState.Loading, false, arrayListOf())
    }
}

sealed class DeviceListScreenState {
    data object Success : DeviceListScreenState()
    data object Error : DeviceListScreenState()
    data object Loading : DeviceListScreenState()
}