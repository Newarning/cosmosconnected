package core.bluetooth.di

import core.bluetooth.connexion.BluetoothDeviceConnexion
import core.bluetooth.connexion.BluetoothDeviceConnexionManager
import core.bluetooth.scan.BluetoothScanManager
import core.bluetooth.scan.Scanner
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BluetoothModule {

    @Singleton
    @Binds
    abstract fun bindScanner(bluetoothScanManager: BluetoothScanManager): Scanner

    @Singleton
    @Binds
    abstract fun bindBluetoothDeviceConnexionManager(
        bluetoothDeviceConnexionManager: BluetoothDeviceConnexionManager
    ): BluetoothDeviceConnexion
}