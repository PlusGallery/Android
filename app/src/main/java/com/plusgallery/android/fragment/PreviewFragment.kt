package com.plusgallery.android.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.plusgallery.android.R
import kotlinx.android.synthetic.main.fragment_preview.*

class PreviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}