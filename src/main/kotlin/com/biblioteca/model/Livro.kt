package com.biblioteca.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class Livro(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    val titulo: String = "",
    val autor: String = "",
    val categoria: String = "",
    val emprestado: Boolean = false
) {

    constructor() : this(0, "", "", "", false)
}
