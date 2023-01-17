package com.donc.gu_utils.data.models

data class Record(
    val art_id: String,
    val attack: Attack,
    val collectable: Boolean,
    val effect: String,
    val god: String,
    val health: Health,
    val id: Int,
    val lib_id: String,
    val live: String,
    val mana: Int,
    val name: String,
    val rarity: String,
    val `set`: String,
    val tribe: Tribe,
    val type: String
)