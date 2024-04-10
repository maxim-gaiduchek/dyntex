package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.media.Video
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<Video, Long> {

    fun findAll(specification: Specification<Video>?, pageable: Pageable?): Page<Video>
}
