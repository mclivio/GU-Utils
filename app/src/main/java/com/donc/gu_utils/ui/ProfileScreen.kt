package com.donc.gu_utils.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.donc.gu_utils.data.models.RecordPlayer
import com.donc.gu_utils.presentation.ProfileViewModel
import kotlin.reflect.full.memberProperties

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile = viewModel.profile
    val isLoading = viewModel.isLoading
    val loadError = viewModel.loadError
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.fillMaxSize()){
            SearchSection{
                viewModel.loadProfile(it)
            }
            Spacer(modifier = Modifier.height(4.dp))
            LoadingSection(isLoading.value, loadError.value)
            ProfileSection(profile.value)
        }
    }
}

@Composable
fun ProfileSection(
    profile: RecordPlayer
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp)
        ){
            RecordPlayer::class.memberProperties.forEach {
                Row(
                    Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${it.name}:",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "${it.get(profile)}")
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(1.dp)
                )
            }
        }
    }
}