package com.donc.gu_utils.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun Navigation(navController: NavHostController) {
    BackHandler(onBack = {})
    NavHost(navController = navController, startDestination = "cards"){
        composable("cards"){
            CardSearchScreen(navController)
        }
        composable(
            "details/{proto_id}",
            arguments = listOf(navArgument("proto_id") { type = NavType.IntType})
        ){
            val id = remember {
                it.arguments?.getInt("proto_id")
            }
            if (id != null) {
                CardDetailsScreen(
                    proto_id = id,
                    navController = navController
                )
            }
        }
        composable("profile"){
            ProfileScreen()
        }
        composable("history"){
            HistoryScreen()
        }
        composable("deck"){
            DeckScreen(navController = navController)
        }
    }
}