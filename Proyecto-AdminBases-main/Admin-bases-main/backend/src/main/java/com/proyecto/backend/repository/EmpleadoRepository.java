package com.proyecto.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.backend.model.Empleado;
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Opcional: Puedes agregar métodos personalizados si lo necesitas
}
