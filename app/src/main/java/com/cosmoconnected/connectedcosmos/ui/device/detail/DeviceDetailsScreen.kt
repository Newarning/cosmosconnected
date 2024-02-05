package com.cosmoconnected.connectedcosmos.ui.device.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cosmoconnected.connectedcosmos.R
import com.cosmoconnected.connectedcosmos.domain.model.Device
import design.designsystem.CCSufaceText
import design.designsystem.CCText
import design.designsystem.CCTitleBoldText
import design.designsystem.theme.ConnectedCosmosTheme

@Composable
fun DevicesDetailsScreen(
    modifier: Modifier = Modifier,
    macAddress: String,
    viewModel: DeviceDetailViewModel = hiltViewModel(),
) {

    val deviceDetailViewModelUiState by viewModel.selectedDeviceFlow(macAddress).collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when(deviceDetailViewModelUiState) {
            is DeviceDetailViewModelUiState.Loading -> { DevicesDetailsLoadingScreen() }
            is DeviceDetailViewModelUiState.Success -> {
                DevicesDetailsSuccessScreen(device = (deviceDetailViewModelUiState as DeviceDetailViewModelUiState.Success).device)
            }
            is DeviceDetailViewModelUiState.Error -> { DevicesDetailsErrorScreen() }
        }
    }
}

@Composable
fun DevicesDetailsLoadingScreen(modifier: Modifier = Modifier){
    Column( modifier = modifier){
        Text(text = stringResource(id = R.string.loading_device_error))
    }
}

@Composable
fun DevicesDetailsErrorScreen(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.ic_warning),
            contentDescription = "Error image"
        )
        Text(text = stringResource(id = R.string.loading_device_error))
    }
}

@Composable
fun DevicesDetailsSuccessScreen(modifier: Modifier = Modifier, device: Device){
    Column(
        modifier = modifier
            .padding(horizontal = ConnectedCosmosTheme.paddings.defaultPadding)
            .padding(top = ConnectedCosmosTheme.paddings.defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally) {
        with(device){
            CCSufaceText(text = "Model: $model")
            product?.let {
                CCSufaceText(text = "Product : $it")
            }
            CCSufaceText(text = "Mac address: $macAddress")
            CCSufaceText(text = "Firmware version address: $firmwareVersion")
            serial?.let {
                CCText(text = "Serial: $it")
            }
            installationMode?.let {
                CCSufaceText(text = "Installation mode: $it")
            }
            lightMode?.let {
                CCSufaceText(text = "Light moder: $it")
            }
            CCSufaceText(text = "Light value: $lightValue")
        }
    }
}

@Preview
@Composable
fun DevicesDetailsSuccessScreenPreview() {
    DevicesDetailsSuccessScreen(device = Device.default)
}

@Preview
@Composable
fun DevicesDetailsLoadingScreenPreview() {
    DevicesDetailsLoadingScreen()
}

@Preview
@Composable
fun DevicesDetailsErrorScreenPreview() {
    DevicesDetailsErrorScreen()
}