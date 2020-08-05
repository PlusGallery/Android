package com.plusgallery.extension.model

/**
 * An interface for sorting enums.
 *
 * Enum constants should be implemented in the following
 * order if matched for the sake of integrity.
 * Date, Views, Favorites, Score, Size, Random
 * Others can vary but always at the end of
 * matching ones.
 *
 * Please do NOT implement any ascending/descending
 * variation here. They belong somewhere else.
 *
 * @constructor none
 */
interface SortType {
    /**
     * Get the text value of the constant item.
     * -May be used as UI element-
     *
     * @return A valid string starting with uppercase.
     */
    fun text(): String
    /**
     * Get a valid resource id for a drawable
     *
     * @return A valid resource id
     */
    fun resId(): Int
}