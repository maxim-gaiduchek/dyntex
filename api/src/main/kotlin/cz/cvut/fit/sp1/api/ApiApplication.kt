package cz.cvut.fit.sp1.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
