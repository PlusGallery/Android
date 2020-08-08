package com.plusgallery.android.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.plusgallery.android.FullscreenActivity
import com.plusgallery.android.GlideApp
import com.plusgallery.android.R
import com.plusgallery.android.page.SearchPage
import kotlinx.android.synthetic.main.item_preview.view.*

class SubmissionAdapter(private var parent: FullscreenActivity, private var page: SearchPage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val glide = GlideApp.with(parent)

    private val detector = GestureDetector(parent, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
            if (parent.mVisible) parent.hide() else parent.show()
            return true
        }
    })

    inner class PreviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ClickableViewAccessibility")
        fun bindView(position: Int) {
            val submission = page.submissions[position]
            itemView.imageView.isZoomable = false
            itemView.imageView.isTranslatable = false
            parent.setContentType(0)
            glide.load(submission.preview())
                .thumbnail(glide.load(submission.thumbnail()))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(true)
                .dontTransform()
                /*.transition(DrawableTransitionOptions.withCrossFade())*/
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        parent.setContentType(1)
                        itemView.imageView.isZoomable = true
                        itemView.imageView.isTranslatable = true
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        itemView.imageView.setImageResource(R.drawable.ic_baseline_error_24)
                        return false
                    }
                })
                .into(itemView.imageView)

            itemView.imageView.setOnTouchListener { _, event ->
                detector.onTouchEvent(event)
                false
            }
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