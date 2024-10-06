package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface MaskRepository : JpaRepository<Mask, Long> {

    fun findAll(specification: Specification<Mask>, pageable: Pageable): Page<Mask>
}