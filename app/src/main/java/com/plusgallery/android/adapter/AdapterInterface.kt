package com.plusgallery.android.adapter

import android.view.View

interface OnItemAction {
    fun onItemPress(item: Any?, view: View)
}