package com.proyecto.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
