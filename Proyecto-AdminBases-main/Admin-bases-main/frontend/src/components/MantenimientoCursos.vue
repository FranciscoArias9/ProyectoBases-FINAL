<template>
  <div class="mantenimiento-cursos container">
    <h2 class="text-center mb-4">Mantenimiento de Cursos</h2>

    <!-- Formulario para agregar o actualizar cursos -->
    <form @submit.prevent="guardarCurso" class="form">
      <div class="form-group mb-3">
        <label for="descripcion" class="form-label">Descripción</label>
        <input
          v-model="curso.descripcion"
          type="text"
          id="descripcion"
          class="form-control"
          placeholder="Descripción del curso"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="nombre" class="form-label">Nombre</label>
        <input
          v-model="curso.nombre"
          type="text"
          id="nombre"
          class="form-control"
          placeholder="Nombre del curso"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="horario" class="form-label">Horario</label>
        <input
          v-model="curso.horario"
          type="text"
          id="horario"
          class="form-control"
          placeholder="Horario del curso"
          required
        />
      </div>

      <button type="submit" class="btn btn-orange w-100">
        {{ esEdicion ? 'Actualizar Curso' : 'Agregar Curso' }}
      </button>
    </form>

    <!-- Listado de cursos -->
    <h3 class="text-center mt-5 mb-3">Lista de Cursos</h3>
    <table class="table table-dark table-striped">
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
            <button @click="seleccionarCurso(curso)" class="btn btn-warning btn-sm">Editar</button>
            <button @click="eliminarCurso(curso.id_curso)" class="btn btn-danger btn-sm">Eliminar</button>
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
      esEdicion: false,
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
          await fetch(`http://localhost:3000/api/cursos/${this.curso.id_curso}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(this.curso),
          });
        } else {
          await fetch('http://localhost:3000/api/cursos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(this.curso),
          });
        }
        this.resetearFormulario();
        this.obtenerCursos();
      } catch (error) {
        console.error('Error al guardar el curso:', error);
      }
    },
    seleccionarCurso(curso) {
      this.curso = { ...curso };
      this.esEdicion = true;
    },
    async eliminarCurso(id_curso) {
      if (confirm('¿Estás seguro de eliminar este curso?')) {
        try {
          await fetch(`http://localhost:3000/api/cursos/${id_curso}`, {
            method: 'DELETE',
            credentials: 'include',
          });
          this.obtenerCursos();
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
    this.obtenerCursos();
  },
};
</script>

<style scoped>
body {
  background-color: #000; 
  color: #fff; 
  margin: 0;
  padding: 0;
  font-family: 'Arial', sans-serif;
}


.container {
  max-width: 80%;
  margin: 40px auto;
  background-color: #1c1c1c; 
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
}

h2, h3 {
  color: #ffa500;
  text-align: center;
  margin-bottom: 20px;
}


.form-label {
  color: #ffa500;
}

.form-control {
  background-color: #333;
  color: #fff;
  border: 2px solid #ffa500;
  border-radius: 8px;
  padding: 10px;
}

.form-control:focus {
  outline: none;
  box-shadow: 0 0 8px #ffa500;
}


.btn-orange {
  background-color: #ffa500;
  color: #000;
  border: none;
  border-radius: 8px;
  padding: 12px;
  font-weight: bold;
  margin-top: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-orange:hover {
  background-color: #ff8c00;
}

.btn-warning {
  background-color: #ffb84d;
  border: none;
}

.btn-danger {
  background-color: #e63946;
  border: none;
}

.btn-sm {
  margin-right: 5px;
}

/* Tabla */
.table {
  color: #fff;
  margin-top: 30px;
  border-collapse: collapse;
  width: 100%;
}

.table-dark {
  background-color: #333;
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: #2a2a2a;
}

.table th, .table td {
  border: 1px solid #ffa500;
  padding: 15px;
  text-align: center;
}

.table th {
  background-color: #444;
}

.table-hover tbody tr:hover {
  background-color: #444;
}


.empty-message {
  text-align: center;
  margin-top: 20px;
  font-style: italic;
  color: #bbb;
}

</style>
