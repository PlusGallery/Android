package com.plusgallery.android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plusgallery.android.R
import com.plusgallery.android.extension.StoredExtension
import com.plusgallery.android.view.ExtensionSelectDialog
import kotlinx.android.synthetic.main.item_extension_dialog.view.*

class ExtensionSelectAdapter(var array: Array<StoredExtension>, private val dialog: ExtensionSelectDialog)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ExtensionHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            val item = array[position]
            val resource = item.getResources(dialog.requireContext())
            itemView.name.text = resource.getText(item.label)
            itemView.icon.setImageDrawable(resource.getDrawable(item.icon, null))

            itemView.linearLayout.setOnClickListener {
                dialog.onItemPress(position, itemView.linearLayout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExtensionHolder(inflater.inflate(R.layout.item_extension_dialog, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExtensionHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return array.size
    }
}