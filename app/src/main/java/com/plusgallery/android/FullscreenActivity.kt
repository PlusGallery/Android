package com.plusgallery.android

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.plusgallery.android.adapter.SubmissionAdapter
import com.plusgallery.android.page.SearchPage
import com.plusgallery.android.page.SearchPageAction
import com.plusgallery.android.view.InformationDialog
import com.thefuntasty.hauler.setOnDragDismissedListener
import kotlinx.android.synthetic.main.activity_fullscreen.*

class FullscreenActivity : AppCompatActivity(), SearchPageAction {
    private lateinit var page: SearchPage
    var mVisible: Boolean = true

    val onPageChange = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val prevPosition = page.selectedPos
            page.selectedPos = position
            val submission = page.submissions[position]
            title = getString(R.string.preview_by, submission.author())
            supportActionBar?.subtitle = submission.title()
            textFavs.text = submission.favourites().toString()

            val recycler = viewPager[0] as RecyclerView
            if (prevPosition != position) {
                val prevHolder = recycler.findViewHolderForAdapterPosition(prevPosition) as SubmissionAdapter.PreviewHolder?
                prevHolder?.unloadView()
            }
            val holder = recycler.findViewHolderForAdapterPosition(position) as SubmissionAdapter.PreviewHolder?
            holder?.loadView()
            page.tryAdvancePage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val position = intent.getIntExtra("page", 0)
        page = GApplication.get.pages[position] as SearchPage
        page.setSearchPageAction(this)
        haulerView.setOnDragDismissedListener { finish() }
        // Single click events detection
        viewPager.adapter = SubmissionAdapter(this, page)
        // Setup visible index
        viewPager.setCurrentItem(page.selectedPos, false)
        // https://stackoverflow.com/questions/16074058/onpageselected-doesnt-work-for-first-page
        // For some reason it must be declared before the callback or it will be called before
        // the ViewHolder initialization.
        viewPager.post{ onPageChange.onPageSelected(page.selectedPos) }
        // Setup on page change updater
        viewPager.registerOnPageChangeCallback(onPageChange)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unload current view if exist
        val recycler = viewPager[0] as RecyclerView
        val prevHolder = recycler.findViewHolderForAdapterPosition(page.selectedPos)
                as SubmissionAdapter.PreviewHolder?
        prevHolder?.unloadView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.preview_fullscreen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.preview_info -> {
                val dialog = InformationDialog.new(page.submissions[page.selectedPos])
                dialog.show(supportFragmentManager, InformationDialog::class.simpleName)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setContentType(type: Int) {
        if (type == 0) {
            progressBar.visibility = VISIBLE
            contentType.visibility = GONE
            return
        }
        progressBar.visibility = GONE
        contentType.visibility = VISIBLE
        contentType.setImageResource(type)
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