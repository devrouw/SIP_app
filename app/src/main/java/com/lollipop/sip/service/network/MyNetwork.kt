package com.lollipop.sip.service.network

import com.lollipop.sip.service.model.BangunanData
import com.lollipop.sip.service.model.KirimData
import com.lollipop.sip.service.model.LoginData
import com.lollipop.sip.service.model.PenghuniData
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MyNetwork {
    @FormUrlEncoded
    @POST("api.php")
    suspend fun login(
        @Field("case") case : String,
        @Field("username") username : String,
        @Field("password") password : String
    ) : LoginData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun showBangunan(
        @Field("case") case : String,
        @Field("id_warga") id : String
    ) : BangunanData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun showBangunanById(
        @Field("case") case : String,
        @Field("id_bangunan") id : String
    ) : BangunanData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun showPenghuni(
        @Field("case") case : String,
        @Field("id_bangunan") id : String
    ) : PenghuniData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun inputPenghuni(
        @Field("case") case : String,
        @Field("nik") nik : String,
        @Field("nama_lengkap") nama : String,
        @Field("tempat_lahir") tempat : String,
        @Field("tgl_lahir") tanggal : String,
        @Field("status_kawin") status : String,
        @Field("kewarganegaraan") kewarganegaraan : String,
        @Field("jenis_kelamin") jenisKelamin : String,
        @Field("pekerjaan") pekerjaan : String,
        @Field("goldar") goldar : String,
        @Field("ket_tambahan") keterangan : String,
        @Field("id_bangunan_fk") id : String
    ) : KirimData
}