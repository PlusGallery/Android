package com.plusgallery.android.fragment

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.plusgallery.android.GApplication
import com.plusgallery.android.R

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        findPreference<SwitchPreferenceCompat>("night")?.onPreferenceClickListener = this
        findPreference<EditTextPreference>("limit")?.onPreferenceClickListener = this
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "night" -> (activity?.application as GApplication).updateTheme()
        }
        return true
    }
}