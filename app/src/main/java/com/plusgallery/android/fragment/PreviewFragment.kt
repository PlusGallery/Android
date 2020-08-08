package com.plusgallery.android.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.plusgallery.android.FullscreenActivity
import com.plusgallery.android.GlideApp
import com.plusgallery.android.R
import com.plusgallery.extension.model.Submission
import kotlinx.android.synthetic.main.fragment_preview.*

class PreviewFragment : Fragment(), View.OnTouchListener {
    private lateinit var submission: Submission
    private lateinit var parent: FullscreenActivity
    private val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
            if (parent.mVisible) parent.hide() else parent.show()
            return true
        }
    })

    companion object {
        fun new(sub: Submission): PreviewFragment {
            val fragment = PreviewFragment()
            fragment.submission = sub
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = activity as FullscreenActivity
        imageView.setOnTouchListener(this)
        imageView.isZoomable = false
        imageView.isTranslatable = false
        val glide = GlideApp.with(this)
        glide.load(submission.file())
            .thumbnail(glide.load(submission.thumbnail()))
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    parent.setContentType(1)
                    imageView.isZoomable = true
                    imageView.isTranslatable = true
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    imageView.setImageResource(R.drawable.ic_baseline_error_24)
                    return false
                }
            })
            .into(imageView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        detector.onTouchEvent(p1)
        return false
    }
}