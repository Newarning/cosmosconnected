package com.cosmoconnected.connectedcosmos.ui.device.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmoconnected.connectedcosmos.data.repository.DevicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviceDetailViewModel @Inject constructor(
    private val devicesRepository: DevicesRepository
):ViewModel() {

    fun  selectedDeviceFlow(macAddress: String): StateFlow<DeviceDetailViewModelUiState> = devicesRepository
        .getSelectedDeviceForMacAddress(macAddress)
        .map {
            if(it != null) {
                DeviceDetailViewModelUiState.Success(it)
            } else {
                DeviceDetailViewModelUiState.Error
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = DeviceDetailViewModelUiState.Loading,
            started = SharingStarted.WhileSubscribed(5_000)
        )
}

