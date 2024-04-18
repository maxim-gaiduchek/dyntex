package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long>
