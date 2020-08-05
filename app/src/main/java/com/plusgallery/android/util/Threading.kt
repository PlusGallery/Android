package com.plusgallery.android.util

import android.os.Handler
import android.os.Looper

object Threading {
    class async(handler: () -> Unit) : Thread(handler) {
        lateinit var complete: () -> Unit

        init {
            start()
        }

        override fun run() {
            try {
                super.run()
            } finally {
                complete()
            }
        }

        fun onComplete(handler: () -> Unit) {
            this.
            complete = handler
        }
    }

    class sync(handler: () -> Unit): Handler(Looper.getMainLooper()) {
        init {
            post(Runnable(handler))
        }
    }
}