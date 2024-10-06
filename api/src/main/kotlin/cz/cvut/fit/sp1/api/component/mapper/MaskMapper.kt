package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = [UserAccountMapper::class, TagMapper::class]
)
abstract class MaskMapper {

    @Mapping(target = "createdBy.likedMedia", ignore = true)
    @Mapping(target = "createdBy.createdMedia", ignore = true)
    @Mapping(target = "createdBy.token", ignore = true)
    abstract fun toDto(mask: Mask?): MaskDto?

    abstract fun toEntity(maskDto: MaskDto?): Mask?

    @AfterMapping
    fun enrichWithLikes(@MappingTarget maskDto: MaskDto, mask: Mask?) {
        maskDto.likes = mask?.likedBy?.size ?: 0
    }
}
