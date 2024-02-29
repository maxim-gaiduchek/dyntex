package cz.cvut.fit.sp1.api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "postgres-autostart")
class PostgresAutoStartProperties {
    var enabled: Boolean = true

    var user: String = "postgres"

    var password: String = "postgres"

    var databaseName: String = "dyntex-db"

    var port: String = "5432"

    var containerName: String = "sp"
}