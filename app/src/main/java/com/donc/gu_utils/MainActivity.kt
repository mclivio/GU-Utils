package com.donc.gu_utils

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.donc.gu_utils.ui.BottomBarItem
import com.donc.gu_utils.ui.BottomBarScreen
import com.donc.gu_utils.ui.Navigation
import com.donc.gu_utils.ui.theme.GUUtilsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GUUtilsTheme {
                val navController = rememberNavController()
                val uriHandler = LocalUriHandler.current
                var titleDestination by remember {mutableStateOf("Cards")}

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = titleDestination.toString())
                            },
                            actions = {
                                IconButton(onClick = { uriHandler.openUri("https://github.com/mclivio/GU-Utils") }) {
                                    Icon(
                                        painter = painterResource(R.drawable.icons8_github_24),
                                        contentDescription = stringResource(R.string.description_github),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
                        )
                    },
                    bottomBar = {
                        BottomBarScreen(
                            items = listOf(
                                BottomBarItem(
                                    route = "cards",
                                    title = "Cards",
                                    icon = R.drawable.icons8_search_in_list_24
                                    //Search in List icon by Icons8
                                ),
                                BottomBarItem(
                                    route = "profile",
                                    title = "Profile",
                                    icon = R.drawable.icons8_graph_24
                                    //Graph icon by Icons8
                                ),
                                BottomBarItem(
                                    route = "history",
                                    title = "History",
                                    icon = R.drawable.icons8_view_details_24
                                    //View Details icon by Icons8
                                ),
                                BottomBarItem(
                                    route = "deck",
                                    title = "Deck",
                                    icon = R.drawable.icons8_deck_24___
                                    //Deck icon by Icons8
                                )
                            ),
                            navController = navController,
                            onItemClick = {
                                titleDestination = it.title
                                navController.navigate(it.route)
                            }
                        )
                    }
                ){ innerPadding ->
                    Box(modifier = Modifier.padding(PaddingValues(
                        0.dp,
                        innerPadding.calculateTopPadding(),
                        0.dp,
                        innerPadding.calculateBottomPadding()))) {
                        Navigation(navController = navController)
                    }
                    //This box with an inner padding calculated from the Scaffold serves as a way
                    //to prevent the Top and Bottom bars from overlapping the design of the screens
                }
            }
        }
    }
}