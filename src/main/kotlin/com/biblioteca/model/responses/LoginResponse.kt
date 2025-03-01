package br.pucpr.authserver.users.controller.responses

import com.biblioteca.model.Usuario

data class LoginResponse(
    val token: String,
    val user: Usuario,
)
