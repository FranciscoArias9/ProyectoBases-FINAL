<template>
  <nav class="navbar navbar-expand-lg bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand text-white">Gimnasio</a>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/" class="nav-link">Inicio</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/contactos" class="nav-link">Contactos</router-link>
          </li>

          <!-- Menú desplegable de Mantenimiento -->
          <li class="nav-item dropdown" v-if="isAuthenticated">
            <a class="nav-link dropdown-toggle" href="#" id="mantenimientoDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Mantenimiento
            </a>
            <ul class="dropdown-menu" aria-labelledby="mantenimientoDropdown">
              <li><router-link to="/clientes/mantenimiento" class="dropdown-item">Mantenimiento de Clientes</router-link></li>
              <li><router-link to="/maquinas/mantenimiento" class="dropdown-item">Mantenimiento de Máquinas</router-link></li>
              <li><router-link to="/cursos/mantenimiento" class="dropdown-item">Mantenimiento de Cursos</router-link></li>
              <li><router-link to="/rutinas/mantenimiento" class="dropdown-item">Mantenimiento de Rutinas</router-link></li>
            </ul>
          </li>

          <!-- Enlace directo a Inscripción de Clientes -->
          <li class="nav-item" v-if="isAuthenticated">
            <router-link to="/servicios/inscripcion" class="nav-link">Inscripción de Clientes</router-link>
          </li>

          <!-- Cerrar sesión -->
          <li class="nav-item" v-if="isAuthenticated">
            <a href="#" @click.prevent="logout" class="nav-link">Cerrar sesión</a>
          </li>

          <li class="nav-item" v-else>
            <router-link to="/ingreso" class="nav-link">Ingreso</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  data() {
    return {
      isAuthenticated: false, // Cambia según el estado de autenticación
    };
  },
  mounted() {
    // Revisar si el usuario está autenticado (por ejemplo, buscando un token en localStorage)
    this.isAuthenticated = !!localStorage.getItem('authToken');
  },
  methods: {
    logout() {
      localStorage.removeItem('authToken');
      this.isAuthenticated = false;
      this.$router.push('/ingreso');
    }
  }
};
</script>

<style scoped>
/* Estilo general de la navbar */
.navbar {
  padding: 15px 30px;
  background-color: #343a40; /* Color oscuro personalizado */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

/* Logo de la navbar */
.navbar-brand {
  font-size: 1.5em;
  font-weight: bold;
  color: #f57c00;
  transition: color 0.3s;
}

.navbar-brand:hover {
  color: #ff8c00;
}

/* Links de la navbar */
.nav-link {
  color: #f0f0f0;
  font-size: 1.1em;
  transition: color 0.3s;
  margin-left: 10px;
}

.nav-link.active, .nav-link:hover {
  color: #f57c00;
}

/* Dropdown menu estilo */
.dropdown-menu {
  background-color: #3b4047;
  border-radius: 8px;
  border: none;
}

.dropdown-item {
  color: #ffffff;
  padding: 10px 20px;
  transition: background-color 0.3s, color 0.3s;
}

.dropdown-item:hover {
  background-color: #f57c00;
  color: #ffffff;
}

/* Botón de toggler */
.navbar-toggler {
  border-color: #f57c00;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3E%3Cpath stroke='rgba%2878, 78, 78, 1%29' stroke-width='2' stroke-linecap='round' stroke-miterlimit='10' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
}
</style>
