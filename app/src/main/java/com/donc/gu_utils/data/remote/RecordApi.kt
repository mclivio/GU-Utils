package com.donc.gu_utils.data.remote

import com.donc.gu_utils.data.models.CardRecords
import retrofit2.http.GET
import retrofit2.http.Query

interface RecordApi {
    //Class used by Retrofit to access API endpoints

    @GET("proto")
    suspend fun getFilteredRecords(
        @Query("page") page: Int,
        @Query("perPage") perPage: Int,
        @Query("god") god: String,
        @Query("mana") mana: Int?,
        @Query("rarity") rarity: String,
        @Query("tribe") tribe: String
    ) : CardRecords
}