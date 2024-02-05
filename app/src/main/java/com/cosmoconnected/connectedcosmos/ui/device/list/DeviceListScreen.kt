package com.cosmoconnected.connectedcosmos.ui.device.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cosmoconnected.connectedcosmos.R
import com.cosmoconnected.connectedcosmos.common.ComposableLifecycle
import com.cosmoconnected.connectedcosmos.domain.model.Device
import design.designsystem.CCAlertText
import design.designsystem.CCBlackText
import design.designsystem.CCSufaceText
import design.designsystem.CCTitleBlackBoldText

@Composable
fun DeviceListScreen(
    modifier: Modifier = Modifier,
    viewModel: DeviceListViewModel = hiltViewModel(),
    onGoToDetailScreen: (Device) -> Unit
) {

    ComposableLifecycle { _, event ->
        when(event){
            Lifecycle.Event.ON_RESUME -> {
                viewModel.getAllDevices()
            }
            Lifecycle.Event.ON_PAUSE-> {
                viewModel.pauseJob()
            }
            Lifecycle.Event.ON_DESTROY -> {
                viewModel.clear()
            }
            else -> { /* No action */ }
        }
    }

    val state by viewModel.deviceListScreenState.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            verticalArrangement= Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ){
            if (state.isConnectedData.not()) {
                CCAlertText(text = stringResource(R.string.disconnected_mode))
                if (state.deviceList.isEmpty()){

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(),
                            painter = painterResource(id = R.drawable.no_wifi),
                            contentDescription = "no data"
                        )
                    }

                }
            }
            if (state.deviceListScreenState == DeviceListScreenState.Error){
                CCSufaceText(
                    modifier = Modifier
                        .padding(),
                    text = stringResource(R.string.generic_error)
                )
            } else {
                LazyColumn {
                    items(items = state.deviceList) {
                        DeviceListItem(device = it) {
                            onGoToDetailScreen(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceListItem(modifier: Modifier = Modifier, device: Device, onItemClicked: () -> Unit) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .clickable {
                onItemClicked()
            }
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
            with(device) {
                CCTitleBlackBoldText(text = macAddress)
                CCBlackText(text = model)
                CCBlackText(text = firmwareVersion)
                product?.let {
                    CCBlackText(text = it)
                }
            }
        }
    }
}

@Preview
@Composable
fun DeviceListItemPreview() {
    DeviceListItem(device = Device.default) { /*No action*/ }
}