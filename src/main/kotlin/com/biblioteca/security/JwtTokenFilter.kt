package com.biblioteca.security

import com.biblioteca.security.JWT
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

@Component
class JwtTokenFilter(private val jwt: JWT): GenericFilterBean() {
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        val auth = jwt.extract(request)
        if (auth != null) SecurityContextHolder.getContext().authentication = auth

        if (request.method == "DELETE") {
            val user = auth?.principal as? UserToken
            if (user?.roles != 1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Operação não permitida.")
                return
            }
        }
        chain.doFilter(req, res)
    }

}
