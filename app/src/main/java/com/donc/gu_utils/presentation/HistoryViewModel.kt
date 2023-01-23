package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.RecordMatch
import com.donc.gu_utils.repository.history.HistoryRepository
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {
    var matchRecords = mutableStateOf<List<RecordMatch>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var playerId = mutableStateOf("")

    private fun loadLatestWinningMatches() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getWinningMatches(player_won = playerId.value
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
                        matchRecords.value += matchEntries
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
    private fun loadLatestLosingMatches() {
        viewModelScope.launch {
            isLoading.value = true
            when (val result = repository.getLosingMatches(player_lost = playerId.value
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
                        matchRecords.value += matchEntries
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
    fun loadLatestMatches(player_id: String) {
        playerId.value = player_id
        matchRecords.value = emptyList()
        loadLatestWinningMatches()
        loadLatestLosingMatches()
        matchRecords.value = matchRecords.value.sortedBy { it.start_time }.reversed()
    }
}