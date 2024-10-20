package com.proyecto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend.model.Maquina;


public interface MaquinaRepository extends JpaRepository<Maquina, Integer> {
}
