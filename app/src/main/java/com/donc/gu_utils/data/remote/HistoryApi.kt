package com.donc.gu_utils.data.remote

import com.donc.gu_utils.data.models.MatchRecords
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoryApi {
    //Class used by Retrofit to access API endpoints

    @GET("match")
    suspend fun getWinningRecords(
        @Query("player_won") player_won: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ) : MatchRecords

    @GET("match")
    suspend fun getLosingRecords(
        @Query("player_lost") player_lost: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
    ) : MatchRecords
}