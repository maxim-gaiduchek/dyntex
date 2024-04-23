package cz.cvut.fit.sp1.api.configuration

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class RestTemplateConfiguration {
    @Bean
    @Lazy
    @Primary
    fun restTemplate(): RestTemplate {
        return RestTemplate(
            HttpComponentsClientHttpRequestFactory().apply {
                this.httpClient = HttpClientBuilder.create().disableCookieManagement().useSystemProperties().build()
            },
        )
    }
}
