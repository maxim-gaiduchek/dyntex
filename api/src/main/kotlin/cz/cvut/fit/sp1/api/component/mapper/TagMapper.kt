package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface TagMapper {
    fun toDto(tag: Tag): TagDto

    fun toBean(tagDto: TagDto): Tag
}
