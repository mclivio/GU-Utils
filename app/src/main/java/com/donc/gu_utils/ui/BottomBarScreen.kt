package com.donc.gu_utils.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBarScreen(
    items: List<BottomBarItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomBarItem) -> Unit
){
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier,
        tonalElevation = 2.dp
    ) {
        items.forEach{ item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item)},
                icon = {
                    Column(
                        horizontalAlignment = CenterHorizontally) {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.title,
                            modifier = if (selected) Modifier.size(20.dp) else Modifier.size(16.dp),
                            tint = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondary
                        )
                        if (selected){
                            Text(
                                text = item.title,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            )
        }
    }
}