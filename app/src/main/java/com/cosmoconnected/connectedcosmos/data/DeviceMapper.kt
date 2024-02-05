package com.cosmoconnected.connectedcosmos.data

import com.cosmoconnected.connectedcosmos.data.database.DeviceEntity
import com.cosmoconnected.connectedcosmos.data.network.DeviceDto
import com.cosmoconnected.connectedcosmos.domain.model.Device

internal fun DeviceEntity.toDevice()= Device(
    macAddress = macAddress,
    model = model,
    product =  product,
    firmwareVersion= firmwareVersion,
    serial = serial,
    installationMode = installationMode,
    brakeLight = brakeLight,
    lightMode = lightMode,
    lightAuto = lightAuto,
    lightValue =lightValue)




internal fun DeviceDto.toDevice()= Device(
    macAddress = macAddress,
    model = model,
    product =  product,
    firmwareVersion= firmwareVersion,
    serial = serial,
    installationMode = installationMode,
    brakeLight = brakeLight,
    lightMode = lightMode,
    lightAuto = lightAuto,
    lightValue =lightValue)

internal fun Device.toDeviceEntity()= DeviceEntity(0,
    macAddress = macAddress,
    model = model,
    product =  product,
    firmwareVersion= firmwareVersion,
    serial = serial,
    installationMode = installationMode,
    brakeLight = brakeLight,
    lightMode = lightMode,
    lightAuto = lightAuto,
    lightValue =lightValue)