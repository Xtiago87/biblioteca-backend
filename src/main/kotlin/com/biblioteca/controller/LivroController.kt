package com.biblioteca.controller

import com.biblioteca.model.Livro
import com.biblioteca.repository.LivroRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/livros")
class LivroController(private val livroRepository: LivroRepository) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun criarLivro(@RequestBody livro: Livro): Livro {
        return livroRepository.save(livro)
    }

    @GetMapping
    fun listarLivros(): List<Livro> {
        return livroRepository.findAll()
    }

    @GetMapping("/{id}")
    fun buscarLivro(@PathVariable id: Long): Livro? {
        return livroRepository.findById(id).orElse(null)
    }

    @PutMapping("/{id}")
    fun atualizarLivro(@PathVariable id: Long, @RequestBody livro: Livro): Livro? {
        return livroRepository.findById(id).map {
            val updatedLivro = it.copy(
                titulo = livro.titulo,
                autor = livro.autor,
                categoria = livro.categoria
            )
            livroRepository.save(updatedLivro)
        }.orElse(null)
    }

    @DeleteMapping("/{id}")
    fun deletarLivro(@PathVariable id: Long): Boolean {
        return livroRepository.findById(id).map {
            livroRepository.delete(it)
            true
        }.orElse(false)
    }
}
