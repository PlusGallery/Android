package com.plusgallery.android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preference(context: Context?) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getInt(name: String?): Int {
        return preferences.getInt(name, 0)
    }

    fun getLong(name: String?): Long {
        return preferences.getLong(name, 0)
    }

    fun getBoolean(name: String?): Boolean {
        return preferences.getBoolean(name, false)
    }

    fun getString(name: String?): String? {
        return preferences.getString(name, "")
    }

    fun setLong(name: String?, value: Long) {
        val editor = preferences.edit()
        editor.putLong(name, value)
        editor.apply()
    }

    fun setInt(name: String?, value: Int) {
        val editor = preferences.edit()
        editor.putInt(name, value)
        editor.apply()
    }

    fun setBoolean(name: String?, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(name, value)
        editor.apply()
    }

    fun setString(name: String?, value: String?) {
        val editor = preferences.edit()
        editor.putString(name, value)
        editor.apply()
    }

    fun delete() {
        preferences.edit().clear().apply()
    }

}