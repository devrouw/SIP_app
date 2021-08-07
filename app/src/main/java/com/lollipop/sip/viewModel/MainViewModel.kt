package com.lollipop.sip.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.sip.repository.MainRepository
import com.lollipop.sip.service.model.*
import com.lollipop.sip.util.ResultOfNetwork
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _repository = MainRepository()

    val progressBar: LiveData<Boolean>
    val login: LiveData<ResultOfNetwork<LoginData>>
    val bangunan: LiveData<ResultOfNetwork<BangunanData>>
    val penghuni: LiveData<ResultOfNetwork<KirimData>>
    val penghuniAll: LiveData<ResultOfNetwork<PenghuniData>>

    init {
        this.progressBar = _repository.progressBar
        this.login = _repository.loginResult
        this.bangunan = _repository.bangunanResult
        this.penghuni = _repository.kirimResult
        this.penghuniAll = _repository.penghuniResult
    }

    fun doLogin(case: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.doLogin(case, username, password)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.loginResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.loginResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.loginResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun showBangunan(case: String, id_warga: String) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.showBangunan(case, id_warga)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.bangunanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.bangunanResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.bangunanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun showBangunanById(case: String, id_bangunan: String) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.showBangunanById(case, id_bangunan)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.bangunanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.bangunanResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.bangunanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun showPenghuni(case: String, id_bangunan: String) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.showPenghuni(case, id_bangunan)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.penghuniResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.penghuniResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.penghuniResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun inputPenghuni(case: String, penghuni: PenghuniResult) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.inputPenghuni(case, penghuni)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.kirimResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.kirimResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.kirimResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }
}