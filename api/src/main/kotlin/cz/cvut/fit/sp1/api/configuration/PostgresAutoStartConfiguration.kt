package cz.cvut.fit.sp1.api.configuration

import cz.cvut.fit.sp1.api.postgresRunner.PostgresRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
@ConditionalOnProperty(name = ["postgres-autostart.enabled"], havingValue = "true")
class PostgresAutoStartConfiguration(
    val postgresAutoStartProperties: PostgresAutoStartProperties,
) {

    private var dataSource: DataSource? = null

    @Bean("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    fun getDataSource(postgresRunner: PostgresRunner?): DataSource {
        postgresRunner ?: throw IllegalStateException("Postgres runner is not initialized")
        dataSource?.let { return it }
        postgresRunner.run()
        dataSource = buildDataSource(postgresRunner)
        return dataSource!!
    }

    private fun buildDataSource(postgresRunner: PostgresRunner): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.url(postgresRunner.datasourceUrl)
        dataSourceBuilder.username(postgresAutoStartProperties.user)
        dataSourceBuilder.password(postgresAutoStartProperties.password)
        return dataSourceBuilder.build()
    }
}