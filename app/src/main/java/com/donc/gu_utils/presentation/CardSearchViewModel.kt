package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.ChipWithList
import com.donc.gu_utils.data.models.Record
import com.donc.gu_utils.repository.cardsearch.CardRepository
import com.donc.gu_utils.util.Constants.PER_PAGE
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    private var page = 1
    var cardRecords = mutableStateOf<List<Record>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    val selectedItems = mutableStateListOf<ChipWithList>()

    private var cachedRecordsList = listOf<Record>()
    private var isSearchStarting = true //if search field is empty
    var isSearching = mutableStateOf(false) //if we are showing search results

    init {
        loadCardsPaginated()
    }

    fun loadCardsPaginated(){
        viewModelScope.launch{
            isLoading.value = true
            when(val result = repository.getAllCards(page)){
                is Resource.Success -> {
                    endReached.value = page >= result.data!!.total/PER_PAGE
                    val cardsEntries = result.data.records.mapIndexed{ _, record ->
                        Record(
                            record.art_id,
                            record.attack,
                            record.collectable,
                            record.effect,
                            record.god,
                            record.health,
                            record.id,
                            record.lib_id,
                            record.live,
                            record.mana,
                            record.name,
                            record.rarity,
                            record.set,
                            record.tribe,
                            record.type
                        )
                    }
                    page++
                    loadError.value = ""
                    isLoading.value = false
                    cardRecords.value += cardsEntries
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> throw java.lang.IllegalArgumentException("Illegal Result")
            }
        }
    }

    fun searchCardRecords(){
        //TODO: Implementation
    }
}