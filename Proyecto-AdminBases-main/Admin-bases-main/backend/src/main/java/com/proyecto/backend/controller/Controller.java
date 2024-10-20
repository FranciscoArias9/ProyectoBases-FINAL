package com.proyecto.backend.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/rutinas")
    public Rutina agregarRutina(@RequestBody Rutina rutina) {
        return rutinaRepository.save(rutina);
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

    @DeleteMapping("/rutinas/{id}")
    public ResponseEntity<String> eliminarRutina(@PathVariable int id) {
        if (rutinaRepository.existsById(id)) {
            rutinaRepository.deleteById(id);
            return ResponseEntity.ok("Rutina eliminada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada.");
        }
    }

    // Máquinas - Obtener todas las máquinas
    @GetMapping("/maquinas")
    public List<Maquina> obtenerMaquinas() {
        return maquinaRepository.findAll(); // Devuelve la lista de máquinas
    }

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
}

    @PutMapping("/maquinas/{id}")
    public ResponseEntity<Maquina> actualizarMaquina(@PathVariable int id, @RequestBody Maquina maquinaActualizada) {
        return maquinaRepository.findById(id)
                .map(maquina -> {
                    maquina.setDescripcion(maquinaActualizada.getDescripcion());
                    maquina.setEstado(maquinaActualizada.getEstado());
                    return ResponseEntity.ok(maquinaRepository.save(maquina));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/maquinas/{id}")
    public ResponseEntity<String> eliminarMaquina(@PathVariable int id) {
        if (maquinaRepository.existsById(id)) {
            maquinaRepository.deleteById(id);
            return ResponseEntity.ok("Máquina eliminada correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Máquina no encontrada.");
        }
    }

    // Clientes - Obtener todos los clientes
    @GetMapping("/clientes")
    public List<Cliente> obtenerClientes() {
        return clienteRepository.findAll(); // Devuelve la lista de clientes
    }

    @GetMapping("/clientes/{cedula}")
    public Optional<Cliente> obtenerCliente(@PathVariable long cedula) {
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
}



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

    @PutMapping("/clientes/{cedula}")
    public ResponseEntity<String> actualizarCliente(@PathVariable long cedula, @RequestBody Cliente clienteActualizado, HttpSession session) throws SQLException {
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
    }

    @DeleteMapping("/clientes/{cedula}")
    public ResponseEntity<String> eliminarCliente(@PathVariable long cedula, HttpSession session) throws SQLException {
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
    }

    // Cursos - Obtener todos los cursos
    @GetMapping("/cursos")
    public List<Curso> obtenerCursos() {
        return cursoRepository.findAll(); // Devuelve la lista de cursos desde la base de datos
    }

    // Cursos - Crear un nuevo curso
    @PostMapping("/cursos")
    public Curso agregarCurso(@RequestBody Curso curso) {
        return cursoRepository.save(curso); // Guarda un curso nuevo
    }

    // Cursos - Actualizar un curso por ID
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
    }

    // Cursos - Eliminar un curso por ID
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable int id) {
        if (cursoRepository.existsById(id)) {
            cursoRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
