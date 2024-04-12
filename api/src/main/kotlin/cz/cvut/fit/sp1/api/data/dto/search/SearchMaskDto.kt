package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.dto.MaskDto

class SearchMaskDto(
        val masks: MutableList<MaskDto>,
        override val currentPage: Int,
        override val totalPages: Int,
        override val totalMatches: Long
) : BaseSearchDto()