package com.plusgallery.android.fragment.tab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.plusgallery.android.FullscreenActivity
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.adapter.OnItemAction
import com.plusgallery.android.adapter.SearchAdapter
import com.plusgallery.android.page.PageData
import com.plusgallery.android.page.SearchPage
import kotlinx.android.synthetic.main.fragment_tab_search.*

class SearchTabFragment : Fragment(), TabLayout.OnTabSelectedListener, OnItemAction,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var app: GApplication
    private lateinit var page: SearchPage
    private lateinit var mAdapter: SearchAdapter
    private lateinit var mLayoutManager: GridLayoutManager

    companion object {
        fun newInstance(data: PageData): SearchTabFragment {
            val instance = SearchTabFragment()
            instance.page = data as SearchPage
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set properties here in case app.page called before
        // view called
        app = requireActivity().application as GApplication
        if (savedInstanceState != null) {
            page = app.pages.popSearchPage(savedInstanceState)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        page.fragment = this
        // Populate sort layout with tabs
        sortSwitch.isChecked = page.sortAsc
        sortSwitch.setOnCheckedChangeListener(this)
        page.sortArray.forEachIndexed { i, it ->
            val tab = sortLayout.newTab()
            tab.text = it.text()
            tab.icon = page.extension.getResources(requireContext()).getDrawable(it.resId(), null)
            tab.view.isClickable = false
            sortLayout.addTab(tab)
            if (page.sortType == page.sortArray[i])
                sortLayout.selectTab(tab, true)
        }
        sortLayout.addOnTabSelectedListener(this)
        // SwipeRefreshLayout
        swipeRefreshLayout.isEnabled = false
        swipeRefreshLayout.isRefreshing = true
        // Activate recyclerview
        mLayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = SearchAdapter(this, page.submissions)
        recyclerView.adapter = mAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position = mLayoutManager.findLastVisibleItemPosition()
                if (page.submissions.size - position < 6) {
                    Log.w("Advancing", position.toString())
                    page.advancePage()
                }
            }
        })

        // Check first search
        if (!page.isSearching)
            onSearchComplete()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        app.pages.pushSearchPage(outState, page)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        // In case of fragment being deleted
        // remove it's reference from the page
        if (this::page.isInitialized)
            page.fragment = null
        super.onDestroy()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (page.isSearching)
            return
        page.sortAsc = isChecked
        page.searchRequest()
    }

    override fun onTabReselected(tab: TabLayout.Tab) {}

    override fun onTabUnselected(tab: TabLayout.Tab) {}

    override fun onTabSelected(tab: TabLayout.Tab) {
        if (page.isSearching)
            return
        val position = sortLayout.selectedTabPosition
        page.sortType = page.sortArray[position]
        page.searchRequest()
    }

    override fun onItemPress(position: Any?, view: View) {
        /*page.selectedPos = position
        val intent = Intent(activity, FullscreenActivity::class.java)
        intent.putExtra("page", app.pages.indexOf(page))
        startActivity(intent, activityCircularReveal(view))*/
    }

    fun onNewSearchBegin() {
        page.submissions.clear()
        mAdapter.notifyDataSetChanged()
        onSearchAdvance()
    }

    fun onSearchAdvance() {
        sortSwitch.isEnabled = false
        enableTabs(false)
        swipeRefreshLayout.isRefreshing = true
    }

    fun onSearchComplete() {
        mAdapter.notifyDataSetChanged()
        sortSwitch.isEnabled = true
        enableTabs(true)
        swipeRefreshLayout.isRefreshing = false
    }

    fun onSearchAdvanceComplete(from: Int, to: Int) {
        mAdapter.notifyItemRangeInserted(from, to)
        sortSwitch.isEnabled = true
        enableTabs(true)
        swipeRefreshLayout.isRefreshing = false
    }

    private fun enableTabs(boolean: Boolean) {
        for (i in 0 until sortLayout.tabCount) {
            sortLayout.getTabAt(i)!!.view.isClickable = boolean
        }
    }
}