package com.cosmoconnected.connectedcosmos.data.repository

import app.cash.turbine.test
import com.cosmoconnected.connectedcosmos.data.datasource.DeviceDataSource
import com.cosmoconnected.connectedcosmos.domain.model.Device
import com.google.common.truth.Truth.assertThat
import core.connexion.ConnexionManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DevicesRepositoryTest {
    private lateinit var vivaRepository: DevicesRepository
    private lateinit var localDataSource: DeviceDataSource
    private lateinit var remoteDataSource: DeviceDataSource
    private lateinit var connexionManager: ConnexionManager
    private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        localDataSource = mockk<DeviceDataSource>(relaxed = true)
        remoteDataSource = mockk<DeviceDataSource>(relaxed = true)
        connexionManager = mockk()
        vivaRepository = DeviceRepositoryImpl(connexionManager, localDataSource,  remoteDataSource, dispatcher)
    }

    @Test
    fun `fetch all device with network returns flow list of device`() = runTest {
        val scope= CoroutineScope(dispatcher)
        //given
        val deviceList = listOf( Device.default)
        val networkIsAvailable = true

        //when
        coEvery { remoteDataSource.getAllDevice() } returns flowOf(deviceList)
        coEvery { connexionManager.networkIsAvailable() } returns flowOf(true).stateIn(scope)

        //expect
        vivaRepository.getDeviceList().test {
            assertThat(awaitItem()).isEqualTo(Pair(networkIsAvailable,deviceList))
            cancelAndConsumeRemainingEvents()
            coVerify { localDataSource.saveAllDevice(deviceList) }
        }
    }

    @Test
    fun `fetch all device with no network returns database list of device`() = runTest {
        val scope= CoroutineScope(dispatcher)
        //given
        val networkDeviceList = arrayListOf<Device>()
        val databaseDeviceList = listOf( Device(
            macAddress = "macAddressDatabase",
            model = "model",
            product = "product",
            firmwareVersion = "firmwareVersion",
            serial = "serial",
            installationMode = "installationMode",
            brakeLight = false,
            lightMode = "lightMode",
            lightAuto = false,
            lightValue = 50,
        ))

        val networkIsAvailable = false

        //when
        coEvery { remoteDataSource.getAllDevice() } returns flowOf(networkDeviceList)
        coEvery { localDataSource.getAllDevice() } returns flowOf(databaseDeviceList)
        coEvery { connexionManager.networkIsAvailable() } returns flowOf(networkIsAvailable).stateIn(scope)

        //expect
        vivaRepository.getDeviceList().test {
            assertThat(awaitItem()).isEqualTo(Pair(networkIsAvailable, databaseDeviceList))
            cancelAndConsumeRemainingEvents()
        }
    }

}