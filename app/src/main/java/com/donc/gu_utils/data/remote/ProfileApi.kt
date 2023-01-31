package com.donc.gu_utils.data.remote

import com.donc.gu_utils.data.models.PlayerRecords
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApi {
    //Class used by Retrofit to access API endpoints

    @GET("properties")
    suspend fun getPlayerRecords(
        @Query("user_id") user_id: String
    ) : PlayerRecords
}