package com.cosmoconnected.connectedcosmos.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface DeviceDao {

    @Query("SELECT * FROM devices")
    suspend fun getAll(): List<DeviceEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( listingList: List<DeviceEntity>)

    @Query("DELETE FROM devices")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshData(listingList: List<DeviceEntity>) {
        deleteAll()
        insertAll(listingList)
    }
}