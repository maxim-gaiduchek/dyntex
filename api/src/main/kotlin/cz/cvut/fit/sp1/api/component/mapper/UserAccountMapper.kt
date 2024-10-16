package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.mapstruct.BeforeMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [VideoMapper::class, MaskMapper::class, TagMapper::class])
abstract class UserAccountMapper {

    @Named("toUserAccountDto")
    @Mapping(target = "token", ignore = true)
    abstract fun toDto(userAccount: UserAccount?): UserAccountDto?

    @Named("toUserAccountDtoWithToken")
    abstract fun toDtoWithToken(userAccount: UserAccount?): UserAccountDto?

    abstract fun toEntity(userAccountDto: UserAccountDto?): UserAccount?

    @BeforeMapping
    fun sortMedia(userAccount: UserAccount?) {
        userAccount?.likedVideos?.sortBy { it.createdAt }
        userAccount?.likedMasks?.sortBy { it.createdAt }
        userAccount?.createdVideos?.sortBy { it.createdAt }
        userAccount?.createdMasks?.sortBy { it.createdAt }
    }
}
