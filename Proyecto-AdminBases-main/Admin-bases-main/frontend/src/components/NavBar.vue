<template>
  <nav class="navbar navbar-expand-lg bg-dark">
    <div class="container-fluid">
      <!-- Logo en lugar de "Gimnasio BD" -->
      <router-link to="/" class="navbar-brand">
        <img src="@/assets/logo2.png" alt="Gimnasio BD" class="navbar-logo">
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/" class="nav-link" :class="{ active: isActive('/') }">Inicio</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/contactos" class="nav-link" :class="{ active: isActive('/contactos') }">Contactos</router-link>
          </li>

          <!-- Menú desplegable de Mantenimiento -->
          <li class="nav-item dropdown" v-if="isAuthenticated">
            <a class="nav-link dropdown-toggle" href="#" id="mantenimientoDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Mantenimiento
            </a>
            <ul class="dropdown-menu" aria-labelledby="mantenimientoDropdown">
              <li><router-link to="/clientes/mantenimiento" class="dropdown-item" :class="{ active: isActive('/clientes/mantenimiento') }">Mantenimiento de Clientes</router-link></li>
              <li><router-link to="/maquinas/mantenimiento" class="dropdown-item" :class="{ active: isActive('/maquinas/mantenimiento') }">Mantenimiento de Máquinas</router-link></li>
              <li><router-link to="/cursos/mantenimiento" class="dropdown-item" :class="{ active: isActive('/cursos/mantenimiento') }">Mantenimiento de Cursos</router-link></li>
              <li><router-link to="/rutinas/mantenimiento" class="dropdown-item" :class="{ active: isActive('/rutinas/mantenimiento') }">Mantenimiento de Rutinas</router-link></li>
            </ul>
          </li>

          <!-- Enlace directo a Inscripción de Clientes -->
          <li class="nav-item" v-if="isAuthenticated">
            <router-link to="/servicios/inscripcion" class="nav-link" :class="{ active: isActive('/servicios/inscripcion') }">Inscripción de Clientes</router-link>
          </li>

          <!-- Cerrar sesión -->
          <li class="nav-item" v-if="isAuthenticated">
            <a href="#" @click.prevent="logout" class="nav-link">Cerrar sesión</a>
          </li>

          <li class="nav-item" v-else>
            <router-link to="/ingreso" class="nav-link" :class="{ active: isActive('/ingreso') }">Ingreso</router-link>
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
    },
    isActive(route) {
      return this.$route.path === route;
    }
  }
};
</script>

<style scoped>
.navbar-logo {
  height: 60px; 
}

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
  color: white;
  font-size: 1.1em;
  transition: color 0.3s;
  margin-left: 10px;
}

.nav-link.active {
  color: orange; 
}

.navbar {
  background-color: #343a40;
  padding: 15px 30px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

.navbar-toggler {
  border-color: rgba(255, 255, 255, 0.5); 
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml;charset=UTF8,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3E%3Cpath stroke='rgba%28255, 255, 255, 0.5%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3E%3C/svg%3E");
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

.nav-link:hover {
  color: orange; 
}
</style>
