<template>
  <section class="vh-100 d-flex align-items-center justify-content-center bg-image">
    <div class="card  border-0" style="max-width: 400px;">
      <div class="card-body text-center p-4">
        <!-- Logo 'G' -->
        <div class="logo-circle mb-3">G</div>
        
        <h2 class="fw-bold text-uppercase text-white">Ingreso</h2>
        <p class="text-white-50 mb-4">Por favor, iniciar sesión</p>

        <form @submit.prevent="login">
          <div class="form-outline mb-3">
            <input type="text" v-model="username" class="form-control form-control-lg" placeholder="Usuario" />
          </div>
          <div class="form-outline mb-3">
            <input type="password" v-model="password" class="form-control form-control-lg" placeholder="Contraseña" />
          </div>

          <button class="btn btn-orange btn-lg w-100 mb-3" type="submit">Ingresar</button>

          <p class="text-white-50 small mb-4">
            <a href="#!" class="text-white-50">¿Olvidaste tu contraseña?</a>
          </p>

          <p class="text-white-50">¿No tienes cuenta? <router-link to="/servicios/inscripcion" class="text-orange">Inscribirse</router-link></p>
        </form>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'AppIngreso',
  data() {
    return {
      username: '',
      password: '',
      mensaje: ''
    };
  },
  methods: {
    async login() {
      try {
        const response = await fetch('http://localhost:3000/api/login', {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: this.username,
            password: this.password
          })
        });

        if (response.ok) {
          const message = await response.text();
          this.mensaje = message;
          localStorage.setItem('authToken', 'dummy-token');
          this.$router.push('/ruta-protegida');
        } else {
          const errorMessage = await response.text();
          this.mensaje = errorMessage;
        }
      } catch (error) {
        console.error(error);
        this.mensaje = 'Error en el servidor';
      }
    }
  }
}
</script>

<style scoped>
.bg-image {
  background-image: url('@/assets/fondoLogin.jpg'); 
  background-size: cover;
  background-position: center;
}

.card {
  background-color: rgba(0, 0, 0, 0.7); 
  border-radius: 0.5rem;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.5);
}


.logo-circle {
  background-color: #ff8c00;
  color: white;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin: 0 auto;
}

/* Botón de ingreso */
.btn-orange {
  background-color: #ff8c00;
  color: white;
  border: none;
  border-radius: 0.25rem;
}

/* Enlaces en color naranja */
.text-orange {
  color: #ff8c00;
}

.text-orange:hover {
  text-decoration: underline;
}
/* Contenedor del formulario */
.form-container {
    padding: 20px;
    border-radius: 8px;
    max-width: 400px;
    margin: 0 auto;
    color: #fff;
}

/* Texto y estilos adicionales */
h1, p {
    color: #ffffff; /* Asegura que el texto sea visible sobre el fondo transparente */
}

/* Botones y enlaces */
button, a {
    color: #ffffff;
}

</style>
