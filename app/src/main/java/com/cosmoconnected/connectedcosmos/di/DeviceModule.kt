package com.cosmoconnected.connectedcosmos.di

import com.cosmoconnected.connectedcosmos.data.repository.DevicesRepository
import com.cosmoconnected.connectedcosmos.data.datasource.DatabaseDeviceDatSource
import com.cosmoconnected.connectedcosmos.data.datasource.DeviceDataSource
import com.cosmoconnected.connectedcosmos.data.datasource.RemoteDeviceDatasource
import com.cosmoconnected.connectedcosmos.data.repository.DeviceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Singleton
    @Binds
    abstract fun bindRepository(deviceRepositoryImpl: DeviceRepositoryImpl): DevicesRepository

    @Remote
    @Singleton
    @Binds
    abstract fun bindRemoteDataSource(remoteDataSource: RemoteDeviceDatasource): DeviceDataSource

    @Local
    @Singleton
    @Binds
    abstract fun bindDatabaseDataSource(localDataSource: DatabaseDeviceDatSource): DeviceDataSource


}