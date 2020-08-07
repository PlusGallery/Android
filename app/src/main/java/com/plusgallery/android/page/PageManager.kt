package com.plusgallery.android.page

import android.os.Bundle

class PageManager: ArrayList<PageData>() {
    override fun removeAt(index: Int): PageData {
        val elem = super.removeAt(index)
        elem.close()
        return elem
    }

    fun popSearchPage(bundle: Bundle): SearchPage {
        val index = bundle.getInt( SearchPage::class.simpleName)
        return get(index) as SearchPage
    }

    fun pushSearchPage(bundle: Bundle, page: SearchPage) {
        bundle.putInt(SearchPage::class.simpleName, indexOf(page))
    }
}