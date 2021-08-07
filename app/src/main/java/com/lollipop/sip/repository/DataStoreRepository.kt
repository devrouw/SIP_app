package com.lollipop.sip.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

const val PREFERENCE_NAME = "sip_preferences"

private object PreferenceKeys {
    val loginStatus = booleanPreferencesKey("loginStatus")
    val id = stringPreferencesKey("id_warga")
    val namaLengkap = stringPreferencesKey("namaLengkap")
    val noTelpon = stringPreferencesKey("noTelpon")
    val alamat = stringPreferencesKey("alamat")
}


class DataStoreRepository(private val dataStore: DataStore<Preferences>) {


    //Save login state to preferences
    suspend fun saveLoginState(value: Boolean) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.loginStatus] = value
        }
    }

    //Save login data to preferences
    suspend fun saveAuth(id: String, namaLengkap: String, noTelpon: String, alamat: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.id] = id
            preference[PreferenceKeys.namaLengkap] = namaLengkap
            preference[PreferenceKeys.noTelpon] = noTelpon
            preference[PreferenceKeys.alamat] = alamat
        }
    }

    //Read login state from preferences
    val readLoginStatus: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val isPaymentSuccess: Boolean = preferences[PreferenceKeys.loginStatus] ?: false
            isPaymentSuccess
        }

    val readUserData: Flow<List<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d(exception.message.toString())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val data: MutableList<String> = mutableListOf()
            val id: String = preferences[PreferenceKeys.id] ?: "-"
            val nama: String = preferences[PreferenceKeys.namaLengkap] ?: "-"
            val notelp: String = preferences[PreferenceKeys.noTelpon] ?: "-"
            val alamat: String = preferences[PreferenceKeys.alamat] ?: "-"
            data.add(id)
            data.add(nama)
            data.add(notelp)
            data.add(alamat)
            data
        }
}