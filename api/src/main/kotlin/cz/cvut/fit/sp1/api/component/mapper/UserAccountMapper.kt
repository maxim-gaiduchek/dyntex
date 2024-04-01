package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserAccountMapper {
    fun toDto(userAccount: UserAccount): UserAccountDto

    fun toBean(userAccountDto: UserAccountDto): UserAccount
}
