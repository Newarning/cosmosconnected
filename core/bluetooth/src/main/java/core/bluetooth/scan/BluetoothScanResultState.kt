package core.bluetooth.scan

import android.bluetooth.le.ScanResult

sealed class BluetoothScanResultState{
    data object Idle : BluetoothScanResultState()
    data object Scanning : BluetoothScanResultState()
    data class NewDevice(val scanResult: ScanResult) : BluetoothScanResultState()
    data object Stop : BluetoothScanResultState()
    data object ErrorScanning : BluetoothScanResultState()
    data object Error : BluetoothScanResultState()
    data object Disable : BluetoothScanResultState()
}