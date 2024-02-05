package com.cosmoconnected.connectedcosmos.ui.device.detail

import com.cosmoconnected.connectedcosmos.domain.model.Device

sealed class DeviceDetailViewModelUiState{
    data object Loading : DeviceDetailViewModelUiState()
    data class Success(val device: Device) : DeviceDetailViewModelUiState()
    data object Error : DeviceDetailViewModelUiState()
}