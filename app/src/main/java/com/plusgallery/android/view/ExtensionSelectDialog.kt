package com.plusgallery.android.view

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.adapter.ExtensionSelectAdapter
import com.plusgallery.android.adapter.OnItemAction
import com.plusgallery.android.extension.StoredExtension
import kotlinx.android.synthetic.main.fragment_dialog_select.*


class ExtensionSelectDialog: DialogFragment(), OnItemAction {
    interface OnNewAction {
        fun onExtensionSelect(index: Int, text: String?, extension: StoredExtension)
    }
    private var prevOrientation: Int = 0
    private lateinit var callback: OnNewAction
    private var index: Int = -1
    private var search: String? = null

    override fun onResume() {
        super.onResume()
        //lock screen to portrait
        prevOrientation = requireActivity().requestedOrientation
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()
        //set rotation to sensor dependent
        requireActivity().requestedOrientation = prevOrientation
    }

    override fun onCreateDialog(bundle: Bundle?): Dialog {
        val onCreateDialog: Dialog = super.onCreateDialog(bundle)
        onCreateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return onCreateDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val extensions = GApplication.get.extensions.storedArray()
        recyclerView.adapter = ExtensionSelectAdapter(extensions,this)
        exitFab.setOnClickListener { dismiss() }
    }

    override fun dismiss() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        transaction.remove(this).commit()
    }

    fun setOnExtensionSelect(call: OnNewAction) {
        callback = call
    }

    fun show(manager: FragmentManager, index: Int = -1, search: String? = null) {
        this.index = index
        this.search = search
        val transaction = manager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(Window.ID_ANDROID_CONTENT, this).commit()
    }

    override fun onItemPress(item: Any?, view: View) {
        dismiss()
        if (this::callback.isInitialized) {
            callback.onExtensionSelect(index, search, item as StoredExtension)
        }
    }
}