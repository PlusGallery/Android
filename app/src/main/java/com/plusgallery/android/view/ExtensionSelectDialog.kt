package com.plusgallery.android.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.plusgallery.android.R
import com.plusgallery.android.adapter.ExtensionSelectAdapter
import com.plusgallery.android.adapter.OnItemAction
import com.plusgallery.android.extension.StoredExtension
import kotlinx.android.synthetic.main.fragment_dialog_select.*

class ExtensionSelectDialog: DialogFragment(), OnItemAction {
    interface OnNewAction {
        fun onExtensionSelect(index: Int, text: String?, extension: StoredExtension)
    }

    private lateinit var manager: FragmentManager
    private lateinit var extensions: Array<StoredExtension>
    private lateinit var adapter: ExtensionSelectAdapter
    private lateinit var callback: OnNewAction
    private var index: Int = -1
    private var search: String? = null

    companion object {
        fun new(manager: FragmentManager, exts: Array<StoredExtension>): ExtensionSelectDialog {
            val instance = ExtensionSelectDialog()
            instance.manager = manager
            instance.extensions = exts
            return instance
        }
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
        adapter = ExtensionSelectAdapter(extensions,this)
        recyclerView.adapter = adapter
        exitFab.setOnClickListener { dismiss() }
    }

    fun setOnExtensionSelect(call: ExtensionSelectDialog.OnNewAction) {
        callback = call
    }

    fun show(index: Int = -1, search: String? = null) {
        this.index = index
        this.search = search
        val transaction = manager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(Window.ID_ANDROID_CONTENT, this)/*.addToBackStack(null)*/.commit()
    }

    override fun onItemPress(item: Any?, view: View) {
        dismiss()
        if (this::callback.isInitialized) {
            val position = item as Int
            callback.onExtensionSelect(index, search, extensions[position])
        }
    }
}