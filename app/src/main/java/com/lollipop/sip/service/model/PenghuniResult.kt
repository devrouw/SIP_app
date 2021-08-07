package com.lollipop.sip.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PenghuniResult(
    val nik: String = "-",
    val nama_lengkap: String = "-",
    val tempat_lahir: String = "-",
    val tgl_lahir: String = "-",
    val status_kawin: String = "-",
    val kewarganegaraan: String = "-",
    val jenis_kelamin: String = "-",
    val pekerjaan: String = "-",
    val goldar: String = "-",
    val ket_tambahan: String = "-",
    val id_bangunan_fk: String = "-",
)