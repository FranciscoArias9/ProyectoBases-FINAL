/* 
package com.proyecto.backend.controller;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend.dto.LoginRequest;
import com.proyecto.backend.service.DynamicOracleConnection;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class LoginController {

    private DynamicOracleConnection dynamicOracleConnection = new DynamicOracleConnection();

    @PostMapping("/login")
public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
    try {
        // Obtener la conexión a la base de datos usando DynamicOracleConnection
        Connection connection = dynamicOracleConnection.getConnection(loginRequest.getUsername(), loginRequest.getPassword());

        // Si la conexión es exitosa, guardarla en la sesión
        session.setAttribute("dbConnection", connection);

        // Mensaje de éxito
        return ResponseEntity.ok("Login exitoso");

    } catch (SQLException e) {
        // Si la conexión falla
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas o error en la conexión");
    }
}

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        Connection connection = (Connection) session.getAttribute("dbConnection");
        if (connection != null) {
            try {
                connection.close();  // Cerrar la conexión a la base de datos
            } catch (SQLException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar la conexión");
            }
        }
        session.invalidate();  // Invalidar la sesión
        return ResponseEntity.ok("Logout exitoso");
    }
}
*/