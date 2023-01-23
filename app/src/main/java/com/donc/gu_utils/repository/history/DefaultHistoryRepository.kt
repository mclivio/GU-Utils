package com.donc.gu_utils.repository.history

import com.donc.gu_utils.data.models.MatchRecords
import com.donc.gu_utils.data.remote.HistoryApi
import com.donc.gu_utils.util.Constants
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultHistoryRepository @Inject constructor(private val apiService: HistoryApi) :
    HistoryRepository {
    override suspend fun getWinningMatches(player_won: String): Resource<MatchRecords> {
        val response = try {
            apiService.getWinningRecords(player_won, Constants.PER_PAGE)
        } catch (e:Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
    override suspend fun getLosingMatches(player_lost: String): Resource<MatchRecords> {
        val response = try {
            apiService.getLosingRecords(player_lost, Constants.PER_PAGE)
        } catch (e:Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
}