package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.Deck
import com.donc.gu_utils.data.models.Record
import com.donc.gu_utils.repository.cardsearch.CardRepository
import com.donc.gu_utils.util.Constants.PER_PAGE
import com.donc.gu_utils.util.DeckBuilder
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    var page = 1
    var cardRecords = mutableStateOf<List<Record>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)
    var god = mutableStateOf("")
    var mana = mutableStateOf("")
    var rarity = mutableStateOf("")
    var tribe = mutableStateOf("")
    var cardsAmount = mutableStateOf(0)
    var deck = mutableStateOf(Deck("nature"))
    init {
        val storedDeck: Deck? = DeckBuilder.getDeck()
        if (storedDeck != null) deck.value = storedDeck
        loadCardsPaginated()
    }

    fun loadCardsPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getFilteredCards(
                page,
                god.value,
                mana.value,
                rarity.value,
                tribe.value
            )) {
                is Resource.Success -> {
                    if (!result.data!!.records.isNullOrEmpty()) {
                        cardsAmount.value = result.data.total
                        endReached.value = page >= result.data.total / PER_PAGE
                        val cardsEntries = result.data.records.mapIndexed { _, record ->
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
                        cardRecords.value += cardsEntries
                    }
                    loadError.value = ""
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
                else -> throw java.lang.IllegalArgumentException("Illegal Result")

            }
        }
    }

    fun clearCardRecords() {
        cardRecords.value = listOf()
        page = 1
    }

    fun updateFilters(filterName: String, value: String) {
        when (filterName) {
            "God" -> god.value = value
            "Rarity" -> rarity.value = value
            "Mana" -> mana.value = value
            "Tribe" -> tribe.value = value
        }
    }

    fun clearFilters() {
        god.value = ""
        rarity.value = ""
        mana.value = ""
        tribe.value = ""
    }

    fun newDeck(god: String) {
        deck.value = Deck(god)
        DeckBuilder.saveDeck(deck.value)
    }

    fun cardCount(libId: String): Int {
        return if (deck.value.libraryIds.contains(libId)) deck.value.libraryIds.count { it == libId }
        else 0
    }

    fun addCard(entry: Record): Int {
        return deck.value.addCard(entry)
    }

    fun removeCard(entry: Record): Int {
        return deck.value.removeCard(entry)
    }
}