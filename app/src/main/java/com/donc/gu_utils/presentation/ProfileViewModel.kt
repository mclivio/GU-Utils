package com.donc.gu_utils.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donc.gu_utils.data.models.RecordPlayer
import com.donc.gu_utils.repository.profile.ProfileRepository
import com.donc.gu_utils.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    val profile = mutableStateOf(RecordPlayer(0,0,0,"",0,0,0))

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var playerId = mutableStateOf("")

    fun loadProfile(user_id: String) {
        viewModelScope.launch {
            playerId.value = user_id
            isLoading.value = true
            when (val result = repository.getPlayerProfile(playerId.value)
            ) {
                is Resource.Success -> {
                    if (!result.data!!.records.isNullOrEmpty()) {
                        profile.value = result.data.records[0]
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
}