package com.cosmoconnected.connectedcosmos.ui.bluetoothlist

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.bluetooth.connexion.BluetoothDeviceConnexionManager
import core.bluetooth.connexion.BluetoothDeviceConnexionResultState
import core.bluetooth.scan.BluetoothScanManager
import core.bluetooth.scan.BluetoothScanResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val scanner: BluetoothScanManager,
    private val bluetoothDeviceConnexionManager: BluetoothDeviceConnexionManager
) : ViewModel() {

    private var currentDeviceDiscovered: HashMap<String, ScanResult> = HashMap()
    private var currentState = BluetoothViewModelUiState.initialValue

    private val _observeDevicesScan = MutableStateFlow(currentState)
    val observeDevicesScan = _observeDevicesScan.asStateFlow()

    private var scanJob: Job? = null
    private var deviceJob: Job? = null

    fun startScan() {
        currentState = currentState.copy(bluetoothScanUiState = BluetoothScanUiState.Scanning)
        update()
        currentDeviceDiscovered = hashMapOf()
        viewModelScope.launch {
            scanner.startScan()
            delay(15_000)
            stopScan()
        }
    }
    fun resetState(){
        currentState = currentState.copy(
            bluetoothScanUiState = BluetoothScanUiState.Idle)
    }

    fun stopScan() {
        currentState = currentState.copy(
            deviceList = getFinalDeviceList(),
            bluetoothScanUiState = BluetoothScanUiState.Stop)
        update()
        scanner.stopScan()
        scanJob?.cancelChildren()
    }

    fun observeNewDevice() {
        scanJob = viewModelScope.launch {
                scanner.notify()
                    .catch {
                        currentState = currentState.copy(bluetoothScanUiState = BluetoothScanUiState.Error)
                        update()
                    }
                    .onEach {
                        manageDataFromScanner(it)
                    }
                    .collect()
        }
    }

    fun getDeviceData(device: BluetoothDevice){
        stopScan()
        bluetoothDeviceConnexionManager.connect(device)
        deviceJob = viewModelScope.launch {
            bluetoothDeviceConnexionManager.notify()
                .catch {
                    currentState = currentState.copy(
                        deviceList = getFinalDeviceList(),
                        bluetoothDeviceConnexionUiState = BluetoothDeviceConnexionUiState.Error(-1))
                    update()
                }
                .onEach {
                    currentState = currentState.copy(
                        deviceList = getFinalDeviceList(),
                        bluetoothDeviceConnexionUiState = it.toUiState())
                    if(it is BluetoothDeviceConnexionResultState.OnServiceDiscovered){
                        currentState = currentState.copy(
                            serviceList = it.servicesList)
                    }
                    update()
                }
                .collect()
        }
    }

    fun stopGetDeviceData() {
        bluetoothDeviceConnexionManager.disconnect()
        deviceJob?.cancelChildren()
        currentState = currentState.copy(
            serviceList = arrayListOf(),
            bluetoothDeviceConnexionUiState = BluetoothDeviceConnexionUiState.Idle)
    }

    private fun manageDataFromScanner(bluetoothScanResultState: BluetoothScanResultState) {
        when (bluetoothScanResultState) {
            is BluetoothScanResultState.Idle -> {
                currentState = currentState.copy(
                    bluetoothScanUiState = BluetoothScanUiState.Idle
                )
            }
            is BluetoothScanResultState.Scanning -> {
                currentState = currentState.copy(
                    deviceList = getFinalDeviceList() ,
                    bluetoothScanUiState = BluetoothScanUiState.Scanning
                )
            }
            is BluetoothScanResultState.NewDevice -> {

                currentDeviceDiscovered[bluetoothScanResultState.scanResult.device.address] =
                    bluetoothScanResultState.scanResult
                currentState = currentState.copy(
                    deviceList = getFinalDeviceList() ,
                    bluetoothScanUiState = BluetoothScanUiState.NewDevice
                )

            }
            is BluetoothScanResultState.ErrorScanning -> {
                currentState = currentState.copy(
                    deviceList = getFinalDeviceList() ,
                    bluetoothScanUiState = BluetoothScanUiState.ErrorScanning
                )
            }
            is BluetoothScanResultState.Error -> {
                currentState = currentState.copy(
                    bluetoothScanUiState = BluetoothScanUiState.Error
                )
            }
            is BluetoothScanResultState.Stop -> {
                currentState = currentState.copy(
                    deviceList = getFinalDeviceList() ,
                    bluetoothScanUiState = BluetoothScanUiState.Stop
                )
            }
            is BluetoothScanResultState.Disable -> {
                currentState = currentState.copy(
                    bluetoothScanUiState = BluetoothScanUiState. BLEDisable
                )
            }
        }
        update()
    }

    private fun update() {
        _observeDevicesScan.value = currentState
    }

    private fun getFinalDeviceList() = currentDeviceDiscovered.values.toList().sortedBy { it.rssi }

}