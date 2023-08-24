package com.example.navigationcomponentbasics.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private var prefs = EncryptedSharedPreferences.create(
        Constants.PREFS_TOKEN_FILE,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {

        /*val editor = prefs.edit()
        editor.putString(Constants.USER_TOKEN, token)
        editor.apply()*/

        //optimised code
        with(prefs.edit()) {
            putString(Constants.USER_TOKEN, token)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(Constants.USER_TOKEN, null)
    }

}