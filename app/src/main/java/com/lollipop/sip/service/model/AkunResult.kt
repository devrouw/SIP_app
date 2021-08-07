package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AkunResult(
    val id_warga: String? = "-",
    val username: String? = "-",
    val password: String? = "-",
    val nama_lengkap: String? = "-",
    val alamat: String? = "-",
    val no_telp: String? = "-",
    val no_rumah: String? = "-",
)
