package com.plusgallery.extension

import com.plusgallery.extension.model.Session
import com.plusgallery.extension.model.SortType
import com.plusgallery.extension.service.RequestHandler
import com.plusgallery.extension.ui.LoginDialog

/**
 * Interface that should be implemented as an object
 * that returns every java class from the extension.
 *
 * Pd: The implementation of this interface must be
 * placed in the root directory and correctly linked
 * in the manifest meta-data.
 *
 * @constructor none
 */
interface BaseInterface {
    val sortArray: Array<SortType>

    val session: Class<Session>
    val requestHandler: Class<RequestHandler>

    val loginDialog: Class<LoginDialog>
}