package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.StatisticDto

interface StatisticService {

    fun buildStatistic(): StatisticDto
}