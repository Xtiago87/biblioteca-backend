package com.biblioteca.controller

import com.biblioteca.model.Usuario
import com.biblioteca.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
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
}
