package com.donc.gu_utils.data.models

data class RecordPlayer(
    val lost_matches: Int,
    val total_xp: Int,
    val user_id: Int,
    val username: String,
    val won_matches: Int,
    val xp_level: Int,
    val xp_to_next: Int
)