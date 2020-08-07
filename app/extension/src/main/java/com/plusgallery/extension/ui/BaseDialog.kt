package com.plusgallery.extension.ui

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class BaseDialog: DialogFragment() {
    lateinit var eResources: Resources

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val onCreateDialog: Dialog = super.onCreateDialog(bundle)
        onCreateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return onCreateDialog
    }

    fun show(manager: FragmentManager, newRes: Resources) {
        eResources = newRes
        val transaction = manager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(Window.ID_ANDROID_CONTENT, this).addToBackStack(null).commit()
    }
}