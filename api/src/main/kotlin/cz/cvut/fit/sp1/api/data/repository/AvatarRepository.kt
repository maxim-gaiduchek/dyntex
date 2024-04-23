package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.media.Avatar
import org.springframework.data.jpa.repository.JpaRepository

interface AvatarRepository : JpaRepository<Avatar, Long>