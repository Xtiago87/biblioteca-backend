package com.biblioteca.controller

import br.pucpr.authserver.users.controller.requests.LoginRequest
import com.biblioteca.model.Usuario
import com.biblioteca.repository.UsuarioRepository
import com.biblioteca.security.JWT
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository,
    private val jwt: JWT
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criarUsuario(@RequestBody usuario: Usuario): Usuario {
        return usuarioRepository.save(usuario)
    }

    @GetMapping
    fun listarUsuarios(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    @GetMapping("/{id}")
    fun buscarUsuario(@PathVariable id: Long): Usuario {
        return usuarioRepository.findById(id).orElseThrow { IllegalArgumentException("Usuário não encontrado") }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletarUsuario(@PathVariable id: Long) {
        val usuario = usuarioRepository.findById(id).orElseThrow { IllegalArgumentException("Usuário não encontrado") }
        usuarioRepository.delete(usuario)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val usuario = usuarioRepository.findByEmail(loginRequest.email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(" ou senha inválidos")

        System.out.println(loginRequest.password)
        System.out.println(usuario.password)

        if (loginRequest.password != usuario.password) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos")
        }

        val token = jwt.createToken(usuario)
        return ResponseEntity.ok(mapOf("token" to token))
    }

}
