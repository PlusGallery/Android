package com.plusgallery.android.view

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu

class IconPopupMenu: PopupMenu {
    constructor(context: Context, anchor: View): super(context, anchor)
    constructor(context: Context, anchor: View, gravity: Int): super(context, anchor, gravity)
    constructor(context: Context, anchor: View, gravity: Int, popupStyleAttr: Int, popupStyleRes: Int): super(context, anchor, gravity, popupStyleAttr, popupStyleRes)

    override fun inflate(menuRes: Int) {
        super.inflate(menuRes)
        forceShowIcon()
    }

    fun forceShowIcon() {
        val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
        fieldMPopup.isAccessible = true
        val mPopup = fieldMPopup.get(this)
        mPopup.javaClass
            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(mPopup, true)
    }
}