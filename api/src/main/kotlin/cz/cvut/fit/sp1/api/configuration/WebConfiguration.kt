package cz.cvut.fit.sp1.api.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:80",
                        "http://localhost:443",
                        "http://localhost:8080"
                    )
                    .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders(
                        "Access-Control-Allow-Headers",
                        "X-Requested-With",
                        "X-HTTP-Method-Override",
                        "Content-Type",
                        "Accept",
                    )
                    .allowCredentials(true)
            }
        }
    }
}
