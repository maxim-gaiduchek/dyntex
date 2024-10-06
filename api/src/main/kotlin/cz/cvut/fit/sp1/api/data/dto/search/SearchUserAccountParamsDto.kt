package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.model.UserAccount
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root

class SearchUserAccountParamsDto(
    private var ids: MutableList<Long>? = mutableListOf(),
    private var name: String? = null,
    override var page: Int = 1,
    override var pageSize: Int = 20,
    override var sortBy: String = "createdAt",
    override var sortDirection: String = "desc",
) : BaseSearchParamsDto<UserAccount>(page, pageSize, sortBy, sortDirection) {

    override val sortVariants: Map<String, List<String>> = super.sortVariants.toMutableMap().apply {
        putAll(
            mapOf(
                "name" to listOf("name")
            )
        )
    }

    override fun getSpecificationPredicates(
        root: Root<UserAccount>,
        query: CriteriaQuery<*>,
        builder: CriteriaBuilder
    ): MutableList<Predicate> {
        val predicates = super.getSpecificationPredicates(root, query, builder)

        if (!ids.isNullOrEmpty()) {
            predicates.add(root.get<Any>("id").`in`(ids))
        }

        if (!name.isNullOrBlank()) {
            val pattern = "%%%s%%".format(name!!.lowercase())
            val lower = builder.lower(root.get("name"))
            predicates.add(builder.like(lower, pattern))
        }

        return predicates
    }
}