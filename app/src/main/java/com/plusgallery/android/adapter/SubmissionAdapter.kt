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
import com.plusgallery.extension.model.Submission
import kotlinx.android.synthetic.main.item_preview.view.*
import java.net.URLConnection

class SubmissionAdapter(private var parent: FullscreenActivity, private var page: SearchPage) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val glide = GlideApp.with(parent)

    inner class PreviewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        lateinit var submission: Submission
        var preview: Bitmap? = null
        var largeTarget: Target<*>? = null
        var thumbTarget = object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                preview = resource
                // In case of submission not being selected one
                if (largeTarget == null)
                    itemView.imageView.setImageBitmap(preview!!)
            }
            override fun onLoadCleared(placeholder: Drawable?) {
                preview = null
            }
        }

        val onObjectReady = object : RequestListener<Drawable> {
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                         dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                parent.setContentType(R.drawable.ic_baseline_image_24)
                itemView.imageView.isZoomable = true
                itemView.imageView.isTranslatable = true
                return false // allow Glide's request to update target
            }

            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                parent.setContentType(R.drawable.ic_baseline_image_24)
                itemView.imageView.setImageResource(R.drawable.ic_baseline_error_24)
                return true
            }
        }

        fun bindView(position: Int) {
            submission = page.submissions[position]

            itemView.imageView.isZoomable = false
            itemView.imageView.isTranslatable = false
            itemView.imageView.setImageResource(0)
            glide.clear(thumbTarget)
            glide.asBitmap().load(submission.thumbnail())
                .into(thumbTarget)
            itemView.imageView.setOnClickListener(this)
            itemView.videoView.setOnClickListener(this)
        }

        fun unloadView() {
            glide.clear(largeTarget)
            largeTarget = null

            itemView.videoView.stopPlayback()
            itemView.videoView.visibility = View.GONE

            itemView.imageView.visibility = View.VISIBLE
            itemView.imageView.isZoomable = false
            itemView.imageView.isTranslatable = false
            itemView.imageView.setImageBitmap(preview)
        }

        fun loadView() {
            parent.setContentType(0)

            val mime = URLConnection.guessContentTypeFromName(submission.file())
            when (mime.substringBefore('/')) {
                "video" -> {
                    itemView.videoView.visibility = View.VISIBLE
                    itemView.videoView.setVideoURI(Uri.parse(submission.file()))
                    itemView.videoView.setOnPreparedListener {
                        itemView.imageView.visibility = View.GONE
                        parent.setContentType(R.drawable.ic_baseline_video_24)
                        it.isLooping = true
                        itemView.videoView.start()
                    }
                }
                "image" -> {
                    largeTarget = glide.load(submission.preview())
                        .placeholder(BitmapDrawable(parent.resources, preview))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE).skipMemoryCache(true)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(onObjectReady).into(itemView.imageView)
                }
                else -> parent.setContentType(R.drawable.ic_baseline_file_24)
            }
        }

        override fun onClick(p0: View?) {
            if (parent.mVisible) parent.hide() else parent.show()
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