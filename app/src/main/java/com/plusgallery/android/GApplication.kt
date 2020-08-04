package com.plusgallery.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.plusgallery.android.extension.ExtensionManager
import com.plusgallery.android.util.Preference

class GApplication: Application() {
    lateinit var extensions: ExtensionManager

    override fun onCreate() {
        super.onCreate()
        extensions = ExtensionManager(this)
        updateTheme()
    }

    fun updateTheme() {
        AppCompatDelegate.setDefaultNightMode(
            when (Preference(this).getBoolean("night")) {
                true -> AppCompatDelegate.MODE_NIGHT_YES
                false -> AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}