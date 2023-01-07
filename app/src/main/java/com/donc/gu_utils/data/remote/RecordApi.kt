package com.donc.gu_utils.data.remote

import com.donc.gu_utils.data.models.CardRecords
import retrofit2.http.GET
import retrofit2.http.Query

interface RecordApi {
    //Clase Usada por Retrofit para acceder a las rutas de la API

    @GET("")
    suspend fun getCardRecords(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ): CardRecords
}