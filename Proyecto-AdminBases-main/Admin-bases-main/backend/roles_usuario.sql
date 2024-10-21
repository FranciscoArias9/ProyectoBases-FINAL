-- Crear usuario
CREATE USER c##francisco IDENTIFIED BY root;

-- Otorgar privilegios para iniciar sesión
GRANT CREATE SESSION TO c##francisco;

-- Otorgar privilegios para crear tablas
GRANT CREATE TABLE TO c##francisco;

-- Otorgar privilegios para modificar tablas (incluye ALTER)
GRANT ALTER ANY TABLE TO c##francisco;

-- Otorgar privilegios para eliminar tablas (incluye DROP)
GRANT DROP ANY TABLE TO c##francisco;

-- Otorgar privilegios para consultar tablas
GRANT SELECT ANY TABLE TO c##francisco;

-- Otorgar privilegios para insertar datos
GRANT INSERT ANY TABLE TO c##francisco;

-- Otorgar privilegios para actualizar datos
GRANT UPDATE ANY TABLE TO c##francisco;

-- Otorgar privilegios para eliminar datos
GRANT DELETE ANY TABLE TO c##francisco;


-- Crear roles
CREATE ROLE c##rol_instructor;
CREATE ROLE c##rol_cliente;
CREATE ROLE c##rol_soporte;

-- Crear usuarios
CREATE USER c##instructor1 IDENTIFIED BY contrasena_instructor1;
CREATE USER c##instructor2 IDENTIFIED BY contrasena_instructor2;

CREATE USER c##cliente1 IDENTIFIED BY contrasena_cliente1;
CREATE USER c##cliente2 IDENTIFIED BY contrasena_cliente2;

CREATE USER c##soporte1 IDENTIFIED BY contrasena_soporte1;

-- Asignar roles a usuarios
GRANT c##rol_instructor TO c##instructor1;
GRANT c##rol_instructor TO c##instructor2;

GRANT c##rol_cliente TO c##cliente1;
GRANT c##rol_cliente TO c##cliente2;

GRANT c##rol_soporte TO c##soporte1;

-- Asignar privilegios a roles
GRANT SELECT, INSERT, UPDATE ON cursos TO c##rol_instructor;
GRANT SELECT, INSERT, UPDATE ON rutinas TO c##rol_instructor;
GRANT SELECT, INSERT, UPDATE ON maquinas TO c##rol_instructor;
GRANT SELECT ON clientes TO c##rol_instructor; -- Cambié "cliente" a "clientes"
GRANT INSERT ON historial_curso TO c##rol_instructor;
GRANT SELECT ON bitacora TO c##rol_instructor;

GRANT SELECT ON cursos TO c##rol_cliente;
GRANT INSERT, UPDATE ON clientes TO c##rol_cliente; -- Cambié "cliente" a "clientes"
GRANT INSERT ON historial_curso TO c##rol_cliente;

GRANT SELECT, INSERT, UPDATE, DELETE ON cursos TO c##rol_soporte;
GRANT SELECT, INSERT, UPDATE, DELETE ON rutinas TO c##rol_soporte;
GRANT SELECT, INSERT, UPDATE, DELETE ON maquinas TO c##rol_soporte;
GRANT SELECT, INSERT, UPDATE, DELETE ON clientes TO c##rol_soporte; -- Cambié "cliente" a "clientes"
GRANT SELECT, INSERT, UPDATE, DELETE ON instructores TO c##rol_soporte;
GRANT SELECT, INSERT, UPDATE, DELETE ON historial_curso TO c##rol_soporte;
GRANT SELECT, INSERT, UPDATE, DELETE ON bitacora TO c##rol_soporte;
