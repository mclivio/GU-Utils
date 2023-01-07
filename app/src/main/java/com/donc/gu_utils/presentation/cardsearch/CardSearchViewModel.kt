package com.donc.gu_utils.presentation.cardsearch

import com.donc.gu_utils.repository.cardsearch.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardSearchViewModel @Inject constructor(
    private val repository: CardRepository
) {

}