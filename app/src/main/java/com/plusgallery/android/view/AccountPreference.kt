package com.plusgallery.android.view

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder


class AccountPreference(context: Context): Preference(context) {

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        if (holder == null)
            return
        val view = holder.itemView
        val imageView = view.findViewById<ImageView>(android.R.id.icon)
        imageView.maxWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            24f, context.resources.displayMetrics).toInt()
    }

}