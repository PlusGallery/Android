package com.plusgallery.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.adapter.ExtensionAdapter
import com.plusgallery.android.adapter.OnItemAction
import com.plusgallery.android.extension.ExtensionManager
import com.plusgallery.android.extension.RemoteExtension
import com.plusgallery.android.extension.StoredExtension
import com.plusgallery.android.util.Threading
import com.plusgallery.android.view.IconPopupMenu
import kotlinx.android.synthetic.main.fragment_extensions.*

class ExtensionsFragment : Fragment(), OnItemAction, PopupMenu.OnMenuItemClickListener {
    private lateinit var extensions: ExtensionManager
    private lateinit var storedAdapter: ExtensionAdapter<StoredExtension>
    private lateinit var remoteAdapter: ExtensionAdapter<RemoteExtension>
    private var progressBar: ProgressBar? = null
    private var extension: Any? = null
    private var isBusy: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_extensions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extensions = (requireActivity().application as GApplication).extensions
        // Stored
        storedRecyclerView.layoutManager = LinearLayoutManager(context)
        storedAdapter = ExtensionAdapter(requireContext(), this)
        storedRecyclerView.adapter = storedAdapter
        // Remote
        remoteRecyclerView.layoutManager = LinearLayoutManager(context)
        remoteAdapter = ExtensionAdapter(requireContext(), this)
        remoteRecyclerView.adapter = remoteAdapter

        onResult(true)
    }

    private fun onResult(success: Boolean) {
        progressBar?.visibility = View.GONE
        if (!success) {
            Toast.makeText(context, "Error while fetching remote.", Toast.LENGTH_LONG).show()
        }
        storedAdapter.array = extensions.storedArray()
        storedAdapter.notifyDataSetChanged()
        remoteAdapter.array = extensions.remoteArray()
        remoteAdapter.notifyDataSetChanged()
        isBusy = false
        progressBar = null
    }

    override fun onItemPress(item: Any?, view: View) {
        if (isBusy)
            return
        extension = item
        progressBar = view.findViewById(R.id.progressBar)
        val menu = IconPopupMenu(requireContext(), view)
        when (item) {
            is RemoteExtension ->
                menu.inflate(R.menu.extension_remote_menu)
            is StoredExtension ->
                menu.inflate(R.menu.extension_stored_menu)
        }
        menu.setOnMenuItemClickListener(this)
        menu.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        isBusy = true
        progressBar?.visibility = View.VISIBLE
        when (item.itemId) {
            R.id.extensions_update -> {
                extensions.update(extension as StoredExtension) { success ->
                    Threading.sync { onResult(success) }
                }
            }
            R.id.extensions_install -> {
                extensions.install(extension as RemoteExtension) { success ->
                    Threading.sync { onResult(success) }
                }
            }
            R.id.extensions_delete -> {
                extensions.uninstall(extension as StoredExtension)
                onResult(true)
            }
        }
        return true
    }
}