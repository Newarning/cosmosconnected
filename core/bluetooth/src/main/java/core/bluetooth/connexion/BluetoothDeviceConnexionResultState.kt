package core.bluetooth.connexion

import android.bluetooth.BluetoothGattService

sealed class BluetoothDeviceConnexionResultState{
    data object Connecting : BluetoothDeviceConnexionResultState()
    data object Connected : BluetoothDeviceConnexionResultState()
    data class OnServiceDiscovered (val servicesList : List<BluetoothGattService>) :
        BluetoothDeviceConnexionResultState()
    data class Error(var errorCode : Int) : BluetoothDeviceConnexionResultState()

}