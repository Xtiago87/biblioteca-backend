package com.biblioteca.security

import com.biblioteca.model.Usuario
import com.fasterxml.jackson.annotation.JsonIgnore

data class UserToken(
    val id: Long,
    val name: String,
    val roles: Int
) {
    constructor(): this(0, "", 0)
    constructor(user: Usuario): this(
          user.id,
          user.nome,
          user.userRole
    )

    @get:JsonIgnore
    val isAdmin: Boolean get() = roles == 0
}
