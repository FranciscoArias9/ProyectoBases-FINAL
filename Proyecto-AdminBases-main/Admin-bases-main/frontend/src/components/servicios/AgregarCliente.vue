<template>
    <div class="container">
      <h2>Agregar Cliente</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="cedula">Cédula:</label>
          <input type="text" id="cedula" v-model="cliente.cedula" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="nombre">Nombre:</label>
          <input type="text" id="nombre" v-model="cliente.nombre" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="apellido1">Primer Apellido:</label>
          <input type="text" id="apellido1" v-model="cliente.apellido1" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="apellido2">Segundo Apellido:</label>
          <input type="text" id="apellido2" v-model="cliente.apellido2" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="direccion">Dirección:</label>
          <textarea id="direccion" v-model="cliente.direccion" class="form-control" required></textarea>
        </div>
  
        <div class="form-group">
          <label for="email">Correo Electrónico:</label>
          <input type="email" id="email" v-model="cliente.e_mail" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="celular">Celular:</label>
          <input type="text" id="celular" v-model="cliente.celular" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="tel_habitacion">Teléfono de Habitación:</label>
          <input type="text" id="tel_habitacion" v-model="cliente.tel_habitacion" class="form-control" required />
        </div>
  
        <div class="form-group">
          <label for="password">Contraseña:</label>
          <input type="password" id="password" v-model="cliente.password" class="form-control" required />
        </div>
  
        <button type="submit" class="btn btn-primary">Agregar Cliente</button>
        <p v-if="mensaje" class="text-success">{{ mensaje }}</p>
      </form>
    </div>
  </template>
  
  <script>
  export default {
    name: 'AgregarCliente',
    data() {
      return {
        cliente: {
          cedula: '',
          nombre: '',
          apellido1: '',
          apellido2: '',
          direccion: '',
          e_mail: '',
          celular: '',
          tel_habitacion: '',
          password: ''
        },
        mensaje: ''
      };
    },
    methods: {
      async submitForm() {
        try {
          const response = await fetch('http://localhost:3000/api/clientes', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.cliente),
          });
  
          if (response.ok) {
            this.mensaje = 'Cliente agregado exitosamente.';
            // Resetear el formulario
            this.cliente = {
              cedula: '',
              nombre: '',
              apellido1: '',
              apellido2: '',
              direccion: '',
              e_mail: '',
              celular: '',
              tel_habitacion: '',
              password: ''
            };
          } else {
            this.mensaje = 'Error al agregar el cliente.';
          }
        } catch (error) {
          console.error('Error:', error);
          this.mensaje = 'Ocurrió un error al procesar la solicitud.';
        }
      }
    }
  }
  </script>
  
  <style scoped>
  .container {
    max-width: 600px;
    margin: 0 auto;
  }
  </style>
  