package com.biblioteca.repository

import com.biblioteca.model.Livro
import org.springframework.data.jpa.repository.JpaRepository

interface LivroRepository : JpaRepository<Livro, Long>
