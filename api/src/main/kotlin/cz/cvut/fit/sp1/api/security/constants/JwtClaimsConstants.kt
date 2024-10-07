package cz.cvut.fit.sp1.api.security.constants

interface JwtClaimsConstants {

    companion object {
        const val USER_ID_KEY: String = "userId"
        const val USER_ROLE_KEY: String = "role"
        const val CREATED_AT: String = "createdAt"
        const val TYPE: String = "type"
    }
}