package com.proyecto.backend.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service
public class DynamicOracleConnection {


    public Connection getConnection(String username, String password) throws SQLException {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";  // Ajusta la URL según tu configuración
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
    
        // Permitir tanto a instructores como a clientes conectarse
        if (!hasValidRole(connection)) {
            connection.close();  // Cerrar la conexión si no tiene el rol válido
            throw new SQLException("Usuario no autorizado. Solo los usuarios con los roles 'C##ROL_DE_INSTRUCTOR' o 'C##ROL_DE_CLIENTE' pueden acceder.");
        }
    
        System.out.println("GET CONNECTION LOGRADO");
        return connection;
    }
    
    // GET CONNECTION viejo //
    /* 
    public Connection getConnection(String username, String password) throws SQLException {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";  // Ajusta la URL según tu configuración
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

        // Verificar si el usuario tiene el rol c##rol_de_instructor o c##rol_de_cliente
        if (!hasValidRole(connection)) {
            connection.close();  // Cerrar la conexión si no tiene el rol válido
            throw new SQLException("Usuario no autorizado. Solo los usuarios con los roles 'C##ROL_DE_INSTRUCTOR' o 'C##ROL_DE_CLIENTE' pueden acceder.");
        }

        System.out.println("GET CONNECTION LOGRADO");
        // Si tiene el rol correcto, devolver la conexión
        return connection;
    } */

    // Método para verificar si el usuario tiene el rol 'C##ROL_DE_INSTRUCTOR' o 'C##ROL_DE_CLIENTE'
    /* 
    public boolean hasValidRole(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_role_privs WHERE granted_role IN ('C##ROL_DE_INSTRUCTOR', 'C##ROL_DE_CLIENTE')";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("HAS VALID LOGRADO");
            return rs.next(); // Si encuentra un resultado, el usuario tiene el rol requerido
        }
    } */

    // Método para verificar si el usuario tiene el rol 'C##ROL_DE_INSTRUCTOR'
    public boolean hasInstructorRole(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_role_privs WHERE granted_role = 'C##ROL_DE_INSTRUCTOR'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("HAS INSTRUCTOR ROLE LOGRADO");
            return rs.next(); // Si encuentra un resultado, el usuario tiene el rol requerido
        }
    }

    // Método para verificar si el usuario tiene el rol 'C##ROL_DE_CLIENTE'
    public boolean hasClienteRole(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_role_privs WHERE granted_role = 'C##ROL_DE_CLIENTE'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next(); // Si encuentra un resultado, el usuario tiene el rol de cliente
        }
    }

    public boolean hasSoportistaRole(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_role_privs WHERE granted_role = 'C##ROL_DE_SOPORTISTA'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next(); // Si encuentra un resultado, el usuario tiene el rol de soportista
        }
    }
    


    // Método para verificar si el usuario tiene el rol 'C##ROL_DE_INSTRUCTOR' o 'C##ROL_DE_CLIENTE' o 'C##ROL_DE_SOPORTISTA'
    public boolean hasValidRole(Connection connection) throws SQLException {
        String query = "SELECT * FROM user_role_privs WHERE granted_role IN ('C##ROL_DE_INSTRUCTOR', 'C##ROL_DE_CLIENTE', 'C##ROL_DE_SOPORTISTA')";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("HAS VALID LOGRADO");
            return rs.next(); // Si encuentra un resultado, el usuario tiene el rol requerido
        }
    }
}
