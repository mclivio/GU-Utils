package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.Record
import com.donc.gu_utils.data.models.RecordMatch
import com.donc.gu_utils.repository.history.HistoryRepository
import com.donc.gu_utils.util.Constants
import com.donc.gu_utils.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {
    var MatchRecords = mutableStateOf<List<RecordMatch>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var player_id = mutableStateOf("")

    fun loadLatestWinningMatches() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getWinningMatches(player_won = player_id.value
            )) {
                is Resource.Success -> {
                    if (!result.data!!.records.isNullOrEmpty()) {
                        val matchEntries = result.data.records.mapIndexed { _, record ->
                            RecordMatch(
                                record.end_time,
                                record.game_id,
                                record.game_mode,
                                record.player_info,
                                record.player_lost,
                                record.player_won,
                                record.start_time,
                                record.total_rounds,
                                record.total_turns
                            )
                        }
                        MatchRecords.value += matchEntries
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
    fun loadLatestLosingMatches() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getLosingMatches(player_lost = player_id.value
            )) {
                is Resource.Success -> {
                    if (!result.data!!.records.isNullOrEmpty()) {
                        val matchEntries = result.data.records.mapIndexed { _, record ->
                            RecordMatch(
                                record.end_time,
                                record.game_id,
                                record.game_mode,
                                record.player_info,
                                record.player_lost,
                                record.player_won,
                                record.start_time,
                                record.total_rounds,
                                record.total_turns
                            )
                        }
                        MatchRecords.value += matchEntries
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
    /* As unexpected as it is, the API get matches by either starting/ending time or winning/losing player
    * Therefore to get the latest matches of the player, you need to get the winning and losing matches,
    * combine them and then sort them. */
    fun loadLatestMatches() {
        loadLatestWinningMatches()
        loadLatestLosingMatches()
        MatchRecords.value = MatchRecords.value.sortedBy { it.start_time }.reversed()
    }
}