package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AkunData(
    val code: Int?=0,
    val message: String?="-",
    val data: List<AkunResult>?=null,
    val status: String?="-"
)
