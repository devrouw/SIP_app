package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PenghuniData(
    val code: Int?=0,
    val message: String?="-",
    val data: List<PenghuniResult>?=null,
    val status: String?="-"
)