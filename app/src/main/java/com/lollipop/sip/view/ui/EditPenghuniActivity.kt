package com.lollipop.sip.view.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lollipop.sip.databinding.ActivityPenghuniAnggotaBinding
import com.lollipop.sip.service.model.PenghuniResult
import com.lollipop.sip.util.Constant
import com.lollipop.sip.util.LoadingDialog
import com.lollipop.sip.util.ResultOfNetwork
import com.lollipop.sip.viewModel.MainViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class EditPenghuniActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityPenghuniAnggotaBinding

    private lateinit var _viewModel: MainViewModel
    private val _nik by lazy { intent.getStringExtra("nik").orEmpty() }

    private var _year = 0
    private var _month = 0
    private var _day = 0
    private var _tglLahir = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPenghuniAnggotaBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding){
            btSubmit.setOnClickListener {
                _viewModel.updatePenghuni("update_penghuni", PenghuniResult(
                    nik = _nik,
                    nama_lengkap = etNamaLengkap.text.toString(),
                    tempat_lahir = etTempat.text.toString(),
                    tgl_lahir = _tglLahir,
                    status_kawin = etStatusKawin.text.toString(),
                    kewarganegaraan = etKewarganegaraan.text.toString(),
                    jenis_kelamin = etJenisKelamin.text.toString(),
                    pekerjaan = etPekerjaan.text.toString(),
                    goldar = etGolDarah.text.toString(),
                    ket_tambahan = etKeterangan.text.toString()
                )
                )
            }

            btCalendar.setOnClickListener {
                showTglLahir()
            }
        }

        observeLiveData()
    }

    private fun showTglLahir() {
        val c = Calendar.getInstance()
        _year = c[Calendar.YEAR]
        _month = c[Calendar.MONTH]
        _day = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this,
            { datePicker, i, i1, i2 ->
                val calendar = Calendar.getInstance()
                calendar[i, i1] = i2
                val format = SimpleDateFormat("dd-MMM-yyyy")
                val dateString = format.format(calendar.time)
                _binding.etTgl.setText(dateString)
                _tglLahir = i.toString() + "-" + (i1 + 1) + "-" + i2
                //                        edtTglLahir.setText(i+"-"+(i1+1)+"-"+i2);
            }, _year, _month, _day
        )
        datePickerDialog.show()
    }

    private fun initializeViewModel() {
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        _viewModel.showPenghuniByNIK("show_penghuni_by_id",_nik)
    }

    private fun observeLiveData() {
        val loadingDialog = LoadingDialog(this)
        _viewModel.progressBar.observe(this,{
            if (it) {
                loadingDialog.startLoadingDialog()
            } else {
                loadingDialog.dismiss()
            }
        })
        _viewModel.penghuni.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    when (it.value.code) {
                        Constant.Network.REQUEST_NOT_FOUND -> {
                            Timber.d("Request not found")
                        }
                        Constant.Network.REQUEST_SUCCESS -> {
                            Toast.makeText(this,"Data berhasil diupdate", Toast.LENGTH_LONG).show()
                            finish()
                        }
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        _viewModel.penghuniAll.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    isSuccessNetworkCallback(it.value.code,it.value.data)
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun isSuccessNetworkCallback(code: Int?, data: List<PenghuniResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Timber.d("Request not found")
            }
            Constant.Network.REQUEST_SUCCESS -> {
                with(_binding){
                    etNik.isEnabled = false
                    etNik.setText(_nik)
                    etNamaLengkap.setText(data?.get(0)?.nama_lengkap)
                    etTempat.setText(data?.get(0)?.tempat_lahir)
                    etStatusKawin.setText(data?.get(0)?.status_kawin)
                    etKewarganegaraan.setText(data?.get(0)?.kewarganegaraan)
                    etJenisKelamin.setText(data?.get(0)?.jenis_kelamin)
                    etPekerjaan.setText(data?.get(0)?.pekerjaan)
                    etGolDarah.setText(data?.get(0)?.goldar)
                    etKeterangan.setText(data?.get(0)?.ket_tambahan)

                    if(data?.get(0)?.tgl_lahir?.isNotEmpty() == true){
                        val arr: Array<String> = data.get(0).tgl_lahir.split("-").toTypedArray()

                        val selectedYear = arr[0].toInt()
                        val selectedDay = arr[2].toInt()
                        val selectedMonth = arr[1].toInt()
                        val cal = Calendar.getInstance()
                        cal[Calendar.YEAR] = selectedYear
                        cal[Calendar.DAY_OF_MONTH] = selectedDay
                        cal[Calendar.MONTH] = selectedMonth - 1
                        val format = SimpleDateFormat("dd-MMM-yyyy").format(cal.time)
                        etTgl.setText(format)
                        _tglLahir = data[0].tgl_lahir
                    }else{
                        etTgl.setText("")
                    }
                }
            }
        }
    }
}