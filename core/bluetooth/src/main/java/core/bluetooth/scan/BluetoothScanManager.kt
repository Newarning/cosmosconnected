package core.bluetooth.scan

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BluetoothScanManager @Inject constructor(
    context: Context
) : Scanner {

    private val bluetoothManager : BluetoothManager? = context
        .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

    private val adapter: BluetoothAdapter?
        get() = bluetoothManager?.adapter

    private val scanner: BluetoothLeScanner?
        get() = adapter?.bluetoothLeScanner

    private val bluetoothScanResultStateChannel = Channel<BluetoothScanResultState>(UNLIMITED)

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if(result != null){
                updateWith( BluetoothScanResultState.NewDevice(result) )
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            updateWith( BluetoothScanResultState.ErrorScanning )
        }
    }

    private fun updateWith(bluetoothScanResultState: BluetoothScanResultState){
        bluetoothScanResultStateChannel.trySend(bluetoothScanResultState)
    }

    override suspend fun startScan() {
        val scanSettings = buildScanSettings()
        if (adapter != null && adapter!!.isEnabled) {
            if (scanner != null) {
                scanner!!.startScan(arrayListOf(), scanSettings, scanCallback)
                updateWith( BluetoothScanResultState.Scanning )
            } else {
                updateWith( BluetoothScanResultState.Error )
            }
        } else {
            updateWith( BluetoothScanResultState.Disable )
        }
    }

    override fun stopScan() {
        if(scanner != null) {
            scanner!!.stopScan(scanCallback)
        }
    }

    override suspend fun notify() = bluetoothScanResultStateChannel.consumeAsFlow()

    private fun buildScanSettings(): ScanSettings {
        return ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()
    }
}