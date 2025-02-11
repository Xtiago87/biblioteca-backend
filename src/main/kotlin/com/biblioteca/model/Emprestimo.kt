package com.biblioteca.model

import java.time.LocalDate
import jakarta.persistence.*

@Entity
data class Emprestimo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    val usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id")
    val livro: Livro,

    val dataEmprestimo: LocalDate = LocalDate.now(),
    val dataDevolucao: LocalDate? = null,

    // Status pode ser "Em andamento" ou "Finalizado"
    val status: String = "Em andamento"
) {
    constructor() : this(0, Usuario(), Livro(), LocalDate.now(), null, "Em andamento")
}
