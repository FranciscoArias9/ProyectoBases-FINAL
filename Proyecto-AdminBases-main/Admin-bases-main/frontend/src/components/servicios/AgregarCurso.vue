<template>
  <div class="container mt-4">
    <h2 class="text-center mb-4">Agregar Curso</h2>
    <form @submit.prevent="submitForm">
      <div class="form-group">
        <label for="nombre">Nombre del Curso:</label>
        <input type="text" id="nombre" v-model="curso.nombre" class="form-control" required />
      </div>

      <div class="form-group mt-3">
        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" v-model="curso.descripcion" class="form-control" required></textarea>
      </div>

      <div class="form-group mt-3">
        <label for="horario">Horario:</label>
        <input type="text" id="horario" v-model="curso.horario" class="form-control" required />
      </div>

      <button type="submit" class="btn btn-primary mt-3">Agregar Curso</button>
      <p v-if="mensaje" class="text-success mt-3">{{ mensaje }}</p>
    </form>
  </div>
</template>
  
  <script>
  export default {
    name: 'AgregarCurso',
    data() {
      return {
        curso: {
          nombre: '',
          descripcion: '',
          horario: '',
        },
        mensaje: ''
      };
    },
    methods: {
      async submitForm() {
        try {
          const response = await fetch('http://localhost:3000/api/cursos', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(this.curso),
          });
  
          if (response.ok) {
            this.mensaje = 'Curso agregado exitosamente.';
            // Resetear el formulario
            //this.curso = { nombre: '', descripcion: '', horario: '' };
          } else {
            this.mensaje = 'Error al agregar el curso.';
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
  