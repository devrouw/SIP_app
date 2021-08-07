package com.lollipop.sip.service.network

import android.content.Context
import com.lollipop.sip.view.ApplicationController
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val okHttpClient =
    OkHttpClient.Builder()
        .addNetworkInterceptor(ChuckInterceptor(ApplicationController.appContext))
        .build()

private val retrofitCall = Retrofit.Builder()
    .baseUrl("http://sip.sha-dev.com/android/")
    .addConverterFactory(MoshiConverterFactory.create())
    .client(okHttpClient)
    .build()

class RetrofitClient(context: Context) {
    companion object Retrofit {
        /**
         * http://ensiklopedia.sha-dev.com/
         * */
        val ftp: MyNetwork = retrofitCall.create(MyNetwork::class.java)
    }
}