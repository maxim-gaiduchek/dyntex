package cz.cvut.fit.sp1.api.configuration

import org.flywaydb.core.Flyway
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class FlywayMigrationConfiguration {

    @Bean
    @Primary
    fun flywayMigrationStrategy(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy { }
    }
}

@Configuration
class FlywayConfiguration(
    flyway: Flyway
) {

    init {
        flyway.migrate()
    }
}