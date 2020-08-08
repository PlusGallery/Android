package com.plusgallery.extension.model

import java.util.*

/**
 * An interface for storing submissions data.
 *
 * @constructor none
 */
interface Submission {
    /**
     * Return an unique id for the submission.
     *
     * @return A generic int
     */
    fun id(): Int
    /**
     * Return a valid userId for api requests.
     * In some websites userId is the same as name.
     *
     * @return A valid not blank string
     */
    fun userId(): String
    /**
     * Return whether the picture contains adult
     * content or not.
     *
     * @return A boolean
     */
    fun isAdult(): Boolean
    /**
     * Return the amount of favourites
     *
     * @return A generic int
     */
    fun favourites(): Int
    /**
     * Return post date.
     *
     * @return An initialized Date
     */
    fun date(): Date
    /**
     * Return a thumbnail if available.
     * Return greater size if not existent
     *
     * @return A string url or blank
     */
    fun thumbnail(): String
    /**
     * Return a medium size image if available.
     * Return full size if not existent
     *
     * @return A string url or blank
     */
    fun preview(): String
    /**
     * Return a full size image or video file if available.
     *
     * @return A string url or blank
     */
    fun file(): String
    /**
     * Return pool id's where submission is
     * featured in.
     *
     * @return An array of strings
     */
    fun pools(): Array<Int>
    /**
     * Return an array of tags
     *
     * @return An array of strings
     */
    fun tags(): Array<String>
    /**
     * Return the user name.
     * -May be used as UI element-
     *
     * @return A valid NOT blank string
     */
    fun author(): String
    /**
     * Return formatted title or id if none.
     * -May be used as UI element-
     *
     * @return A valid NOT blank string
     */
    fun title(): String
    /**
     * Return formatted description.
     * Return submission details if not existent.
     * -May be used as UI element-
     *
     * @return A valid NOT blank string
     */
    fun description(): String
}