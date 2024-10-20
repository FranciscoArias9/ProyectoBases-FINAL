<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <input type="text" v-model="username" placeholder="Usuario" />
      <input type="password" v-model="password" placeholder="Contraseña" />
      <button type="submit">Iniciar sesión</button>
    </form>
    <p v-if="mensaje">{{ mensaje }}</p>
  </div>
</template>

<script>
export default {
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
