package com.plusgallery.android.adapter

import android.view.View

interface OnItemAction<T> {
    fun onItemPress(item: T, view: View)
}