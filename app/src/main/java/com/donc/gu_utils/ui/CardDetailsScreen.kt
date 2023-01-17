package com.donc.gu_utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.donc.gu_utils.R
import com.donc.gu_utils.util.Constants

@Composable
fun CardDetailsScreen(
    proto_id : Int,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constants.BASE_PIC_URL + proto_id)
                .placeholder(CircularProgressDrawable(LocalContext.current))
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.description_card) + "card ID: $proto_id",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
        )
        IconButton(
            modifier = Modifier
                .padding(12.dp)
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    shape = CircleShape
                ),
            onClick = { navController.popBackStack() }
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.button_back),
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }

}