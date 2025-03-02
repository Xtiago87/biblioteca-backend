package com.biblioteca.controller

import com.biblioteca.model.Emprestimo
import com.biblioteca.repository.EmprestimoRepository
import com.biblioteca.repository.LivroRepository
import com.biblioteca.repository.UsuarioRepository
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/emprestimos")
class EmprestimoController(
    private val emprestimoRepository: EmprestimoRepository,
    private val livroRepository: LivroRepository,
    private val usuarioRepository: UsuarioRepository
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    fun criarEmprestimo(@RequestParam usuarioId: Long, @RequestParam livroId: Long): Emprestimo {
        val livro = livroRepository.findById(livroId).orElseThrow { IllegalArgumentException("Livro não encontrado") }

        if(livro.emprestado){
            throw IllegalArgumentException("Livro já emprestado!")
        }

        val updatedLivro = livro.copy(
            titulo = livro.titulo,
            autor = livro.autor,
            categoria = livro.categoria,
            emprestado = true
        )

        livroRepository.save(updatedLivro)

        val usuario = usuarioRepository.findById(usuarioId).orElseThrow { IllegalArgumentException("Usuário não encontrado") }


        val emprestimo = Emprestimo(usuario = usuario, livro = updatedLivro)
        return emprestimoRepository.save(emprestimo)
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    fun listarEmprestimos(): List<Emprestimo> {
        return emprestimoRepository.findAll()
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('LIBRARIAN')")
    fun finalizarEmprestimo(@PathVariable id: Long): Emprestimo {
        val emprestimo = emprestimoRepository.findById(id).orElseThrow { IllegalArgumentException("Empréstimo não encontrado") }
        val dataDevolucao = java.time.LocalDate.now()

        val livro = livroRepository.findById(emprestimo.livro.id).orElseThrow { IllegalArgumentException("Livro não encontrado") }
        val updatedLivro = livro.copy(
            titulo = livro.titulo,
            autor = livro.autor,
            categoria = livro.categoria,
            emprestado = false
        )

        livroRepository.save(updatedLivro)

        val emprestimoAtualizado = emprestimo.copy(status = "Finalizado", dataDevolucao = dataDevolucao)
        return emprestimoRepository.save(emprestimoAtualizado)
    }
}
