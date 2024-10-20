<template>
    <button @click="logout">Cerrar sesión</button>
  </template>
  
  <script>
  export default {
    methods: {
      async logout() {
        try {
          const response = await fetch('http://localhost:3000/api/logout', {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' }
          });
  
          if (response.ok) {
            this.$router.push('/login'); // Redirigir a la página de login
          }
        } catch (error) {
          console.error('Error al cerrar sesión:', error);
        }
      }
    }
  }
  </script>
  