package com.example.astro

import android.content.Context
import android.content.SharedPreferences

class SharedPrefUtils {

    companion object {
        private const val PREF_APP = "sharedPreferences"

        fun getBooleanData(context: Context, key: String?): Boolean {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean(key, true)
        }

        fun getIntData(context: Context, key: String?): Int {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(key, 0)
        }

        fun getStringData(context: Context, key: String?): String? {
            return context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key, null)
        }

        fun saveData(context: Context, key: String?, value: String?) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putString(key, value).apply()
        }

        fun saveData(context: Context, key: String?, value: Int) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
        }

        fun saveData(context: Context, key: String?, value: Boolean) {
            context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
        }
    }
}