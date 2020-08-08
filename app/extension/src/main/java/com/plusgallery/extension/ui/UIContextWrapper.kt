package com.plusgallery.extension.ui

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import java.util.*


class UIContextWrapper(base: Context?, private val newRes: Resources) : ContextWrapper(base) {
    private var mInflater: LayoutInflater? = null

    override fun getResources(): Resources? {
        return newRes
    }

    /**
     * Required so that the LayoutInflater uses this context to resolve resources.
     */
    override fun getSystemService(name: String): Any? {
        if (Context.LAYOUT_INFLATER_SERVICE == name) {
            if (mInflater == null) {
                mInflater = LayoutInflater.from(baseContext).cloneInContext(this)
            }
            return mInflater
        }
        return super.getSystemService(name)
    }
}