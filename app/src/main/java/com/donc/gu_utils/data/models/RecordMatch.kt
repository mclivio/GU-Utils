package com.donc.gu_utils.data.models

data class RecordMatch(
    val end_time: Int,
    val game_id: String,
    val game_mode: Int,
    val player_info: List<PlayerInfo>,
    val player_lost: Int,
    val player_won: Int,
    val start_time: Int,
    val total_rounds: Int,
    val total_turns: Int
)