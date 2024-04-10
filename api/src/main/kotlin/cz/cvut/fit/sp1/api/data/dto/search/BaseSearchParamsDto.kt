package cz.cvut.fit.sp1.api.data.dto.search

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification

open class BaseSearchParamsDto(
        protected open var page: Int = 1,
        protected open var pageSize: Int = 20,
        protected open var sortBy: String = "createdAt",
        protected open var sortDirection: String = "desc"
) {


    protected val sortVariants: Map<String, List<String>> = HashMap()
    protected val DEFAULT_SORT_BY: String = "createdAt"
    protected val DEFAULT_SORT_DIRECTION: Sort.Direction = Sort.Direction.DESC

    /*fun buildSpecification(): Specification<T?> {
        return Specification<T?> { root: Root<T?>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
            val predicates: List<Predicate> = getSpecificationPredicates(root, query, builder)
            builder.and(*predicates.toTypedArray<Predicate>())
        }
    }

    fun buildPageable(): Pageable {
        return PageRequest.of(page - 1, pageSize, buildSort())
    }

    private fun buildSort(): Sort {
        val direction: Unit = Try.of { this.getSortDirection() }
                .getOrElse(DEFAULT_SORT_DIRECTION)
        val property = sortVariants.getOrDefault(sortBy, mutableListOf(DEFAULT_SORT_BY))
        return Sort.by(direction, *property)
    }

    protected fun getSortDirection(): Sort.Direction {
        return Sort.Direction.fromString(sortDirection)
    }*/
}