package com.cosmoconnected.connectedcosmos.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DeviceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun devicesDao(): DeviceDao

    companion object {
         const val databaseName = "connected_cosmos_database"
    }
}