package com.plusgallery.android.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.plusgallery.android.fragment.tab.DetailsTabFragment
import com.plusgallery.android.fragment.tab.SearchTabFragment
import com.plusgallery.android.page.DetailsPage
import com.plusgallery.android.page.PageData
import com.plusgallery.android.page.PageManager
import com.plusgallery.android.page.SearchPage

class TabPagerAdapter(private val pages: PageManager, fragment: Fragment) : FragmentStateAdapter(fragment) {

    fun insert(page: PageData, index: Int = -1): Int {
        var position = index
        if (index == -1) position = pages.size
        pages.add(page)
        notifyItemInserted(position)
        return position
    }

    fun remove(position: Int) {
        pages.removeAt(position)
        notifyItemRemoved(position)
    }

    fun change(position: Int) {
        notifyItemChanged(position)
    }

    fun get(position: Int): PageData {
        return pages[position]
    }

    override fun createFragment(position: Int): Fragment {
        return when (val page = pages[position]) {
            is SearchPage -> SearchTabFragment.newInstance(page)
            is DetailsPage -> DetailsTabFragment.newInstance(page)
        }
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun containsItem(itemId: Long): Boolean {
        pages.forEach {
            if (it.hashCode().toLong() == itemId)
                return true
        }
        return false
    }

    override fun getItemId(position: Int): Long {
        return pages[position].hashCode().toLong()
    }

}