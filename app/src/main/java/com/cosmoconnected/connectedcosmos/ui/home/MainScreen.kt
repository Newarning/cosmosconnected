package com.cosmoconnected.connectedcosmos.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cosmoconnected.connectedcosmos.ui.bluetoothlist.BluetoothScanScreen
import com.cosmoconnected.connectedcosmos.ui.navigation.GeneralScreens
import com.cosmoconnected.connectedcosmos.ui.device.detail.DevicesDetailsScreen
import com.cosmoconnected.connectedcosmos.ui.device.list.DeviceListScreen
import design.designsystem.theme.ConnectedCosmosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    var currentPage by remember { mutableStateOf <GeneralScreens>(
        GeneralScreens.Devices.DevicesList
    ) }
    var isBottomBarVisible by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ConnectedCosmosTheme.colors.primaryContainer,
                    titleContentColor = ConnectedCosmosTheme.colors.onPrimaryContainer,
                ),
                title = {
                    Text(stringResource(id = currentPage.titleId))
                },
                navigationIcon = {
                    if(currentPage.needToShowBackIcon) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                tint = ConnectedCosmosTheme.colors.primary,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier,
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    GeneralScreens.toBottomNavigationItems().forEach { navigationItem ->
                        NavigationBarItem(
                            icon = { Icon(painterResource(id = navigationItem.imageResourceId),
                                         contentDescription = navigationItem.route)
                                   },
                            label = { Text(stringResource(navigationItem.labelId)) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == navigationItem.route } == true,
                            onClick = {
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = GeneralScreens.Devices.DevicesList.route,
            Modifier.padding(innerPadding)
        ) {
            composable(GeneralScreens.Devices.DevicesList.route) {
                currentPage = GeneralScreens.Devices.DevicesList
                isBottomBarVisible = currentPage.bottomBarIsVisible
                DeviceListScreen {
                    navController.navigate(
                        GeneralScreens.Devices.DevicesDetails.route + "/${it.macAddress}"
                    )
                }
            }

            composable(route = GeneralScreens.Devices.DevicesDetails.route + "/{macAddress}",
                arguments = listOf(navArgument("macAddress") { type = NavType.StringType })
            ) {
                currentPage = GeneralScreens.Devices.DevicesDetails
                isBottomBarVisible = currentPage.bottomBarIsVisible
                val macAddress = it.arguments?.getString("macAddress")
                DevicesDetailsScreen(macAddress = macAddress ?: "")
            }

            composable(GeneralScreens.Bluetooth.route) {
                currentPage = GeneralScreens.Bluetooth
                isBottomBarVisible = currentPage.bottomBarIsVisible
                BluetoothScanScreen()
            }


        }
    }
}