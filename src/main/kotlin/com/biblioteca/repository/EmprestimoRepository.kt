package com.biblioteca.repository

import com.biblioteca.model.Emprestimo
import org.springframework.data.jpa.repository.JpaRepository

interface EmprestimoRepository : JpaRepository<Emprestimo, Long>
