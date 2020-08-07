package com.plusgallery.android.page

import android.content.Context
import android.graphics.drawable.Drawable
import com.plusgallery.android.extension.StoredExtension
import com.plusgallery.android.fragment.tab.SearchTabFragment
import com.plusgallery.android.util.Threading
import com.plusgallery.extension.model.Session
import com.plusgallery.extension.model.SortType
import com.plusgallery.extension.model.Submission
import com.plusgallery.extension.service.WebRequest
import com.plusgallery.extension.service.WebResponse

sealed class PageData {
    abstract fun title(): String
    abstract fun icon(context: Context): Drawable
    abstract fun copy(): PageData
    abstract fun close()
}

class SearchPage(val extension: StoredExtension, session: Session): PageData(), WebRequest {
    // Page parameters
    private val handler = extension.baseClass.newRequestHandler
    val sortArray = extension.baseClass.sortArray
    val submissions: ArrayList<Submission> = ArrayList()
    var selectedPos: Int = 0
    var fragment: SearchTabFragment? = null
    var isSearching: Boolean = false
    // WebRequest parameters
    override var page: Int = 1
    override var isMature: Boolean = false
    override var parameters: String = ""
    override var sortType: SortType = sortArray[0]
    override var sortAsc: Boolean = false
    override var ownerId: String = ""
    override var favById: String = ""
    override var poolId: String = ""

    init {
        handler.setSession(session)
    }

    override fun title(): String {
        if (parameters.isEmpty())
            return "Blank search"
        return parameters
    }

    override fun icon(context: Context): Drawable {
        return extension.getResources(context)
            .getDrawable(extension.icon, null)
    }

    override fun copy(): PageData {
        return SearchPage(extension, handler.getSession())
    }

    override fun close() {
        handler.release()
        fragment = null
    }

    fun searchRequest(value: String? = null) {
        if (value != null)
            parameters = value
        isSearching = true
        fragment?.onNewSearchBegin()
        handler.search(this) {
            Threading.sync {
                if (it.responseCode() != WebResponse.Code.SUCCESS)
                    return@sync
                submissions.addAll(it.submissions())
                // Block with a false search when end reached
                isSearching = it.endReached()
                fragment?.onSearchComplete()
            }
        }
    }

    fun advancePage() {
        if (isSearching)
            return
        page++
        isSearching = true
        fragment?.onSearchAdvance()
        handler.search(this) {
            Threading.sync {
                if (it.responseCode() != WebResponse.Code.SUCCESS)
                    return@sync
                val size = submissions.size
                submissions.addAll(it.submissions())
                // Block with a false search when end reached
                isSearching = it.endReached()
                fragment?.onSearchAdvanceComplete(size, submissions.size - 1)
            }
        }
    }
}

class DetailsPage: PageData() {
    override fun title(): String {
        TODO("Not yet implemented")
    }

    override fun icon(context: Context): Drawable {
        TODO("Not yet implemented")
    }

    override fun copy(): PageData {
        return DetailsPage()
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}