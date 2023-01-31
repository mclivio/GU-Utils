package com.donc.gu_utils.repository.profile

import com.donc.gu_utils.data.models.PlayerRecords
import com.donc.gu_utils.data.remote.ProfileApi
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultProfileRepository @Inject constructor(private val apiService: ProfileApi) :
    ProfileRepository {
    override suspend fun getPlayerProfile(user_id: String): Resource<PlayerRecords> {
        val response = try {
            apiService.getPlayerRecords(user_id)
        } catch (e:Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
}
