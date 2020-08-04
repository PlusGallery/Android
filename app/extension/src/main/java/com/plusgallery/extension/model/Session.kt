package com.plusgallery.extension.model

/**
 * An interface for storing user access data and session.
 *
 * @constructor none
 */
interface Session {
    /**
     * Abstract call to get the user identifier.
     *
     * It could be an username, a number or something
     * else but must be unique and stylized.
     * -May be used as UI element-
     *
     * @return A valid string containing the value.
     */
    fun id(): String
}