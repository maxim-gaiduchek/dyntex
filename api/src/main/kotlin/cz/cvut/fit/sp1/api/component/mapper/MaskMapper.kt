package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [UserAccountMapper::class, TagMapper::class])
interface MaskMapper {

    fun toDto(mask: Mask): MaskDto

    fun toEntity(maskDto: MaskDto): Mask
}
