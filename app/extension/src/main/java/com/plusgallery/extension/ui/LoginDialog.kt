package com.plusgallery.extension.ui

import android.se.omapi.Session

abstract class LoginDialog: BaseDialog() {
    abstract fun setOnLogin(handler: (session: Session) -> Unit)
}