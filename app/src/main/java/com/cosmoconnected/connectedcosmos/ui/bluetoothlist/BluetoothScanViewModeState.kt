package com.cosmoconnected.connectedcosmos.ui.bluetoothlist

import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import com.cosmoconnected.connectedcosmos.R
import core.bluetooth.connexion.BluetoothDeviceConnexionResultState

data class BluetoothViewModelUiState(
    val deviceList: List<ScanResult> = arrayListOf(),
    val serviceList: List<BluetoothGattService> = arrayListOf(),
    val bluetoothScanUiState: BluetoothScanUiState = BluetoothScanUiState.Idle,
    val bluetoothDeviceConnexionUiState: BluetoothDeviceConnexionUiState
            = BluetoothDeviceConnexionUiState.Idle
) {
    companion object {
        val initialValue = BluetoothViewModelUiState(
            arrayListOf(),
            arrayListOf(),
            BluetoothScanUiState.Idle,
            BluetoothDeviceConnexionUiState.Idle,
            )
    }
}

sealed class BluetoothScanUiState {
    data object Idle : BluetoothScanUiState()
    data object Error : BluetoothScanUiState()
    data object BLEDisable : BluetoothScanUiState()
    data object Scanning : BluetoothScanUiState()
    data object NewDevice : BluetoothScanUiState()
    data object ErrorScanning : BluetoothScanUiState()
    data object Stop : BluetoothScanUiState()
    data object DeviceDiscover : BluetoothScanUiState()

    fun getMessageId() = when(this){
        Idle -> R.string.push_start_scan
        Error -> R.string.scanning_error_message
        BLEDisable -> R.string.ble_disable_message
        Scanning -> R.string.empty
        NewDevice -> R.string.empty
        ErrorScanning -> R.string.scanning_error_message
        Stop -> R.string.nb_device
        DeviceDiscover -> R.string.nb_device
    }
}

sealed class BluetoothDeviceConnexionUiState{
    data object Idle : BluetoothDeviceConnexionUiState()
    data object Connecting : BluetoothDeviceConnexionUiState()
    data object Connected : BluetoothDeviceConnexionUiState()
    data object OnServiceDiscovered : BluetoothDeviceConnexionUiState()
    data class Error(val errorCode: Int) : BluetoothDeviceConnexionUiState()

    fun getStatusLabelId() = when(this){
        Idle -> R.string.connexion_in_progress
        Connecting -> R.string.connexion_in_progress
        Connected -> R.string.connected
        OnServiceDiscovered -> R.string.service_list
        is Error -> R.string.service_error
    }
}

fun BluetoothDeviceConnexionResultState.toUiState() = when(this){
    BluetoothDeviceConnexionResultState.Connecting -> BluetoothDeviceConnexionUiState.Connecting
    BluetoothDeviceConnexionResultState.Connected ->  BluetoothDeviceConnexionUiState.Connected
    is BluetoothDeviceConnexionResultState.OnServiceDiscovered ->  BluetoothDeviceConnexionUiState
        .OnServiceDiscovered
    is BluetoothDeviceConnexionResultState.Error ->  BluetoothDeviceConnexionUiState.Error(this.errorCode)
}