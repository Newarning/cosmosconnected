package core.bluetooth.connexion

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattService
import android.content.Context
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BluetoothDeviceConnexionManager @Inject constructor(
    private val context: Context
): BluetoothDeviceConnexion {

    private var bluetoothGatt: BluetoothGatt? = null
    private var services: List<BluetoothGattService> = emptyList()

    private val bluetoothDeviceConnexionResultStateChannel = Channel<BluetoothDeviceConnexionResultState>(10)

    private val callback = object: BluetoothGattCallback() {

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            services = gatt.services
            bluetoothDeviceConnexionResultStateChannel.
            trySend(BluetoothDeviceConnexionResultState.OnServiceDiscovered(gatt.services))
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                bluetoothDeviceConnexionResultStateChannel.trySend(BluetoothDeviceConnexionResultState.Error(status))
                return
            }

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                bluetoothDeviceConnexionResultStateChannel
                    .trySend(BluetoothDeviceConnexionResultState.Connected)
                discoverServices()
            }
        }
    }

    override fun connect(bluetoothDevice: BluetoothDevice) {
        bluetoothDeviceConnexionResultStateChannel.trySend(BluetoothDeviceConnexionResultState.Connecting)
        bluetoothGatt = bluetoothDevice.connectGatt(context, false, callback)
    }

    override fun notify() = bluetoothDeviceConnexionResultStateChannel.consumeAsFlow()

    override fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    private fun discoverServices() {
        bluetoothGatt?.discoverServices()
    }


}