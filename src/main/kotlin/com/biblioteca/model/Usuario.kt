package com.biblioteca.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    val nome: String = "",

    @Column(unique = true)
    val email: String = "",

    val password: String = "",

    // 0 == usuario normal
    // 1 == bibliotecario
    val userRole : Int = 0
) {
    constructor() : this(0, "", "","", 0)
}
