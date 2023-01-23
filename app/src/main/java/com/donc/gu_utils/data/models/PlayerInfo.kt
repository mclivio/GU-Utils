package com.donc.gu_utils.data.models

data class PlayerInfo(
    val cards: List<Int>,
    val global: Boolean,
    val god: String,
    val god_power: Int,
    val health: Int,
    val status: String,
    val user_id: Int
)