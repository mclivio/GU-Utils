package com.donc.gu_utils.repository.cardsearch

import com.donc.gu_utils.data.models.CardRecords
import com.donc.gu_utils.data.remote.RecordApi
import com.donc.gu_utils.util.Constants.PER_PAGE
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultCardRepository @Inject constructor(private val apiService: RecordApi) :
    CardRepository{
    override suspend fun getAllCards(page: Int): Resource<CardRecords> {
        val response = try {
            apiService.getCardRecords(page, PER_PAGE)
        } catch(e: Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
}
