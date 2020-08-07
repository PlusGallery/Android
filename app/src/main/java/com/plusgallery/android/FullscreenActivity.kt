package com.plusgallery.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plusgallery.android.fragment.PreviewFragment
import com.thefuntasty.hauler.setOnDragDismissedListener
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : AppCompatActivity(), View.OnTouchListener {
    private var mVisible: Boolean = true

    private val detector = GestureDetector(baseContext, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
            if (mVisible) hide() else show()
            return true
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        haulerView.setOnDragDismissedListener {
            finish() // finish activity when dismissed
        }
        viewPager.adapter = object :
            FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return PreviewFragment()
            }

            override fun getItemCount(): Int {
                return 5
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hide() {
        mVisible = false
        // Hide UI first
        supportActionBar?.hide()
        controlsLayout.animate().alpha(0f).setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    controlsLayout.visibility = GONE
                }
            })

        // Hide the system bar
        fullscreen_content.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    private fun show() {
        mVisible = true
        // Show the system bar
        fullscreen_content.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        // Show the UI
        supportActionBar?.show()
        controlsLayout.visibility = VISIBLE
        controlsLayout.animate().alpha(1F).setDuration(200)
            .setStartDelay(100).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    controlsLayout.alpha = 1F
                }
            })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        detector.onTouchEvent(p1)
        return false
    }
}