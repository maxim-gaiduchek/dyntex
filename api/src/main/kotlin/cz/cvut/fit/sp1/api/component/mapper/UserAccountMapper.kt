package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [MediaMapper::class, TagMapper::class])
interface UserAccountMapper {

    fun toDto(userAccount: UserAccount): UserAccountDto

    fun toEntity(userAccountDto: UserAccountDto): UserAccount
}
