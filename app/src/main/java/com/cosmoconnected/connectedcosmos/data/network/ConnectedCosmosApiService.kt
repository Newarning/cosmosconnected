package com.cosmoconnected.connectedcosmos.data.network

import retrofit2.http.GET

interface ConnectedCosmosApiService {

    @GET("test/devices")
    suspend fun getAll(): GetAllResponse
}