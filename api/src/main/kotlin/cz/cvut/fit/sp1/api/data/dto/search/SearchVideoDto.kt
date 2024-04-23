package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.dto.VideoDto

class SearchVideoDto(
        val videos: MutableList<VideoDto>,
        override val currentPage: Int,
        override val totalPages: Int,
        override val totalMatches: Long
) : BaseSearchDto()