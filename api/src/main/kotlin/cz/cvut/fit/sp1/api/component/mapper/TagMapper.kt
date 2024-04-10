package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [UserAccountMapper::class, MediaMapper::class])
interface TagMapper {

    fun toDto(tag: Tag): TagDto

    fun toEntity(tagDto: TagDto): Tag
}
