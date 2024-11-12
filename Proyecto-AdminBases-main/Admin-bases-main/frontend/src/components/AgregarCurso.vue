<template>
  <div class="container">
    <h2 class="text-center mb-4">Agregar Curso</h2>
    <form @submit.prevent="submitForm" class="form">
      <div class="form-group mb-3">
        <label for="nombre" class="form-label">Nombre del Curso:</label>
        <input type="text" id="nombre" v-model="curso.nombre" class="form-control" required />
      </div>

      <div class="form-group mb-3">
        <label for="descripcion" class="form-label">Descripción:</label>
        <textarea id="descripcion" v-model="curso.descripcion" class="form-control" required></textarea>
      </div>

      <div class="form-group mb-3">
        <label for="horario" class="form-label">Horario:</label>
        <input type="text" id="horario" v-model="curso.horario" class="form-control" required />
      </div>

      <button type="submit" class="btn btn-orange w-100">Agregar Curso</button>
      <p v-if="mensaje" class="text-success mt-3">{{ mensaje }}</p>
    </form>

    <div v-if="cursos.length" class="table-container mt-5">
      <h3 class="text-center mb-3">Lista de Cursos</h3>
      <table class="table table-dark table-striped">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Horario</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="curso in cursos" :key="curso.nombre">
            <td>{{ curso.nombre }}</td>
            <td>{{ curso.descripcion }}</td>
            <td>{{ curso.horario }}</td>
          </tr>
        </tbody>
      </table>
    </div>
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
      cursos: [], // Para almacenar los cursos agregados
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
          // Agregar el curso a la lista de cursos
          this.cursos.push({ ...this.curso });
          // Resetear el formulario
          this.curso = { nombre: '', descripcion: '', horario: '' };
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

</style>
