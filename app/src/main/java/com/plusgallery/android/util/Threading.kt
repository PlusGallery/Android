package com.plusgallery.android.util

import android.os.Handler
import android.os.Looper

class async(handler: () -> Unit) : Thread(handler) {
    lateinit var complete: () -> Unit

    init {
        start()
    }

    fun onComplete(handler: () -> Unit) {
        complete = handler
    }
}

class sync(handler: () -> Unit): Handler(Looper.getMainLooper()) {
    init {
        post(Runnable(handler))
    }
}