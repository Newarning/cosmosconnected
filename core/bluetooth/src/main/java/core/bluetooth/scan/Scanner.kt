package core.bluetooth.scan

import kotlinx.coroutines.flow.Flow

interface Scanner {

    suspend fun startScan()
    fun stopScan()
    suspend fun notify() : Flow<BluetoothScanResultState>
}