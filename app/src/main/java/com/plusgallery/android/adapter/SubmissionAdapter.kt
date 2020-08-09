package com.plusgallery.android.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.plusgallery.android.FullscreenActivity
import com.plusgallery.android.GlideApp
import com.plusgallery.android.R
import com.plusgallery.android.page.SearchPage
import kotlinx.android.synthetic.main.item_preview.view.*
import java.net.URLConnection

class SubmissionAdapter(private var parent: FullscreenActivity, private var page: SearchPage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var visibleIndex: Int = -1
    val glide = GlideApp.with(parent)

    private val detector = GestureDetector(parent, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
            if (parent.mVisible) parent.hide() else parent.show()
            return true
        }
    })

    inner class PreviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var preview: Bitmap? = null

        @SuppressLint("ClickableViewAccessibility")
        fun bindView(position: Int) {
            val submission = page.submissions[position]
            itemView.imageView.isZoomable = false
            itemView.imageView.isTranslatable = false
            preview = null
            glide.asBitmap()
                .load(submission.thumbnail())
                .error(R.drawable.ic_baseline_error_24)
                .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    preview = resource
                    if (visibleIndex != position)
                        itemView.imageView.setImageBitmap(preview!!)
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    preview = null
                }
            })
            itemView.imageView.setOnTouchListener { _, event ->
                detector.onTouchEvent(event)
                false
            }
        }

        fun unloadView(position: Int) {
            if (position !in 0..page.submissions.size)
                return
            itemView.imageView.isZoomable = false
            itemView.imageView.isTranslatable = false
            itemView.imageView.setImageBitmap(preview)
        }

        fun loadView(position: Int) {
            // Set the currently visible position
            visibleIndex = position
            // Handle submission
            val submission = page.submissions[position]
            parent.setContentType(position,0)
            glide.load(submission.preview())
                .placeholder(BitmapDrawable(parent.resources, preview))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .skipMemoryCache(true)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        // true if the listener has handled setting the resource on the target,
                        // false to allow Glide's request to update the target
                        if (visibleIndex == position) {
                            parent.setContentType(position,1)
                            itemView.imageView.isZoomable = true
                            itemView.imageView.isTranslatable = true
                            return false
                        }
                        return true
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        if (visibleIndex == position) {
                            parent.setContentType(position,1)
                        }
                        itemView.imageView.setImageResource(R.drawable.ic_baseline_error_24)
                        return true
                    }
                })
                .into(itemView.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PreviewHolder(inflater.inflate(R.layout.item_preview, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PreviewHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return page.submissions.size
    }
}