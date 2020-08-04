package com.plusgallery.extension.ui

import androidx.fragment.app.Fragment

interface OnSessionListener {
    fun onSessionChange()
}

class AccountFragment: Fragment() {
    lateinit var listener: OnSessionListener

    fun setOnSessionListener(call: OnSessionListener) {
        listener = call
    }
}