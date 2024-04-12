package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.media.Mask
import org.springframework.data.jpa.repository.JpaRepository

interface AvatarRepository : JpaRepository<Mask, Long>