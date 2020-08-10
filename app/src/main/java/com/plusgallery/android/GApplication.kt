package com.plusgallery.android

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.plusgallery.android.extension.ExtensionManager
import com.plusgallery.android.page.PageManager
import com.plusgallery.android.util.Preference
import org.acra.annotation.AcraCore
import org.acra.annotation.AcraDialog
import org.acra.annotation.AcraMailSender

@AcraCore(buildConfigClass = BuildConfig::class,
    alsoReportToAndroidFramework = false,
    deleteOldUnsentReportsOnApplicationStart = true,
    sendReportsInDevMode = false)
@AcraMailSender(mailTo = "rickpersonalcontact@gmail.com", reportAsFile = true)
@AcraDialog(resTheme = R.style.AppTheme,
    resIcon = R.drawable.ic_baseline_bug_report_24,
    resTitle = R.string.report_title,
    resText = R.string.report_text)
class GApplication: Application() {
    lateinit var extensions: ExtensionManager
    lateinit var pages: PageManager

    companion object {
        lateinit var get: GApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        get = this
        extensions = ExtensionManager(this)
        pages = PageManager()
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