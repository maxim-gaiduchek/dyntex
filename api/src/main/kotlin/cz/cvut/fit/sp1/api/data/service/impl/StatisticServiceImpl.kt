package cz.cvut.fit.sp1.api.data.service.impl

import cz.cvut.fit.sp1.api.data.dto.StatisticDto
import cz.cvut.fit.sp1.api.data.service.interfaces.*
import org.springframework.stereotype.Service

@Service
class StatisticServiceImpl(
    private val userAccountService: UserAccountService,
    private val videoService: VideoService,
    private val maskService: MaskService,
    private val tagService: TagService
) : StatisticService {

    override fun buildStatistic(): StatisticDto {
        return StatisticDto(
            usersCount = userAccountService.countAll(),
            videosCount = videoService.countAll(),
            masksCount = maskService.countAll(),
            tagsCount = tagService.countAll(),
        )
    }
}