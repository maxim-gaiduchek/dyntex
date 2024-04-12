package cz.cvut.fit.sp1.api.data.repository

import cz.cvut.fit.sp1.api.data.model.media.Video
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Video, Long>
