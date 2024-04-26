package cz.cvut.fit.sp1.api.controller

import cz.cvut.fit.sp1.api.data.dto.StatisticDto
import cz.cvut.fit.sp1.api.data.service.interfaces.StatisticService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/statistics")
class StatisticController(
    private val statisticService: StatisticService
) {

    @GetMapping
    fun buildStatistic(): StatisticDto {
        return statisticService.buildStatistic()
    }
}