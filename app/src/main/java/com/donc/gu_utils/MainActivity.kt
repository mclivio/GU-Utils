package com.donc.gu_utils

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.donc.gu_utils.presentation.navigation.BottomBarItem
import com.donc.gu_utils.presentation.navigation.BottomBarScreen
import com.donc.gu_utils.presentation.navigation.Navigation
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
                var titleDestination by remember {mutableStateOf("")}
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
                                        contentDescription = getString(R.string.github_description),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomBarScreen(
                            items = listOf(
                                BottomBarItem(
                                    route = "card_search",
                                    title = "Cards",
                                    icon = R.drawable.icons8_search_in_list_24
                                    //Search in List icon by Icons8
                                ),
                                BottomBarItem(
                                    route = "weekend",
                                    title = "Weekend",
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
                ){
                    Navigation(navController = navController)
                }
            }
        }
    }
}