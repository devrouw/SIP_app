package com.lollipop.sip.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lollipop.sip.databinding.ActivityLoginBinding
import com.lollipop.sip.service.model.LoginResult
import com.lollipop.sip.util.Constant
import com.lollipop.sip.util.ResultOfNetwork
import com.lollipop.sip.viewModel.DataStoreViewModel
import com.lollipop.sip.viewModel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityLoginBinding

    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding){
            btnLogin.setOnClickListener {
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (username == "") {
                    tfUsername.isErrorEnabled = true
                    tfUsername.error = "Email harus diisi."
                } else {
                    tfUsername.isErrorEnabled = false
                }

                if (password == "") {
                    tfPassword.isErrorEnabled = true
                    tfPassword.error = "Kata Sandi harus diisi."
                } else {
                    tfPassword.isErrorEnabled = false
                }

                if (username != "" && password != "") {
                    _viewModel.doLogin("login",username,password)
                }
            }
        }

        observableLiveData()
    }

    private fun initializeViewModel() {
        _viewModelDataStore = ViewModelProvider(this).get(DataStoreViewModel::class.java)
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun observableLiveData() {
        _viewModel.login.observe(this@LoginActivity, { result ->
            when(result){
                is ResultOfNetwork.Success -> {
                    result.value.code?.let { isSuccessNetworkCallback(it,result.value.data) }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isSuccessNetworkCallback(code: Int, data: List<LoginResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this@LoginActivity, "Email atau Password Salah", Toast.LENGTH_SHORT).show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                if (data != null) {
                    data.get(0).id_warga?.let { data.get(0).nama_lengkap?.let { it1 ->
                        data.get(0).no_telp?.let { it2 ->
                            data.get(0).alamat?.let { it3 ->
                                _viewModelDataStore.setAuthPref(it,
                                    it1, it2, it3
                                )
                            }
                        }
                    } }
                }
                _viewModelDataStore.setLoginStatus(true)
                Toast.makeText(this@LoginActivity, "Berhasil Masuk", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }

}