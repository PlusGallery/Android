package com.plusgallery.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plusgallery.android.GlideApp
import com.plusgallery.android.R
import com.plusgallery.android.extension.RemoteExtension
import com.plusgallery.android.extension.StoredExtension
import kotlinx.android.synthetic.main.item_extension.view.*

class ExtensionAdapter<T>(
    val context: Context,
    private val listener: OnItemAction
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    @Suppress("UNCHECKED_CAST")
    var array: Array<T> = arrayOfNulls<Any?>(0) as Array<T>
    val glide = GlideApp.with(context)

    inner class ExtensionHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            if (position == 0) {
                itemView.titleLayout.visibility = View.VISIBLE
            }
            when (val item = array[position]) {
                is RemoteExtension -> {
                    itemView.title.text = "Remote"
                    itemView.name.text = item.label
                    itemView.packageName.text = item.packageName
                    glide.load(item.iconFullPath())
                        .placeholder(R.drawable.ic_baseline_error_24)
                        .into(itemView.icon)
                }
                is StoredExtension -> {
                    itemView.title.text = "Installed"
                    val res = item.getResources(context)
                    itemView.name.text = res.getString(item.label)
                    itemView.packageName.text = item.packageName
                    glide.load(res.getDrawable(item.icon, null))
                        .placeholder(R.drawable.ic_baseline_error_24)
                        .into(itemView.icon)
                }
            }
            itemView.progressBar.visibility = View.GONE
            itemView.linearLayout.setOnClickListener {
                listener.onItemPress(array[position], itemView.linearLayout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExtensionHolder(inflater.inflate(R.layout.item_extension, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ExtensionAdapter<*>.ExtensionHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return array.size
    }
}