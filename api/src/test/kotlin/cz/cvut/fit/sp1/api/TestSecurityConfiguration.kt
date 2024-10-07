package cz.cvut.fit.sp1.api

import cz.cvut.fit.sp1.api.security.configuration.UnsecuredEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@Profile("test")
class TestSecurityConfiguration {

    @Bean
    fun testFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
    }

    @Bean
    fun testWebSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { it.ignoring().requestMatchers(UnsecuredEndpoint::isEndpointUnsecured) }
    }
}
