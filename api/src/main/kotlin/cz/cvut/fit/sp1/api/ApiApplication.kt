package cz.cvut.fit.sp1.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
	scanBasePackages = ["cz.cvut.fit.sp1.api"],
)
@ConfigurationPropertiesScan
class ApiApplication
fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}