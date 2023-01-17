package com.donc.gu_utils.repository.cardsearch

import android.util.Log
import com.donc.gu_utils.data.models.CardRecords
import com.donc.gu_utils.data.remote.RecordApi
import com.donc.gu_utils.util.Constants.PER_PAGE
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class DefaultCardRepository @Inject constructor(private val apiService: RecordApi) :
    CardRepository{
    override suspend fun getFilteredCards(
        page: Int,
        god: String,
        mana: String,
        rarity: String,
        tribe: String
    ): Resource<CardRecords> {
        var manaFilter = if(mana.isEmpty()) null else mana.toInt()
        val response = try {
            apiService.getFilteredRecords(page, PER_PAGE, god, manaFilter, rarity, tribe)
        } catch (e:Exception) {
            return Resource.Error("Ha ocurrido un error inesperado. Intente nuevamente.")
        }
        return Resource.Success(response)
    }
}
