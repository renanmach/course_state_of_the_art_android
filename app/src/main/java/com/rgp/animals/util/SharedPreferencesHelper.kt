package com.rgp.animals.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {
    private val PREF_API_KEY = "api_key"
    private val prefs = PreferenceManager
        .getDefaultSharedPreferences(context.applicationContext)


    fun saveApiKey(key: String?) {
        prefs.edit().putString(PREF_API_KEY, key).apply()
    }

    fun getApiKey() = prefs.getString(PREF_API_KEY, null)
}