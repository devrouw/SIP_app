package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BangunanResult(
    val id_bangunan: String? = "-",
    val foto_bangunan: String? = "-",
    val jenis_bangunan: String? = "-",
    val alamat: String? = "-",
    val lng: Double? = 0.0,
    val lat: Double? = 0.0,
    val nomor_rumah: String? = "-",
    val luas_tanah: String? = "-",
    val luas_bangunan: String? = "-",
    val id_warga: String? = "-",
)