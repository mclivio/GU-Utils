package com.donc.gu_utils.data.models

data class PlayerRecords(
    val page: Int,
    val perPage: Int,
    val records: List<RecordPlayer>,
    val total: Int
)