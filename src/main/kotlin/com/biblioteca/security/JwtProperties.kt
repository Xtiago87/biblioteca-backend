package com.biblioteca.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    lateinit var SECRET: String
    var EXPIRE_HOURS: Long = 0
    var ADMIN_EXPIRE_HOURS: Long = 0
    lateinit var ISSUER: String
    lateinit var USER_FIELD: String
}