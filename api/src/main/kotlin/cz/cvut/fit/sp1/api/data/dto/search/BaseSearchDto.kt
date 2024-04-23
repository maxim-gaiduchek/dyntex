package cz.cvut.fit.sp1.api.data.dto.search

abstract class BaseSearchDto {
    abstract val currentPage: Int
    abstract val totalPages: Int
    abstract val totalMatches: Long
}