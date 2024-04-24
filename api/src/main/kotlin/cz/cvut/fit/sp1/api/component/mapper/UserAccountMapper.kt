package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.UserAccountDto
import cz.cvut.fit.sp1.api.data.model.UserAccount
import org.mapstruct.*

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [MediaMapper::class, TagMapper::class])
abstract class UserAccountMapper {

    @Mapping(target = "token", ignore = true)
    abstract fun toDto(userAccount: UserAccount?): UserAccountDto

    abstract fun toDtoWithToken(userAccount: UserAccount?): UserAccountDto

    abstract fun toEntity(userAccountDto: UserAccountDto?): UserAccount

    @BeforeMapping
    fun sortMedia(userAccount: UserAccount?) {
        userAccount?.likedMedia?.sortBy { it.createdAt }
        userAccount?.createdMedia?.sortBy { it.createdAt }
    }

    @AfterMapping
    fun enrichWithLikedVideosIds(user: UserAccount, @MappingTarget userAccountDto: UserAccountDto) {
        userAccountDto.likedVideosIds = user.likedVideos.map { it.id!! }.toMutableList()
    }
}
