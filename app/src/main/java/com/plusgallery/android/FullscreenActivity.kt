package com.plusgallery.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.plusgallery.android.fragment.PreviewFragment
import com.plusgallery.android.page.SearchPage
import com.plusgallery.android.page.SearchPageAction
import com.thefuntasty.hauler.setOnDragDismissedListener
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : AppCompatActivity(), SearchPageAction {
    private lateinit var page: SearchPage
    var mVisible: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        setSupportActionBar(toolBar)
        val position = intent.getIntExtra("page", 0)
        page = (application as GApplication).pages[position] as SearchPage
        page.setSearchPageAction(this)
        haulerView.setOnDragDismissedListener {
            finish() // finish activity when dismissed
        }
        // Single click events detection
        viewPager.adapter = object :
            FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return PreviewFragment.new(page.submissions[position])
            }

            override fun getItemCount(): Int {
                return page.submissions.size
            }
        }
        viewPager.setCurrentItem(page.selectedPos, false)
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page.selectedPos = position
                val submission = page.submissions[position]
                toolBar.title = submission.title()
                toolBar.subtitle = submission.author()
                setContentType(0)
                page.tryAdvanceSearch()
            }
        })
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

    fun setContentType(type: Int) {
        when (type) {
            0 -> {
                progressBar.visibility = VISIBLE
                contentType.visibility = GONE
            }
            1 -> {
                progressBar.visibility = GONE
                contentType.visibility = VISIBLE
                contentType.setImageResource(R.drawable.ic_baseline_image_24)
            }
            2 -> {
                progressBar.visibility = GONE
                contentType.visibility = VISIBLE
                contentType.setImageResource(R.drawable.ic_baseline_video_24)
            }
        }
    }

    fun hide() {
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

    fun show() {
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

    override fun onSearchAdvance() {
        refreshLayout.visibility = VISIBLE
    }

    override fun onSearchAdvanceComplete(from: Int, to: Int) {
        viewPager.adapter?.notifyItemRangeInserted(from, to)
        refreshLayout.visibility = GONE
    }
}