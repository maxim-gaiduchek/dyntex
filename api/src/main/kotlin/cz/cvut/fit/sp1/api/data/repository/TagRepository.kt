package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {

    fun findAll(specification: Specification<Tag>, pageable: Pageable): Page<Tag>
}
