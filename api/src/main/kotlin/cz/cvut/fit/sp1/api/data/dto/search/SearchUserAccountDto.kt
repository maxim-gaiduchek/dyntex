package cz.cvut.fit.sp1.api.data.dto.search

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto

class SearchUserAccountDto(
    val userAccounts: MutableList<UserAccountDto>,
    override val currentPage: Int,
    override val totalPages: Int,
    override val totalMatches: Long
) : BaseSearchDto()