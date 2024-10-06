package cz.cvut.fit.sp1.api.data.service.interfaces

import cz.cvut.fit.sp1.api.data.dto.StatisticDto

fun interface StatisticService {

    fun buildStatistic(): StatisticDto
}