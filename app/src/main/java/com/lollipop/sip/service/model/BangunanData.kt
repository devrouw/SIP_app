package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BangunanData(
    val code: Int?=0,
    val message: String?="-",
    val data: List<BangunanResult>?=null,
    val status: String?="-"
)
