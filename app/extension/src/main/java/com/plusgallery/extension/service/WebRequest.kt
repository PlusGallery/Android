package com.plusgallery.extension.service

import com.plusgallery.extension.model.SortType

interface WebRequest {
    companion object {
        /**
         * Per page
         */
        var limit = 20
    }
    var page: Int
    var isMature: Boolean
    var parameters: String
    var sortType: SortType
    var sortAsc: Boolean
    var ownerId: String
    var favById: String
    var poolId: String
}