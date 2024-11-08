package com.proyecto.backend.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.backend.dto.LoginRequest;
import com.proyecto.backend.model.Cliente;
import com.proyecto.backend.model.Curso;
import com.proyecto.backend.model.Empleado;
import com.proyecto.backend.model.Maquina;
import com.proyecto.backend.model.Rutina;
import com.proyecto.backend.repository.ClienteRepository;
import com.proyecto.backend.repository.CursoRepository;
import com.proyecto.backend.repository.EmpleadoRepository;
import com.proyecto.backend.repository.MaquinaRepository;
import com.proyecto.backend.repository.RutinaRepository;
import com.proyecto.backend.service.DynamicOracleConnection;

import jakarta.servlet.http.HttpSession;



@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RutinaRepository rutinaRepository;

    @Autowired
    private MaquinaRepository maquinaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository; 

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DynamicOracleConnection dynamicOracleConnection;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            Connection connection = dynamicOracleConnection.getConnection(loginRequest.getUsername(), loginRequest.getPassword());
            System.out.println("Session ID - Login: " + session.getId());

            System.out.println("Conexión es: " + connection);
            if (connection != null) {
                session.setAttribute("dbConnection", connection);
                System.out.println("Conexión guardada en sesión: " + connection);
            } else {
                System.out.println("Conexión es null durante el login.");
            }
            
            System.out.println("LOGIN EXITOSO CONTROLLER");
            // Mensaje de éxito
            return ResponseEntity.ok("Login exitoso");
            

        } catch (SQLException e) {
            // Si la conexión falla
            System.out.println("LOGIN FALLO CONTROLLER");
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

    // Empleados - Obtener todos los empleados
    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados() {
        return empleadoRepository.findAll(); // Devuelve la lista de empleados
    }

    // Empleados - Obtener un empleado por cédula
    @GetMapping("/empleados/{cedula}")
    public Optional<Empleado> obtenerEmpleado(@PathVariable int cedula) {
        return empleadoRepository.findById(cedula); // Devuelve un empleado por cédula
    }

    // Empleados - Agregar un nuevo empleado
    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado) {
        return empleadoRepository.save(empleado); // Guarda un nuevo empleado
    }

    // Empleados - Actualizar un empleado por cédula
    @PutMapping("/empleados/{cedula}")
    public Empleado actualizarEmpleado(@PathVariable int cedula, @RequestBody Empleado empleadoActualizado) {
        return empleadoRepository.findById(cedula)
                .map(empleado -> {
                    empleado.setNombre(empleadoActualizado.getNombre());
                    empleado.setApellido1(empleadoActualizado.getApellido1());
                    empleado.setApellido2(empleadoActualizado.getApellido2());
                    empleado.setDireccion(empleadoActualizado.getDireccion());
                    empleado.setE_mail(empleadoActualizado.getE_mail());
                    empleado.setTel_cel(empleadoActualizado.getTel_cel());
                    empleado.setTel_habitacion(empleadoActualizado.getTel_habitacion());
                    empleado.setPassword(empleadoActualizado.getPassword());
                    return empleadoRepository.save(empleado);
                }).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    // Obtener todas las rutinas
    @GetMapping("/rutinas")
    public List<Rutina> obtenerRutinas() {
        return rutinaRepository.findAll();
    }

    // Obtener una rutina por ID
    @GetMapping("/rutinas/{id}")
    public ResponseEntity<Rutina> obtenerRutina(@PathVariable int id) {
        Optional<Rutina> rutina = rutinaRepository.findById(id);
        return rutina.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva rutina
    /* 
    @PostMapping("/rutinas")
    public Rutina agregarRutina(@RequestBody Rutina rutina) {
        return rutinaRepository.save(rutina);
    } */

    @PostMapping("/rutinas")
public ResponseEntity<String> agregarRutina(@RequestBody Rutina rutina, HttpSession session) {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    try {
        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
            try {
                // Llamar al procedimiento almacenado para insertar la rutina
                String sql = "{call insertar_rutina(?, ?, ?, ?, ?, ?)}";
                jdbcTemplate.update(sql,
                        rutina.getNombre(),
                        rutina.getFecha(),
                        rutina.getHoras(),
                        rutina.getCliente().getCedula(), // ID del cliente
                        rutina.getInstructor(), // ID del instructor
                        rutina.getMaquina().getId_maquina() // ID de la máquina
                );

                return ResponseEntity.ok("Rutina agregada correctamente mediante procedimiento almacenado.");
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar la rutina.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar rutinas.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
    }
}


    // Actualizar una rutina existente
    @PutMapping("/rutinas/{id}")
    public ResponseEntity<Rutina> actualizarRutina(@PathVariable int id, @RequestBody Rutina rutinaActualizada) {
        return rutinaRepository.findById(id)
                .map(rutina -> {
                    rutina.setCliente(rutinaActualizada.getCliente());
                    rutina.setInstructor(rutinaActualizada.getInstructor());
                    rutina.setMaquina(rutinaActualizada.getMaquina());
                    rutina.setFecha(rutinaActualizada.getFecha());
                    rutina.setHoras(rutinaActualizada.getHoras());
                    rutina.setNombre(rutinaActualizada.getNombre());
                    return ResponseEntity.ok(rutinaRepository.save(rutina));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* 
    @DeleteMapping("/rutinas/{id}")
    public ResponseEntity<String> eliminarRutina(@PathVariable int id) {
        if (rutinaRepository.existsById(id)) {
            rutinaRepository.deleteById(id);
            return ResponseEntity.ok("Rutina eliminada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada.");
        }
    } */

    @DeleteMapping("/rutinas/{id}")
public ResponseEntity<String> eliminarRutina(@PathVariable int id, HttpSession session) {
    System.out.println("EN ELIMINAR RUTINA");
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        System.out.println("CONEXIÓN NULA - ELIMINAR RUTINA");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    try {
        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection)) {
            System.out.println("EN ELIMINAR RUTINA - ROL INSTRUCTOR: True");
            try {
                // Llamar al procedimiento almacenado para eliminar la rutina
                String sql = "{call eliminar_rutina(?)}";
                jdbcTemplate.update(sql, id);

                System.out.println("ELIMINADO BIEN");
                return ResponseEntity.ok("Rutina eliminada correctamente mediante procedimiento almacenado.");
            } catch (DataAccessException e) {
                e.printStackTrace();
                System.out.println("ERROR AL ELIMINAR LA RUTINA");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la rutina.");
            }
        } else {
            System.out.println("NO TIENES PERMISO");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para eliminar rutinas.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("ERROR AL VERIFICAR EL ROL");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
    }
}


    // Máquinas - Obtener todas las máquinas
    @GetMapping("/maquinas")
    public List<Maquina> obtenerMaquinas() {
        return maquinaRepository.findAll(); // Devuelve la lista de máquinas
    }

    /* 
    @PostMapping("/maquinas")
@Transactional
public ResponseEntity<String> agregarMaquina(@RequestBody Maquina maquina, HttpSession session) throws SQLException {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    // Verificar si el usuario tiene el rol de instructor
    if (dynamicOracleConnection.hasInstructorRole(connection)) {
        // Guardar la máquina
        maquinaRepository.save(maquina);
        return ResponseEntity.ok("Máquina agregada correctamente.");
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar máquinas.");
    }
}*/

    
@PostMapping("/maquinas")
@Transactional
public ResponseEntity<String> agregarMaquina(@RequestBody Maquina maquina, HttpSession session) throws SQLException {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    // Verificar si el usuario tiene el rol de instructor
    if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
        try {
            // Llamar al procedimiento almacenado para insertar la máquina
            String sql = "{call insertar_maquina(?, ?)}";
            jdbcTemplate.update(sql,
                    maquina.getDescripcion(),
                    maquina.getEstado());

            return ResponseEntity.ok("Máquina agregada correctamente mediante procedimiento almacenado.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar la máquina.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar máquinas.");
    }
}

    /* 
    @PutMapping("/maquinas/{id}")
    public ResponseEntity<Maquina> actualizarMaquina(@PathVariable int id, @RequestBody Maquina maquinaActualizada) {
        return maquinaRepository.findById(id)
                .map(maquina -> {
                    maquina.setDescripcion(maquinaActualizada.getDescripcion());
                    maquina.setEstado(maquinaActualizada.getEstado());
                    return ResponseEntity.ok(maquinaRepository.save(maquina));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    } */
    @PutMapping("/maquinas/{id}")
    public ResponseEntity<String> actualizarMaquina(@PathVariable int id, @RequestBody Maquina maquinaActualizada, HttpSession session) {
        Connection connection = (Connection) session.getAttribute("dbConnection");
    
        if (connection == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }
    
        try {
            // Verificar si el usuario tiene el rol de instructor
            if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
                // Llamar al procedimiento almacenado para actualizar la máquina
                String sql = "{call actualizar_maquina(?, ?, ?)}";
                jdbcTemplate.update(sql,
                        id,
                        maquinaActualizada.getDescripcion(),
                        maquinaActualizada.getEstado());
    
                return ResponseEntity.ok("Máquina actualizada correctamente mediante procedimiento almacenado.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para actualizar máquinas.");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la máquina.");
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
        }
    }
    
    /* 
    @DeleteMapping("/maquinas/{id}")
    public ResponseEntity<String> eliminarMaquina(@PathVariable int id) {
        if (maquinaRepository.existsById(id)) {
            maquinaRepository.deleteById(id);
            return ResponseEntity.ok("Máquina eliminada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Máquina no encontrada.");
        }
    } */

    @DeleteMapping("/maquinas/{id}")
    public ResponseEntity<String> eliminarMaquina(@PathVariable int id, HttpSession session) {
        Connection connection = (Connection) session.getAttribute("dbConnection");
    
        if (connection == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }
    
        try {
            // Verificar si el usuario tiene el rol de instructor
             if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
                try {
                    // Llamar al procedimiento almacenado para eliminar la máquina
                    String sql = "{call eliminar_maquina(?)}";
                    jdbcTemplate.update(sql, id);
    
                    return ResponseEntity.ok("Máquina eliminada correctamente mediante procedimiento almacenado.");
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la máquina.");
                }
            } else {
                System.out.println("No hay permiso");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para eliminar máquinas.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
        }
    }
    


    // Clientes - Obtener todos los clientes
     @GetMapping("/clientes")
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll(); // Devuelve la lista de clientes
    }

   

    @GetMapping("/clientes/{cedula}")
    public Optional<Cliente> obtenerCliente(@PathVariable int cedula) {
        return clienteRepository.findById(cedula); // Devuelve un cliente por cédula
    }



    @PostMapping("/clientes")
@Transactional
public ResponseEntity<String> agregarCliente(@RequestBody Cliente cliente, HttpSession session) throws SQLException {
    System.out.println("Session - agregar cliente: " + session.getId());

    Connection connection = (Connection) session.getAttribute("dbConnection");
    System.out.println("Conexión obtenida de la sesión: " + connection);

    if (connection == null) {
        System.out.println("CONEXION FALLIDA: AGREGAR CLIENTES");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
        // Invocar el procedimiento almacenado
        String sql = "{call insertar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        jdbcTemplate.update(sql, cliente.getCedula(), cliente.getNombre(), cliente.getApellido1(), cliente.getApellido2(),
                cliente.getDireccion(), cliente.getE_mail(), cliente.getFecha_inscripcion(), cliente.getCelular(),
                cliente.getTel_habitacion(), cliente.getPassword());

        System.out.println("CONEXION EXITO: AGREGAR CLIENTES");

        return ResponseEntity.ok("Cliente agregado y usuario creado.");
    } else {
        System.out.println("SIN PERMISO: AGREGAR CLIENTES");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar clientes.");
    }
}



/* 

    @PostMapping("/clientes")
@Transactional
public ResponseEntity<String> agregarCliente(@RequestBody Cliente cliente, HttpSession session) throws SQLException {
    System.out.println("Session - agregar cliente: " + session.getId());

    Connection connection = (Connection) session.getAttribute("dbConnection");
    System.out.println("Conexión obtenida de la sesión: " + connection);

    if (connection == null) {
        System.out.println("CONEXION FALLIDA: AGREGAR CLIENTES");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    // Solo permitir agregar clientes si el usuario tiene el rol de instructor
    if (dynamicOracleConnection.hasInstructorRole(connection)) {
        // Guardar el cliente
        Cliente nuevoCliente = clienteRepository.save(cliente);

        // Crear un usuario en dba_users con la cédula del cliente y contraseña root
        String username = "c##" + cliente.getCedula();
        String password = "root";

        // Ejecutar la consulta para crear el usuario en Oracle
        String sql = "CREATE USER " + username + " IDENTIFIED BY " + password;
        jdbcTemplate.execute(sql);

        // Asignar roles o privilegios si es necesario
        jdbcTemplate.execute("GRANT CONNECT TO " + username);
        jdbcTemplate.execute("GRANT RESOURCE TO " + username);


        // Asignar el rol de cliente al cliente recien creado
        jdbcTemplate.execute("GRANT c##rol_de_cliente TO " + username);

        System.out.println("CONEXION EXITO: AGREGAR CLIENTES");

        return ResponseEntity.ok("Cliente agregado y usuario creado.");
    } else {
        System.out.println("SIN PERMISO: AGREGAR CLIENTES");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar clientes.");
    }
}*/



    // AGREGAR CLIENTE viejo
    /* 
    @PostMapping("/clientes")
    @Transactional
    public ResponseEntity<String> agregarCliente(@RequestBody Cliente cliente, HttpSession session) throws SQLException {
        System.out.println("Session  -  agregar cliente: " + session.getId());

        Connection connection = (Connection) session.getAttribute("dbConnection");
        System.out.println("Conexión obtenida de la sesión: " + connection);


        if (connection == null) {
            System.out.println("CONEXION FALLIDA: AGREGAR CLIENTES");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }

        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection)) {
            // Guardar el cliente
            Cliente nuevoCliente = clienteRepository.save(cliente);

            // Crear un usuario en dba_users con la cédula del cliente y contraseña root
            String username = "c##" + cliente.getCedula();
            String password = "root";

            // Ejecutar la consulta para crear el usuario en Oracle
            String sql = "CREATE USER " + username + " IDENTIFIED BY " + password;
            jdbcTemplate.execute(sql);

            // Asignar roles o privilegios si es necesario
            jdbcTemplate.execute("GRANT CONNECT TO " + username);
            jdbcTemplate.execute("GRANT RESOURCE TO " + username);

            System.out.println("CONEXION EXITO: AGREGAR CLIENTES");

            return ResponseEntity.ok("Cliente agregado y usuario creado.");
        } else {
            System.out.println("SIN PERMISO: AGREGAR CLIENTES");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar clientes.");
        }
    } */


    /* 
    @PutMapping("/clientes/{cedula}")
    public ResponseEntity<String> actualizarCliente(@PathVariable int cedula, @RequestBody Cliente clienteActualizado, HttpSession session) throws SQLException {
        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }

        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection)) {
            Cliente cliente = clienteRepository.findById(cedula)
                    .map(existingClient -> {
                        existingClient.setNombre(clienteActualizado.getNombre());
                        existingClient.setApellido1(clienteActualizado.getApellido1());
                        existingClient.setDireccion(clienteActualizado.getDireccion());
                        return clienteRepository.save(existingClient);
                    }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            return ResponseEntity.ok("Cliente actualizado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para actualizar clientes.");
        }
    } */


    
    @PutMapping("/clientes/{cedula}")
public ResponseEntity<String> actualizarCliente(@PathVariable int cedula, @RequestBody Cliente clienteActualizado, HttpSession session) {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    try {
        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
            try {
                // Llamar al procedimiento almacenado para actualizar el cliente
                String sql = "{call actualizar_cliente(?, ?, ?, ?, ?, ?)}";
                jdbcTemplate.update(sql,
                        cedula,
                        clienteActualizado.getNombre(),
                        clienteActualizado.getApellido1(),
                        clienteActualizado.getApellido2(),
                        clienteActualizado.getDireccion(),
                        clienteActualizado.getE_mail());

                return ResponseEntity.ok("Cliente actualizado correctamente mediante procedimiento almacenado.");
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el cliente.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para actualizar clientes.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
    }
}

     /* 
    @DeleteMapping("/clientes/{cedula}")
    public ResponseEntity<String> eliminarCliente(@PathVariable int cedula, HttpSession session) throws SQLException {
        Connection connection = (Connection) session.getAttribute("dbConnection");

        if (connection == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }

        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection)) {
            clienteRepository.deleteById(cedula);
            return ResponseEntity.ok("Cliente eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para eliminar clientes.");
        }
    }*/

    @DeleteMapping("/clientes/{cedula}")
public ResponseEntity<String> eliminarCliente(@PathVariable int cedula, HttpSession session) {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    try {
        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
            try {
                System.out.println("En el eliminar cliente - confrimado");
                // Llamar al procedimiento almacenado para eliminar el cliente
                String sql = "{call eliminarcl(?)}";
                jdbcTemplate.update(sql, cedula);

                return ResponseEntity.ok("Cliente eliminado correctamente mediante procedimiento almacenado.");
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el cliente.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para eliminar clientes.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
    }
}



    // Cursos - Obtener todos los cursos
    @GetMapping("/cursos")
    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll(); // Devuelve la lista de cursos desde la base de datos
    }

    // Cursos - Crear un nuevo curso
    /* 
    @PostMapping("/cursos")
    public Curso agregarCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso); // Guarda un curso nuevo
    } */
    @PostMapping("/cursos")
    public ResponseEntity<String> agregarCurso(@RequestBody Curso curso, HttpSession session) {
        Connection connection = (Connection) session.getAttribute("dbConnection");
    
        if (connection == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
        }
    
        try {
            // Verificar si el usuario tiene el rol de instructor
            if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
                try {
                    // Llamar al procedimiento almacenado para agregar el curso
                    String sql = "{call insertar_curso(?, ?, ?)}";
                    jdbcTemplate.update(sql,
                            curso.getNombre(),
                            curso.getDescripcion(),
                            curso.getHorario());
    
                    return ResponseEntity.ok("Curso agregado correctamente mediante procedimiento almacenado.");
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar el curso.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para agregar cursos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
        }
    }
    

    // Cursos - Actualizar un curso por ID
    /* 
    @PutMapping("/cursos/{id}")
    public ResponseEntity<Curso> actualizarCurso(@PathVariable int id, @RequestBody Curso cursoDetalles) {
        Optional<Curso> curso = cursoRepository.findById(id);

        if (curso.isPresent()) {
            Curso cursoActualizado = curso.get();
            cursoActualizado.setDescripcion(cursoDetalles.getDescripcion());
            cursoActualizado.setNombre(cursoDetalles.getNombre());
            cursoActualizado.setHorario(cursoDetalles.getHorario());
            return ResponseEntity.ok(cursoRepository.save(cursoActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    } */

    @PutMapping("/cursos/{id}")
public ResponseEntity<String> actualizarCurso(@PathVariable int id, @RequestBody Curso cursoDetalles, HttpSession session) {
    Connection connection = (Connection) session.getAttribute("dbConnection");

    if (connection == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
    }

    try {
        // Verificar si el usuario tiene el rol de instructor
        if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
            try {
                // Llamar al procedimiento almacenado para actualizar el curso
                String sql = "{call actualizar_curso(?, ?, ?, ?)}";
                jdbcTemplate.update(sql,
                        id,
                        cursoDetalles.getNombre(),
                        cursoDetalles.getDescripcion(),
                        cursoDetalles.getHorario());

                return ResponseEntity.ok("Curso actualizado correctamente mediante procedimiento almacenado.");
            } catch (DataAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el curso.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para actualizar cursos.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
    }
}


    // Cursos - Eliminar un curso por ID
    /* 
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable int id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        */

        @DeleteMapping("/cursos/{id}")
        public ResponseEntity<String> eliminarCurso(@PathVariable int id, HttpSession session) {
            Connection connection = (Connection) session.getAttribute("dbConnection");
        
            if (connection == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No hay una conexión activa.");
            }
        
            try {
                // Verificar si el usuario tiene el rol de instructor
                if (dynamicOracleConnection.hasInstructorRole(connection) || dynamicOracleConnection.hasSoportistaRole(connection)) {
                    try {
                        // Llamar al procedimiento almacenado para eliminar el curso
                        String sql = "{call eliminar_curso(?)}";
                        jdbcTemplate.update(sql, id);
        
                        return ResponseEntity.ok("Curso eliminado correctamente mediante procedimiento almacenado.");
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el curso.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No tienes permiso para eliminar cursos.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al verificar el rol de usuario.");
            }
        }
            


    @PutMapping("/clientes/{cedula}/estado")
    public ResponseEntity<String> cambiarEstadoCliente(@PathVariable int cedula, @RequestBody String nuevoEstado) {
        Optional<Cliente> cliente = clienteRepository.findById(cedula);
        if (cliente.isPresent()) {
            Cliente clienteExistente = cliente.get();
            clienteExistente.setEstado(nuevoEstado);  // Actualizar el estado
            clienteRepository.save(clienteExistente);
            return ResponseEntity.ok("Estado actualizado a " + nuevoEstado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
    }
}
