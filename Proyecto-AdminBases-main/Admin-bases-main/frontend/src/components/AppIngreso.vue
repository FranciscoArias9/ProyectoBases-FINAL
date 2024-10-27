<template>
  <section class="vh-75">
    <div class="container py-4 h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-8 col-lg-6 col-xl-5">
          <div class="card bg-dark text-white" style="border-radius: 1rem;">
            <div class="card-body p-4 text-center">
              <div class="mb-md-4 mt-md-3 pb-4">
                <h2 class="fw-bold mb-2 text-uppercase">Login</h2>
                <p class="text-white-50 mb-5">Por favor, iniciar sesión</p>
                
                <form @submit.prevent="login">
                  <div data-mdb-input-init class="form-outline form-white mb-3">
                    <input type="text" v-model="username" class="form-control form-control-lg" placeholder="Usuario" />
                  </div>
                  <div data-mdb-input-init class="form-outline form-white mb-3">
                    <input type="password" v-model="password" class="form-control form-control-lg" placeholder="Contraseña" />
                  </div>

                  <p class="small mb-5 pb-lg-2">
                    <a class="text-white-50" href="#!">Se me olvidó la contraseña</a>
                  </p>
                  
                  <button class="btn btn-outline-light btn-lg px-4" type="submit">Iniciar sesión</button>
                </form>
                
                <p v-if="mensaje" class="text-warning mt-3">{{ mensaje }}</p>
              </div>

              <div>
                <p class="mb-0">¿No tienes cuenta?
                  <router-link to="/servicios/inscripcion" class="text-white-50 fw-bold">Inscribirse</router-link>
                </p>
              </div>
            </div>
          </div>
        </div>
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
          const message = await response.text(); // Manejar texto plano
          this.mensaje = message; // Mostrar mensaje de éxito
          localStorage.setItem('authToken', 'dummy-token'); // Simular almacenamiento de un token
          this.$router.push('/ruta-protegida'); // Redirigir a una página protegida
        } else {
          const errorMessage = await response.text(); // Manejar error como texto plano
          this.mensaje = errorMessage;
        }
      } catch (error) {
        console.error(error);
        this.mensaje = 'Error en el servidor';
      }
    },

    // Método para enviar una solicitud a una ruta protegida
    async solicitarRecursoProtegido() {
      const token = localStorage.getItem('authToken'); // Recuperar el token almacenado
      if (!token) {
        this.mensaje = 'No estás autenticado';
        return;
      }

      try {
        const response = await fetch('http://localhost:3000/api/protected-route', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`, // Enviar el token en el encabezado Authorization
            'Content-Type': 'application/json'
          }
        });

        if (response.ok) {
          const data = await response.text(); // Manejar texto plano
          console.log('Recurso protegido:', data);
        } else {
          this.mensaje = 'No tienes acceso';
        }
      } catch (error) {
        console.error('Error en el servidor', error);
      }
    }
  }
}
</script>
