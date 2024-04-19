package cz.cvut.fit.sp1.api.security.model

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import java.util.*

enum class UnsecuredEndpoint(
    private val urlPattern: String,
    private val httpMethod: HttpMethod,
) {

    GET_SWAGGER("/api/swagger-ui/.*", GET),
    GET_API_DOCS("/api/v3/api-docs.*", GET),

    GET_USER("/api/users/[0-9]+", GET),
    GET_USER_LOGIN("/api/users/login", POST),
    POST_USER("/api/users", POST),

    GET_TAG("/api/tags/.*", GET),
    GET_TAGS("/api/tags", GET),

    GET_VIDEO("/api/videos/.*", GET),
    GET_VIDEOS("/api/videos", GET),
    POST_VIDEOS("/api/videos", POST),
    GET_VIDEO_PREVIEW("/api/videos/previews/.*", GET),

    GET_MASK("/api/masks/.*", GET),
    GET_MASKS("/api/masks", GET),
    ;

    companion object {
        fun isEndpointUnsecured(request: HttpServletRequest): Boolean {
            val url = request.requestURI
            val method = HttpMethod.valueOf(request.method)
            return Arrays.stream(entries.toTypedArray())
                .filter {
                    url.matches(
                        it.urlPattern.toRegex()
                    )
                }
                .anyMatch { method == it.httpMethod }
        }
    }
}