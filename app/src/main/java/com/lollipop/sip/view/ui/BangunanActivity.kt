package com.lollipop.sip.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lollipop.sip.R
import com.lollipop.sip.databinding.ActivityBangunanBinding
import com.lollipop.sip.service.model.BangunanResult
import com.lollipop.sip.service.model.PenghuniResult
import com.lollipop.sip.util.Constant
import com.lollipop.sip.util.GlideUtil
import com.lollipop.sip.util.ResultOfNetwork
import com.lollipop.sip.view.adapter.PenghuniAdapter
import com.lollipop.sip.viewModel.DataStoreViewModel
import com.lollipop.sip.viewModel.MainViewModel
import timber.log.Timber

class BangunanActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityBangunanBinding

    private lateinit var _viewModel: MainViewModel
    private lateinit var _adapter: PenghuniAdapter

    private val _idBangunan by lazy { intent.getStringExtra("id_bangunan").orEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBangunanBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()
        initializeAdapter()

        with(_binding) {
            btAdd.setOnClickListener {
                startActivity(
                    Intent(
                        this@BangunanActivity,
                        PenghuniActivity::class.java
                    ).putExtra("id_bangunan", _idBangunan)
                )
            }
        }

        observeLiveData()
    }

    private fun initializeViewModel() {
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        _viewModel.showBangunanById("show_bangunan_by_id", _idBangunan)
        _viewModel.showPenghuni("show_penghuni",_idBangunan)
    }

    private fun initializeAdapter() {
        _binding.rvAnggota.layoutManager = LinearLayoutManager(this)
        _adapter = PenghuniAdapter()
        _adapter.setOnItemClickCallback(object : PenghuniAdapter.OnItemClickCallback{
            override fun onItemClick(item: PenghuniResult) {
                Timber.d("lihat data ${item.nama_lengkap}")
            }

        })
        _binding.rvAnggota.adapter = _adapter
    }

    private fun observeLiveData() {
        _viewModel.penghuniAll.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    when (it.value.code) {
                        Constant.Network.REQUEST_NOT_FOUND -> {
                            Timber.d("Request not found")
                        }
                        Constant.Network.REQUEST_SUCCESS -> {
                            _adapter.setList(it.value.data)
                        }
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        _viewModel.bangunan.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    it.value.data?.let { it1 ->
                        isSuccessNetworkCallback(
                            it.value.code,
                            it1
                        )
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun isSuccessNetworkCallback(code: Int?, data: List<BangunanResult>) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Timber.d("Request not found")
            }
            Constant.Network.REQUEST_SUCCESS -> {
                _binding.tvJenisBg.text = data[0].jenis_bangunan
                _binding.tvAlamat.text = data[0].alamat
                GlideUtil.buildDefaultGlide(
                    this,
                    "${Constant.URL.IMAGE_BANGUNAN}${data[0].foto_bangunan}",
                    _binding.ivImageDetail,
                    GlideUtil.CENTER_CROP,
                    R.drawable.ic_baseline_image_not_supported
                )
                _binding.tvLuasBg.text = "${data[0].luas_bangunan} m2"
                _binding.tvLuasTanah.text = "${data[0].luas_tanah} m2"
            }
        }
    }
}