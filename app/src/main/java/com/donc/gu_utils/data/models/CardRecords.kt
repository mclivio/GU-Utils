package com.donc.gu_utils.data.models

data class CardRecords(
    val page: Int,
    val perPage: Int,
    val records: List<Record>,
    val total: Int
)