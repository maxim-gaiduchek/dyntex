package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.model.media.Media
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class SearchMediaParamsDto<T : Media>(
    private var ids: MutableList<Long>? = mutableListOf(),
    private var createdBy: MutableList<Long>? = mutableListOf(),
    private var tags: MutableList<Long>? = mutableListOf(),
    private var name: String? = null,
    override var page: Int = 1,
    override var pageSize: Int = 20,
    override var sortBy: String = "createdAt",
    override var sortDirection: String = "desc",
) : BaseSearchParamsDto<T>(page, pageSize, sortBy, sortDirection) {

    override val sortVariants: Map<String, List<String>> = super.sortVariants.toMutableMap().apply {
        putAll(
            mapOf(
                "tag" to listOf("tag.id"),
                "name" to listOf("name")
            )
        )
    }

    override fun getSpecificationPredicates(
        root: Root<T>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): MutableList<Predicate> {
        val predicates = super.getSpecificationPredicates(root, query, builder)

        if (!ids.isNullOrEmpty()) {
            predicates.add(root.get<Any>("id").`in`(ids))
        }

        if (!createdBy.isNullOrEmpty()) {
            predicates.add(root.join<Any, Any>("createdBy").get<Any>("id").`in`(createdBy))
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