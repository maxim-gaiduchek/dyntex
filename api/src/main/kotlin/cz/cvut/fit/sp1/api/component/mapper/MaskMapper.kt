package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.MaskDto
import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface MaskMapper {
    @InheritInverseConfiguration
    fun toDto(mask: Mask): MaskDto

    fun toBean(maskDto: MaskDto): Mask
}
