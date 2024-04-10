package cz.cvut.fit.sp1.api.data.dto.search

class SearchMediaParamsDto(
        var ids: MutableList<Long>? = mutableListOf(),
        var tags: MutableList<Long>? = mutableListOf(),
        var name: String? = null,
        override var page: Int,
        override var pageSize: Int,
        override var sortBy: String,
        override var sortDirection: String
) : BaseSearchParamsDto(page, pageSize, sortBy, sortDirection)