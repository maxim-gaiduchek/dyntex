package cz.cvut.fit.sp1.api.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie

object CookieUtils {

    fun getCookie(cookies: Array<Cookie?>?, name: String): Cookie? {
        cookies ?: return null
        return cookies.firstOrNull { cookie: Cookie? -> cookie!!.name == name }
    }

    fun deleteCookie(cookies: Array<Cookie?>?, httpResponse: HttpServletResponse, name: String) {
        cookies ?: return
        val cookie = getCookie(cookies, name)
        cookie ?: return
        cookie.value = null
        cookie.maxAge = 0
        httpResponse.addCookie(cookie)
    }

    fun createHttpOnlyCookie(name: String, value: String, path: String, maxAge: Long): ResponseCookie {
        return ResponseCookie
            .from(name, value)
            .httpOnly(true)
            .path(path)
            .maxAge(maxAge)
            .sameSite("None")
            .build()
    }
}