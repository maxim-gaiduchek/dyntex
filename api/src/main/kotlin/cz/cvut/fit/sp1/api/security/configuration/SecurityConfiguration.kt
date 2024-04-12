package cz.cvut.fit.sp1.api.security.configuration

import cz.cvut.fit.sp1.api.data.service.interfaces.UserAccountService
import cz.cvut.fit.sp1.api.security.filter.TokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@Profile("!local") // TODO spring profiles idk how to do it on gradle
class SecurityConfiguration(
        private val userAccountService: UserAccountService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val tokenFilter = TokenFilter(userAccountService)
        return http
                .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
                .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
                .authorizeHttpRequests { it.anyRequest().authenticated() }
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .build()
    }
}