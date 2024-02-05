package com.cosmoconnected.connectedcosmos.ui.navigation

import com.cosmoconnected.connectedcosmos.R
import com.cosmoconnected.connectedcosmos.ui.home.BottomNavigationItem

sealed class GeneralScreens(
    val titleId: Int,
    val imageResourceId: Int,
    val route: String,
    val bottomBarIsVisible: Boolean,
    val needToShowBackIcon: Boolean,
) {
    data object Bluetooth : GeneralScreens(
        titleId = R.string.bluetooph_tile,
        imageResourceId= R.drawable.ic_bluetooth,
        route = "Bluetooth",
        bottomBarIsVisible = true,
        needToShowBackIcon = false)

    sealed class Devices {
        data object DevicesList : GeneralScreens(
            titleId = R.string.device_tile,
            imageResourceId = R.drawable.ic_home,
            route = "device_list",
            bottomBarIsVisible = true,
            needToShowBackIcon = false
        )

        data object DevicesDetails : GeneralScreens(
            titleId = R.string.device_details,
            imageResourceId = R.drawable.ic_home,
            route = "device_details",
            bottomBarIsVisible = false,
            needToShowBackIcon = true
        )
    }

    companion object {
        fun toBottomNavigationItems() : List<BottomNavigationItem> {
            return listOf(Devices.DevicesList, Bluetooth).map {
                BottomNavigationItem(
                    labelId = it.titleId,
                    imageResourceId = it.imageResourceId,
                    route = it.route
                )
            }
        }
    }
}
