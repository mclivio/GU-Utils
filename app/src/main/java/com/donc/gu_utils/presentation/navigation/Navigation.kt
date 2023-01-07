package com.donc.gu_utils.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.donc.gu_utils.presentation.cardsearch.CardSearchScreen
import com.donc.gu_utils.presentation.deck.DeckScreen
import com.donc.gu_utils.presentation.history.HistoryScreen
import com.donc.gu_utils.presentation.weekend.WeekendScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "card_search"){
        composable("card_search"){
            CardSearchScreen()
        }
//        composable("card_details/{card_id}"){
//        }
        composable("weekend"){
            WeekendScreen()
        }
        composable("history"){
            HistoryScreen()
        }
        composable("deck"){
            DeckScreen()
        }
        //TODO: Routes for Weekend Results, Player Stats and Deck Builder
    }
}