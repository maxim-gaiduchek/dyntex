package cz.cvut.fit.sp1.api.data.dto.search

abstract class BaseSearchDto {
    protected abstract val currentPage: Int
    protected abstract val totalPages: Int
    protected abstract val totalMatches: Long
}