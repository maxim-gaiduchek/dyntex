package cz.cvut.fit.sp1.api.postgresRunner

import DockerContainerRunner
import cz.cvut.fit.sp1.api.configuration.PostgresAutoStartProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PostgresRunner(
    private val postgresAutoStartProperties: PostgresAutoStartProperties,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var instance: DockerContainerRunner? = null

    fun run() {
        instance =
            DockerContainerRunner(
                image = "postgres:13",
                expose = mapOf(postgresAutoStartProperties.port to "5432"),
                waitForLog = "database system is ready to accept connections",
                waitForLogTimesForNewContainer = 2,
                waitForLogTimesForExistingContainer = 1,
                rm = false,
                name = postgresAutoStartProperties.containerName,
                stopBeforeStart = false,
                env =
                mapOf(
                    "POSTGRES_PASSWORD" to postgresAutoStartProperties.password,
                    "POSTGRES_USER" to postgresAutoStartProperties.user,
                    "POSTGRES_DB" to postgresAutoStartProperties.databaseName,
                ),
                command =
                "postgres -c max_connections=10000 -c random_page_cost=1.0 " +
                        "-c fsync=off -c synchronous_commit=off -c full_page_writes=off",
                timeout = 300000,
            ).also {
                logger.info("Starting Postgres Docker container")
                it.run()
            }
    }

    fun stop() {
        instance?.let {
            logger.info("Stopping Postgres container")
            it.stop()
        }
    }

    val datasourceUrl by lazy {
        "jdbc:postgresql://localhost:${postgresAutoStartProperties.port}/${postgresAutoStartProperties.databaseName}"
    }
}
