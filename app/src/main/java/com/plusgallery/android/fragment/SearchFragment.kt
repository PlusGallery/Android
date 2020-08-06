package com.plusgallery.android.fragment

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.plusgallery.android.GApplication
import com.plusgallery.android.R
import com.plusgallery.android.view.SearchBar
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.include_search_fragment.*


class SearchFragment : Fragment(), SearchBar.OnSearchBarListener, View.OnClickListener,
    TabLayoutMediator.TabConfigurationStrategy {
    private lateinit var app: GApplication

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
        /*adapter = TabPagerAdapter(app.pages, childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager, this).attach()*/
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun onNavigationClicked() {
        btnClose.setOnClickListener { drawerLayout.closeDrawers() }
        btnApply.setOnClickListener {
            drawerLayout.closeDrawers()
        }
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        TODO("Not yet implemented")
    }
}