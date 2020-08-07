package com.plusgallery.android.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plusgallery.android.GlideApp
import com.plusgallery.android.R
import com.plusgallery.android.fragment.tab.SearchTabFragment
import com.plusgallery.extension.model.Submission
import kotlinx.android.synthetic.main.item_submission.view.*

class SearchAdapter(parent: SearchTabFragment, private var list: List<Submission>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val itemAction: OnItemAction = parent
    val glide = GlideApp.with(parent)

    inner class SearchHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(position: Int) {
            val item = list[position]
            itemView.textType.text = item.author()
            itemView.textTags.text = item.title()
            glide.load(item.thumbnail())
                .error(R.drawable.ic_baseline_search_24)
                .into(itemView.imageThumb)
            itemView.verLayout.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    itemAction.onItemPress(position, itemView.imageThumb)
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchHolder(inflater.inflate(R.layout.item_submission, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < list.size)
            (holder as SearchHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}