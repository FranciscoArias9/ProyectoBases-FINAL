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

          <!-- Mostrar solo si el usuario está autenticado -->
          <li class="nav-item" v-if="isAuthenticated">
            <router-link to="/clientes/mantenimiento" class="nav-link">Mantenimiento de Clientes</router-link>
          </li>

          <li class="nav-item" v-if="isAuthenticated">
              <router-link to="/maquinas/mantenimiento" class="nav-link">Mantenimiento de Máquinas</router-link>
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
/* Estilos del navbar */
</style>
