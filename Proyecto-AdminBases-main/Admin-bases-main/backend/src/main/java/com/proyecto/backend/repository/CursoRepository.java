package com.proyecto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {
}
