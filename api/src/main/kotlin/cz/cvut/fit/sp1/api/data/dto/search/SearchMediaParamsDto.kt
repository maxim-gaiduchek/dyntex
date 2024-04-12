package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.model.media.Media
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class SearchMediaParamsDto<T : Media>(
        var ids: MutableList<Long>? = mutableListOf(),
        var tags: MutableList<Long>? = mutableListOf(),
        var name: String? = null,
        override var page: Int,
        override var pageSize: Int,
        override var sortBy: String,
        override var sortDirection: String
) : BaseSearchParamsDto<T>(page, pageSize, sortBy, sortDirection) {

    override val sortVariants: Map<String, List<String>> = super.sortVariants.toMutableMap().apply {
        putAll(mapOf(
                "tag" to listOf("tag.id"),
                "name" to listOf("name")
        ))
    }

    override fun getSpecificationPredicates(root: Root<T>, query: CriteriaQuery<*>, builder: CriteriaBuilder): MutableList<Predicate> {
        val predicates = super.getSpecificationPredicates(root, query, builder)

        if (!ids.isNullOrEmpty()) {
            predicates.add(root.get<Any>("id").`in`(ids))
        }

        if (!tags.isNullOrEmpty()) {
            predicates.add(root.join<Any, Any>("tags").get<Any>("id").`in`(tags))
        }

        if (!name.isNullOrBlank()) {
            val pattern = "%%%s%%".format(name!!.lowercase())
            val lower = builder.lower(root.get("name"))
            predicates.add(builder.like(lower, pattern))
        }

        return predicates
    }
}