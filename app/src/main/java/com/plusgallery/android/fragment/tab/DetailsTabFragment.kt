package com.plusgallery.android.fragment.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plusgallery.android.R
import com.plusgallery.android.page.DetailsPage
import com.plusgallery.android.page.PageData

class DetailsTabFragment : Fragment() {
    lateinit var data: DetailsPage

    companion object {
        fun newInstance(data: PageData): DetailsTabFragment {
            val instance = DetailsTabFragment()
            instance.data = data as DetailsPage
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_collections, container, false)
    }
}