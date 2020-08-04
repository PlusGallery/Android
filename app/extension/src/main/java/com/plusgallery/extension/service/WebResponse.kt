package com.plusgallery.extension.service

import com.plusgallery.extension.model.Submission

/**
 * A sealed class for defining response states.
 *
 * @constructor none
 */
interface WebResponse {
    /**
     * An enum for defining response states.
     *
     * @constructor none
     */
    enum class Code {
        /**
         * Everything went as planned and response
         * was successful.
         */
        SUCCESS,
        /**
         * There is a problem and must notify the
         * user but execution can continue.
         *
         * I/E: Login failure.
         */
        FAILURE,
        /**
         * A critical error has occurred and cannot
         * continue the execution.
         *
         * I/E: No internet connection
         */
        ERROR;
    }
    /**
     * Returns whether request succeeded or failed
     * and the reason. Use NET_FAILURE for connection
     * issues only since app may retry it. For other
     * api-specific errors always use FAILURE.
     *
     * @return A generic int
     */
    fun responseCode(): Code
    /**
     * Returns a error message describing the error.
     * Must accompany a FAILURE codeResult.
     *
     * @return A valid not blank string
     */
    fun message(): String
    /**
     * Returns whether the final page has been reached
     * or not. Used by the app to decide if request
     * more pages or not.
     *
     * @return A boolean
     */
    fun endReached(): Boolean
    /**
     * Returns a valid list of submissions for
     * the requested properties.
     * ArrayList preferably.
     *
     * @return A valid list
     */
    fun submissions(): MutableList<Submission>
}