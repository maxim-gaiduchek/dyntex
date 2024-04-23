package cz.cvut.fit.sp1.api.component.mapper

import cz.cvut.fit.sp1.api.data.dto.TagDto
import cz.cvut.fit.sp1.api.data.model.Tag
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = [UserAccountMapper::class, MediaMapper::class])
abstract class TagMapper {

    abstract fun toDto(tag: Tag?): TagDto?

    abstract fun toDtos(tags: List<Tag?>): List<TagDto?>

    abstract fun toEntity(tagDto: TagDto?): Tag?
}
