<template>
    <div class="mantenimiento-cursos">
      <h2>Mantenimiento de Cursos</h2>
  
      <!-- Formulario para agregar o actualizar cursos -->
      <form @submit.prevent="guardarCurso">
        <div class="form-group">
          <label for="descripcion">Descripción</label>
          <input
            v-model="curso.descripcion"
            type="text"
            id="descripcion"
            placeholder="Descripción del curso"
            required
          />
        </div>
        <div class="form-group">
          <label for="nombre">Nombre</label>
          <input
            v-model="curso.nombre"
            type="text"
            id="nombre"
            placeholder="Nombre del curso"
            required
          />
        </div>
        <div class="form-group">
          <label for="horario">Horario</label>
          <input
            v-model="curso.horario"
            type="text"
            id="horario"
            placeholder="Horario del curso"
            required
          />
        </div>
  
        <button type="submit" class="btn btn-primary">
          {{ esEdicion ? 'Actualizar Curso' : 'Agregar Curso' }}
        </button>
      </form>
  
      <!-- Listado de cursos -->
      <h3>Lista de Cursos</h3>
      <table class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Descripción</th>
            <th>Nombre</th>
            <th>Horario</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="curso in cursos" :key="curso.id_curso">
            <td>{{ curso.id_curso }}</td>
            <td>{{ curso.descripcion }}</td>
            <td>{{ curso.nombre }}</td>
            <td>{{ curso.horario }}</td>
            <td>
              <button @click="seleccionarCurso(curso)" class="btn btn-warning">Editar</button>
              <button @click="eliminarCurso(curso.id_curso)" class="btn btn-danger">Eliminar</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        curso: {
          id_curso: null,
          descripcion: '',
          nombre: '',
          horario: ''
        },
        cursos: [],
        esEdicion: false, // Estado para saber si estamos editando o creando
      };
    },
    methods: {
      async obtenerCursos() {
        try {
          const response = await fetch('http://localhost:3000/api/cursos');
          const data = await response.json();
          this.cursos = data;
        } catch (error) {
          console.error('Error al obtener los cursos:', error);
        }
      },
      async guardarCurso() {
        try {
          if (this.esEdicion) {
            // Si estamos editando, enviamos una petición PUT
            await fetch(`http://localhost:3000/api/cursos/${this.curso.id_curso}`, {
              method: 'PUT',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(this.curso),
            });
          } else {
            // Si estamos creando un nuevo curso, enviamos una petición POST
            await fetch('http://localhost:3000/api/cursos', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(this.curso),
            });
          }
          this.resetearFormulario(); // Reiniciar el formulario después de guardar
          this.obtenerCursos(); // Actualizar la lista de cursos
        } catch (error) {
          console.error('Error al guardar el curso:', error);
        }
      },
      seleccionarCurso(curso) {
        this.curso = { ...curso }; // Copiar el curso seleccionado al formulario
        this.esEdicion = true; // Cambiar a modo edición
      },
      async eliminarCurso(id_curso) {
        if (confirm('¿Estás seguro de eliminar este curso?')) {
          try {
            await fetch(`http://localhost:3000/api/cursos/${id_curso}`, {
              method: 'DELETE',
            });
            this.obtenerCursos(); // Actualizar la lista de cursos
          } catch (error) {
            console.error('Error al eliminar el curso:', error);
          }
        }
      },
      resetearFormulario() {
        this.curso = { id_curso: null, descripcion: '', nombre: '', horario: '' };
        this.esEdicion = false;
      },
    },
    mounted() {
      this.obtenerCursos(); // Cargar la lista de cursos cuando se monta el componente
    },
  };
  </script>
  
  <style scoped>
  .mantenimiento-cursos {
    padding: 20px;
  }
  </style>
  
  