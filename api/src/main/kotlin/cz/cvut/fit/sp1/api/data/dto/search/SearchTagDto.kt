package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.dto.TagDto

class SearchTagDto(
    val tags: MutableList<TagDto>,
    override val currentPage: Int,
    override val totalPages: Int,
    override val totalMatches: Long
) : BaseSearchDto()