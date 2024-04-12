package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import io.vavr.control.Try
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification

open class BaseSearchParamsDto<T : StandardAuditModel>(
        protected open var page: Int = 1,
        protected open var pageSize: Int = 20,
        protected open var sortBy: String = "createdAt",
        protected open var sortDirection: String = "desc"
) {

    protected open val sortVariants: Map<String, List<String>> = mapOf(
            "id" to listOf("id"),
            "createdAt" to listOf("createdAt")
    )

    companion object {
        const val DEFAULT_SORT_BY: String = "createdAt"
        val DEFAULT_SORT_DIRECTION: Sort.Direction = Sort.Direction.DESC
    }

    fun buildSpecification(): Specification<T> {
        return Specification { root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder ->
            val predicates: List<Predicate> = getSpecificationPredicates(root, query, builder)
            builder.and(*predicates.toTypedArray<Predicate>())
        }
    }

    protected open fun getSpecificationPredicates(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): MutableList<Predicate> {
        return mutableListOf()
    }

    fun buildPageable(): Pageable {
        return PageRequest.of(page - 1, pageSize, buildSort())
    }

    protected fun buildSort(): Sort {
        val direction = Try.of { this.getSortDirection() }
                .getOrElse(DEFAULT_SORT_DIRECTION)
        val property = sortVariants.getOrDefault(sortBy, mutableListOf(DEFAULT_SORT_BY))
        return Sort.by(direction, *property.toTypedArray())
    }

    protected fun getSortDirection(): Sort.Direction {
        return Sort.Direction.fromString(sortDirection)
    }
}