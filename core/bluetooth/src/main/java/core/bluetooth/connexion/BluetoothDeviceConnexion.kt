package core.bluetooth.connexion

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothDeviceConnexion {
    fun connect(bluetoothDevice: BluetoothDevice)
    fun disconnect()
    fun notify() : Flow<BluetoothDeviceConnexionResultState>
}