package com.plusgallery.android.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.plusgallery.android.R
import com.plusgallery.extension.model.Submission
import kotlinx.android.synthetic.main.fragment_dialog_information.*
import java.net.URLConnection

class InformationDialog: DialogFragment() {
    private lateinit var submission: Submission

    companion object {
        fun new(sub: Submission): InformationDialog {
            val dialog = InformationDialog()
            dialog.submission = sub
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textTitle.text = submission.title()
        val mime = URLConnection.guessContentTypeFromName(submission.file())
        when (mime.substringBefore('/')) {
            "video" -> imageType.setImageResource(R.drawable.ic_baseline_video_24)
            "image" -> imageType.setImageResource(R.drawable.ic_baseline_image_24)
            else -> imageType.setImageResource(R.drawable.ic_baseline_file_24)
        }
        textMime.text = mime
        textAuthor.text = submission.author()
        textDescription.text = submission.description()
    }
}