package com.plusgallery.android.fragment

import android.os.Bundle
import androidx.preference.*
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.view.AccountPreference
import com.plusgallery.extension.ui.UIContextWrapper

class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        findPreference<SwitchPreferenceCompat>("night")?.onPreferenceClickListener = this
        findPreference<EditTextPreference>("limit")?.onPreferenceClickListener = this

        val root = findPreference<PreferenceCategory>("accounts")
        GApplication.get.extensions.storedArray().forEach {
            val preference = AccountPreference(requireContext())
            preference.key = it.packageName
            preference.onPreferenceClickListener = this
            preference.title = it.getAppLabel(requireContext())
            preference.icon = it.getAppIcon(requireContext())
            root?.addPreference(preference)
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference?.key) {
            "night" -> (activity?.application as GApplication).updateTheme()
            else -> {
                val ext = GApplication.get.extensions.retrieve(preference!!.key)
                if (ext != null) {
                    val ctx = UIContextWrapper(requireContext(), ext.getResources(requireContext()))
                    ext.baseClass.loginDialog.newInstance().show(parentFragmentManager, ctx)
                }
            }
        }
        return true
    }
}