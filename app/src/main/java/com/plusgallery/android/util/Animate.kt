package com.plusgallery.android.util

import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.view.View

object Animate {
    fun clipReveal(v: View): Bundle {
        return ActivityOptions.makeClipRevealAnimation(v, 0, 0, v.measuredWidth, v.measuredHeight).toBundle()
    }

    fun custom(context: Context, enter: Int, exit: Int): Bundle {
        return ActivityOptions.makeCustomAnimation(context, enter, exit).toBundle()
    }
}