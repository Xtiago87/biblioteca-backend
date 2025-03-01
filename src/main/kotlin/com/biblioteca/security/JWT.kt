package com.biblioteca.security

import com.biblioteca.model.Usuario
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class JWT {
    fun createToken(user: Usuario): String =
        UserToken(user).let {
            Jwts.builder().json(JacksonSerializer())
                .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .subject(user.id.toString())
                .issuedAt(utcNow().toDate())
                .expiration(
                    utcNow().plusHours(
                    if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS
                ).toDate())
                .issuer(ISSUER)
                .claim(USER_FIELD, it)
                .compact()
        }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val header = req.getHeader(AUTHORIZATION)
            if (header == null || !header.startsWith("Bearer ")) return null
            val token = header.removePrefix("Bearer ")
            if (token.isEmpty()) return null

            val claims = Jwts.parser().json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .build()
                .parseSignedClaims(token).payload

            if (claims.issuer != ISSUER) return null
            return claims.get("user", UserToken::class.java).toAuthentication()

        } catch (e: Throwable) {
            log.warn("Token rejected", e)
            return null
        }
    }
    companion object {
        val log = LoggerFactory.getLogger(JWT::class.java)

        const val SECRET="aef154f729347b5894f64477dd62ddc2d72d1e28"
        const val EXPIRE_HOURS = 48L
        const val ADMIN_EXPIRE_HOURS = 1L
        const val ISSUER = "AuthServer"
        const val USER_FIELD = "user"

        private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)
        private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
        private fun UserToken.toAuthentication(): Authentication {
            val authorities = listOf(SimpleGrantedAuthority("ROLE_$roles"))
            return UsernamePasswordAuthenticationToken(this, id, authorities)
        }
    }
}
