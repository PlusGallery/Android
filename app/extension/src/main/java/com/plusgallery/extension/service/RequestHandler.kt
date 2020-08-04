package com.plusgallery.extension.service

import com.plusgallery.extension.model.Session

/**
 * A Web-Request handler interface.
 *
 * @constructor none
 */
interface RequestHandler {
    /**
     * Set a valid session for making requests
     */
    fun setSession(session: Session)
    /**
     * Retrieve the actual session from the handler
     */
    fun getSession(): Session
    /**
     * Attempt a login with given credentials,
     * check session or store for later use.
     */
    fun login(call: (r: WebResponse) -> Unit)
    /**
     * Attempt a search and return the parsed response.
     */
    fun search(request: WebRequest, call: (r: WebResponse) -> Unit)
    /**
     * Attempt to release the object and currently executing calls.
     * Should implement an HttpClient close
     */
    fun release()
}