package com.donc.gu_utils.data.models

data class MatchRecords(
    val page: Int,
    val perPage: Int,
    val records: List<RecordMatch>,
    val total: Int
)