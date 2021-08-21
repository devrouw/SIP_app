package com.lollipop.sip.repository

import androidx.lifecycle.MutableLiveData
import com.lollipop.sip.service.model.*
import com.lollipop.sip.service.network.RetrofitClient
import com.lollipop.sip.util.ResultOfNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository() {
    val progressBar = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<ResultOfNetwork<LoginData>>()
    val bangunanResult = MutableLiveData<ResultOfNetwork<BangunanData>>()
    val kirimResult = MutableLiveData<ResultOfNetwork<KirimData>>()
    val penghuniResult = MutableLiveData<ResultOfNetwork<PenghuniData>>()

    suspend fun doLogin(case: String, username: String, password: String) {
        withContext(Dispatchers.IO) {
            loginResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.login(case, username, password)
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun showBangunan(case: String, id_warga: String) {
        withContext(Dispatchers.IO) {
            bangunanResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.showBangunan(case, id_warga)
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun showBangunanById(case: String, id_bangunan: String) {
        withContext(Dispatchers.IO) {
            bangunanResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.showBangunanById(case, id_bangunan)
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun showPenghuni(case: String, id_bangunan: String) {
        withContext(Dispatchers.IO) {
            penghuniResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.showPenghuni(case, id_bangunan)
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun showPenghuniByNIK(case: String, nik: String) {
        withContext(Dispatchers.IO) {
            penghuniResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.showPenghuniByNIK(case, nik)
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun inputPenghuni(case: String, penghuni: PenghuniResult) {
        withContext(Dispatchers.IO) {
            kirimResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.inputPenghuni(
                        case,
                        penghuni.nik,
                        penghuni.nama_lengkap,
                        penghuni.tempat_lahir,
                        penghuni.tgl_lahir,
                        penghuni.status_kawin,
                        penghuni.kewarganegaraan,
                        penghuni.jenis_kelamin,
                        penghuni.pekerjaan,
                        penghuni.goldar,
                        penghuni.ket_tambahan,
                        penghuni.id_bangunan_fk
                    )
                )
            )
            progressBar.postValue(false)
        }
    }

    suspend fun updatePenghuni(case: String, penghuni: PenghuniResult) {
        withContext(Dispatchers.IO) {
            kirimResult.postValue(
                ResultOfNetwork.Success(
                    RetrofitClient.ftp.updatePenghuni(
                        case,
                        penghuni.nik,
                        penghuni.nama_lengkap,
                        penghuni.tempat_lahir,
                        penghuni.tgl_lahir,
                        penghuni.status_kawin,
                        penghuni.kewarganegaraan,
                        penghuni.jenis_kelamin,
                        penghuni.pekerjaan,
                        penghuni.goldar,
                        penghuni.ket_tambahan
                    )
                )
            )
            progressBar.postValue(false)
        }
    }
}