package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.RecordMatch
import com.donc.gu_utils.repository.history.HistoryRepository
import com.donc.gu_utils.util.Constants
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {
    private val _matchRecords = mutableStateListOf<RecordMatch>()
    val matchRecords: List<RecordMatch> = _matchRecords

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var playerId = mutableStateOf("")

    private fun loadLatestWinningMatches() {
        viewModelScope.launch {
            isLoading.value = true
            var page = 0
            var lastResult = 9999
            do {
                when (val result =
                    repository.getWinningMatches(player_won = playerId.value, page+1)) {
                    is Resource.Success -> {
                        lastResult = result.data!!.total
                        if (!result.data.records.isNullOrEmpty()) {
                            val unixCurrentTime = System.currentTimeMillis() / 1000
                            val unixTenDaysAgo = unixCurrentTime - 864000
                            /*  1m = 60s
                                1h = 60m = 3600s
                                1d = 24h = 1440m = 86400s
                                10d = 240h = 14400m = 864000s  */
                            val matchEntries = result.data.records.map { record ->
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
                            }.filter { it.start_time >= unixTenDaysAgo }
                            _matchRecords.addAll(matchEntries)
                            page++

                            //Since the API doesn't search for matches between start_time and end_time
                            //but instead searches for the exact times, the only way to get all matches
                            //between a certain range of time is to get all matches and filter them.
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
            } while (page * Constants.PER_PAGE_MAX < lastResult)
        }
    }

    private fun loadLatestLosingMatches() {
        viewModelScope.launch {
            isLoading.value = true
            var page = 0
            var lastResult = 9999
            do {
                when (val result =
                    repository.getLosingMatches(player_lost = playerId.value, page+1)) {
                    is Resource.Success -> {
                        lastResult = result.data!!.total
                        if (!result.data.records.isNullOrEmpty()) {
                            val unixCurrentTime = System.currentTimeMillis() / 1000
                            val unixTenDaysAgo = unixCurrentTime - 864000
                            val matchEntries = result.data.records.map { record ->
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
                            }.filter { it.start_time >= unixTenDaysAgo }
                            _matchRecords.addAll(matchEntries)
                            page++
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
            } while (page * Constants.PER_PAGE_MAX < lastResult)
        }
    }
    /* As unexpected as it is, the API gets matches by either starting/ending time or winning/losing player
    * Therefore to get the latest matches of the player, you need to get the winning and losing matches,
    * combine them and then sort them.
    * The reason why some websites can show the latest matches without doing this is that they are constantly
    * tracking all new records that appear in the API and adding them to a list corresponding to each player
    * profile. Since I do not have the hardware to constantly monitor the changes in the API, getting all matches
    * from a winning player, all from a losing player, combining them and then sorting them is the only way
    * to achieve the desired outcome */
    fun loadLatestMatches(player_id: String) {
        playerId.value = player_id
        _matchRecords.clear()
        loadLatestWinningMatches()
        loadLatestLosingMatches()
    }
}
