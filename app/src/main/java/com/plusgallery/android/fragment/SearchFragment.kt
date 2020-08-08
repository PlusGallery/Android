package com.plusgallery.android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.adapter.TabPagerAdapter
import com.plusgallery.android.extension.StoredExtension
import com.plusgallery.android.page.PageData
import com.plusgallery.android.page.SearchPage
import com.plusgallery.android.view.ExtensionSelectDialog
import com.plusgallery.android.view.IconPopupMenu
import com.plusgallery.android.view.SearchBar
import com.plusgallery.extension.ui.UIContextWrapper
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.include_search_fragment.*


class SearchFragment : Fragment(), SearchBar.OnSearchBarListener, View.OnLongClickListener,
    View.OnClickListener, PopupMenu.OnMenuItemClickListener, ExtensionSelectDialog.OnNewAction,
    TabLayoutMediator.TabConfigurationStrategy {
    private lateinit var app: GApplication
    lateinit var adapter: TabPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app = requireActivity().application as GApplication
        // Initialize searchBar
        searchBar.setOnSearchBarListener(this)
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset == 0) { controlFab.show() }
            else { controlFab.hide() }
        })
        // Initialize fab controls
        controlFab.setOnClickListener(this)
        // Initialize tabLayout
        adapter = TabPagerAdapter(app.pages, this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager, this).attach()
    }

    override fun onNavigationClicked() {
        val page = getSelectedPage()
        if (page !is SearchPage)
            return
        safeMode.isChecked = page.isMature
        ownerId.setText(page.ownerId)
        favById.setText(page.favById)
        poolId.setText(page.poolId)
        btnClose.setOnClickListener { drawerLayout.closeDrawers() }
        btnApply.setOnClickListener {
            page.isMature = safeMode.isChecked
            page.ownerId = ownerId.text.toString()
            page.favById = favById.text.toString()
            page.poolId = poolId.text.toString()
            drawerLayout.closeDrawers()
            page.searchRequest()
        }
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (!enabled)
            return
        val selected = tabLayout.selectedTabPosition
        if (selected < 0) { // Not active tabs
            return
        }
        val page = app.pages[selected]
        if (page is SearchPage)
            searchBar.setText(page.parameters)
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        searchBar.closeSearch()
        val selected = tabLayout.selectedTabPosition
        if (selected < 0) {
            val selectDialog = ExtensionSelectDialog.new(requireActivity().supportFragmentManager,
                app.extensions.storedArray())
            selectDialog.setOnExtensionSelect(this)
            selectDialog.show(-1, text.toString())
            return
        }
        val page = app.pages[selected]
        if (page is SearchPage) {
            page.searchRequest(text.toString())
            onConfigureTab(tabLayout.getTabAt(selected)!!, selected)
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.view.setOnLongClickListener(this)
        val page = app.pages[position]
        tab.text = page.title()
        tab.icon = page.icon(requireContext())
    }

    override fun onLongClick(v: View): Boolean {
        // Force-focus in tab in case it's not
        v.performClick()

        val menu = IconPopupMenu(requireContext(), v)
        menu.inflate(R.menu.fragment_tab_menu)
        menu.setOnMenuItemClickListener(this)
        menu.show()
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_new -> {
                val position = tabLayout.selectedTabPosition
                if (position < 0)
                    return true
                val page = app.pages[position].copy()
                appendNewPage(position, page, false)

            }
            R.id.tab_close -> adapter.remove(tabLayout.selectedTabPosition)
        }
        return true
    }

    override fun onClick(v: View) {
        if (searchBar.isSearchOpened())
            searchBar.closeSearch()
        val selectDialog = ExtensionSelectDialog.new(requireActivity().supportFragmentManager,
            app.extensions.storedArray())
        selectDialog.setOnExtensionSelect(this)
        selectDialog.show()
    }

    override fun onExtensionSelect(index: Int, text: String?, extension: StoredExtension) {
        val page = SearchPage(extension, extension.baseClass.session.newInstance())
        val position = appendNewPage(index, page, true)
        if (text != null) {
            page.searchRequest(text)
            onConfigureTab(tabLayout.getTabAt(position)!!, position)
        }
    }

    private fun getSelectedPage(): PageData? {
        val position = tabLayout.selectedTabPosition
        if (position < 0)
            return null
        return app.pages[position]
    }

    private fun appendNewPage(index: Int, page: PageData, scroll: Boolean = false): Int {
        val position = adapter.insert(page, index)
        if (scroll) {
            val tab = tabLayout.getTabAt(position)
            tabLayout.selectTab(tab)
        }
        return position
    }
}