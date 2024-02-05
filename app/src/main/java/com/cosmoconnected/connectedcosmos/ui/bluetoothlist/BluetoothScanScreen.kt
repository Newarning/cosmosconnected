package com.cosmoconnected.connectedcosmos.ui.bluetoothlist

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattService
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cosmoconnected.connectedcosmos.R
import com.cosmoconnected.connectedcosmos.common.ComposableLifecycle
import com.cosmoconnected.connectedcosmos.domain.model.Device
import com.cosmoconnected.connectedcosmos.ui.device.list.DeviceListItem
import core.bluetooth.permissions.ALL_BLE_PERMISSIONS
import design.designsystem.CCBlackText
import design.designsystem.CCSufaceText
import design.designsystem.CCText
import design.designsystem.CCTitleBlackBoldText
import design.designsystem.animation.CCLottieAnimation
import design.designsystem.component.CCButton
import design.designsystem.component.CCDialog
import design.designsystem.theme.ConnectedCosmosTheme

@Composable
fun BluetoothScanScreen(
    modifier: Modifier = Modifier,
    viewModel: BluetoothViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    var userHaseBlePermission by remember { mutableStateOf(false) }
    var bleIsActivated by remember { mutableStateOf(true) }
    var openAlertDialog by remember { mutableStateOf(false) }
    var deviceBottomSheet by remember { mutableStateOf(false) }

    val launcherForPermissions = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        userHaseBlePermission = granted.values.all { it }
    }

    val launcherForSetting =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode != Activity.RESULT_OK) {
                openAlertDialog = true
            } else {
                viewModel.startScan()
                bleIsActivated = true
            }
        }
    val bluetoothDeviceUiState by viewModel.observeDevicesScan.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = bluetoothDeviceUiState.bluetoothScanUiState){
            if(bluetoothDeviceUiState.bluetoothScanUiState == BluetoothScanUiState.BLEDisable) {
                openAlertDialog = true
            }
    }

    LaunchedEffect(key1 = bluetoothDeviceUiState.bluetoothDeviceConnexionUiState){
        if(bluetoothDeviceUiState.bluetoothDeviceConnexionUiState != BluetoothDeviceConnexionUiState.Idle) {
            deviceBottomSheet = true
        }
    }

    ComposableLifecycle { _, event ->
        when(event){

            Lifecycle.Event.ON_RESUME -> {
                //bleIsActivated = viewModel.getIfBluetoothIsActivate()
                viewModel.observeNewDevice()
                val userHaveBlePermissions = ALL_BLE_PERMISSIONS
                    .all { context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED }

                if(userHaveBlePermissions.not()){
                    launcherForPermissions.launch(ALL_BLE_PERMISSIONS)
                }
            }

            Lifecycle.Event.ON_PAUSE -> {
                viewModel.stopScan()
            }
            else -> { /* No action */ }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(ConnectedCosmosTheme.colors.primaryContainer)
            ) {

                when (bluetoothDeviceUiState.bluetoothScanUiState) {
                    is BluetoothScanUiState.Scanning,
                    is BluetoothScanUiState.NewDevice -> {
                        CCLottieAnimation(assetTitle = "bluetooth.json")
                    } else -> { /* No action */}
                }

                CCText(
                    modifier = Modifier
                        .padding(ConnectedCosmosTheme.paddings.defaultPadding)
                        .align(Alignment.Center),
                    text = stringResource(bluetoothDeviceUiState
                        .bluetoothScanUiState.getMessageId())
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .width(250.dp)
                        .padding(bottom = ConnectedCosmosTheme.paddings.defaultPadding)
                        .align(Alignment.BottomCenter)
                ) {
                    CCButton(title = stringResource(R.string.start_scan)) {
                        if(userHaseBlePermission && bleIsActivated ){
                            viewModel.startScan()
                        } else if(userHaseBlePermission.not())
                            launcherForPermissions.launch(ALL_BLE_PERMISSIONS)
                        else {
                            openAlertDialog = true
                        }
                    }
                    CCButton(title = stringResource(R.string.stop_scan)) {
                        viewModel.stopScan()
                    }
                }
            }

            when( bluetoothDeviceUiState.bluetoothScanUiState ){
                is  BluetoothScanUiState.Scanning,
                is BluetoothScanUiState.NewDevice,
                is BluetoothScanUiState.ErrorScanning,
                is BluetoothScanUiState.Stop,
                is  BluetoothScanUiState.DeviceDiscover -> {
                    LazyColumn {
                        items(items = bluetoothDeviceUiState.deviceList){
                            ScanResultItem(scanResult = it){
                                viewModel.getDeviceData(it.device)
                            }
                        }
                    }
                }
                else -> { /* No action */}
            }


            if (openAlertDialog) {
                CCDialog(
                    onDismissRequest = {
                        openAlertDialog = false
                        bleIsActivated = false
                        viewModel.resetState()
                    },
                    onConfirmation = {
                        openAlertDialog = false
                        launcherForSetting.launch(
                            Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        )
                    },
                    dialogTitle = "Bluetooth",
                    dialogText = stringResource(R.string.ble_disable_message),
                    confirmText = stringResource(R.string.activate),
                    dismissText = stringResource(R.string.close)
                )
            }

            if (deviceBottomSheet) {
                DeviceBottomSheet(
                    bluetoothDeviceConnexionUiState = bluetoothDeviceUiState.bluetoothDeviceConnexionUiState,
                    serviceList = bluetoothDeviceUiState.serviceList
                ) {
                    viewModel.stopGetDeviceData()
                    deviceBottomSheet = false
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun ScanResultItem(
    modifier: Modifier = Modifier,
    scanResult: ScanResult,
    onItemClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                with(scanResult) {
                    CCTitleBlackBoldText(
                        text = scanResult.device.name ?: stringResource(R.string.no_name)
                    )
                    CCBlackText(text = scanResult.device.address ?: "")
                    CCBlackText(text = scanResult.rssi.toString())
                }
            }
            CCButton(title = stringResource(R.string.se_connecter)) {
                onItemClicked()
            }
        }
    }
}

@Composable
fun ServiceItem(
    modifier: Modifier = Modifier,
    bluetoothGattService: BluetoothGattService
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.White
            ),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            with(bluetoothGattService){
                CCTitleBlackBoldText(text = "Service type : $type")
                if(characteristics.isNullOrEmpty().not()){
                    characteristics.forEach{
                        CCBlackText(text = "Characteristics : $it.uuid")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceBottomSheet(
    modifier: Modifier = Modifier,
    bluetoothDeviceConnexionUiState: BluetoothDeviceConnexionUiState,
    serviceList: List<BluetoothGattService>,
    onDismissRequest: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier= Modifier.defaultMinSize(minHeight = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CCSufaceText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ConnectedCosmosTheme.paddings.defaultPadding),
                text = stringResource(id = if(
                    serviceList.isEmpty())
                    bluetoothDeviceConnexionUiState.getStatusLabelId() else R.string.service_list)
            )
            if(bluetoothDeviceConnexionUiState is BluetoothDeviceConnexionUiState.Error){
                CCSufaceText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = ConnectedCosmosTheme.paddings.defaultPadding),
                    text = "Error code ${bluetoothDeviceConnexionUiState.errorCode}"
                )
            }

            LazyColumn {
                items(items = serviceList) {
                    ServiceItem(bluetoothGattService = it)
                }
            }
            Spacer(modifier = Modifier.height(0.5.dp))
        }
    }
}

@Preview
@Composable
fun DeviceListItemPreview() {
    DeviceListItem (device = Device.default){ /*No action*/}
}
