package com.plusgallery.extension.ui

import android.se.omapi.Session

interface LoginDialogAction {
    fun onNewSession()
}

abstract class LoginDialog(): BaseDialog() {
    abstract fun setOnLogin(handler: (session: Session) -> Unit)
}