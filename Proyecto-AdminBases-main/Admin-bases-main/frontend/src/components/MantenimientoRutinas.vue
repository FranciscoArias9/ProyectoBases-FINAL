<template>
  <div class="container my-4">
    <h1 class="text-center mb-4">Mantenimiento de Rutinas</h1>

    <div class="row">
      <div class="col-md-6">
        <form @submit.prevent="guardarRutina" class="p-4 bg-light rounded shadow-sm" style="max-width: 100%;">
          <div class="mb-2">
            <label for="nombre" class="form-label">Nombre de la Rutina:</label>
            <input type="text" id="nombre" v-model="rutina.nombre" class="form-control" required />
          </div>

          <div class="mb-2">
            <label for="fecha" class="form-label">Fecha:</label>
            <input type="date" id="fecha" v-model="rutina.fecha" class="form-control" required />
          </div>

          <div class="mb-2">
            <label for="horas" class="form-label">Horas:</label>
            <input type="number" id="horas" v-model="rutina.horas" class="form-control" required />
          </div>

          <div class="mb-2">
            <label for="cliente" class="form-label">Cliente:</label>
            <select id="cliente" v-model="rutina.cliente" class="form-select">
              <option v-for="cliente in clientes" :key="cliente?.cedula" :value="cliente?.cedula">
                {{ cliente?.nombre }} - {{ cliente?.cedula }}
              </option>
            </select>
          </div>

          <div class="mb-2">
            <label for="instructor" class="form-label">Instructor:</label>
            <select id="instructor" v-model="rutina.instructor" class="form-select">
              <option v-for="instructor in instructores" :key="instructor?.id" :value="instructor?.id">
                {{ instructor?.nombre }} - {{ instructor?.id }}
              </option>
            </select>
          </div>

          <div class="mb-2">
            <label for="maquina" class="form-label">Máquina:</label>
            <select id="maquina" v-model="rutina.maquina" class="form-select">
              <option v-for="maquina in maquinas" :key="maquina?.id_maquina" :value="maquina?.id_maquina">
                {{ maquina?.descripcion }} - {{ maquina?.id_maquina }}
              </option>
            </select>
          </div>

          <button type="submit" class="btn btn-primary w-100">
            Guardar Rutina
          </button>
        </form>
      </div>

      <div class="col-md-6">
        <h2 class="text-center my-4">Lista de Rutinas</h2>
        <ul class="list-group">
          <li v-for="rutina in rutinas" :key="rutina?.id_rutina" class="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <strong>{{ rutina?.nombre }}</strong> - {{ rutina?.fecha }} - {{ rutina?.horas }} horas
            </div>
            <button @click="eliminarRutina(rutina?.id_rutina)" class="btn btn-danger btn-sm">Eliminar</button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rutina: {
        nombre: '',
        fecha: '',
        horas: 0,
        cliente: '',
        instructor: '',
        maquina: ''
      },
      clientes: [],
      instructores: [],
      maquinas: [],
      rutinas: []
    };
  },
  created() {
    this.cargarClientes();
    this.cargarInstructores();
    this.cargarMaquinas();
    this.cargarRutinas();
  },
  methods: {
    async cargarClientes() {
      try {
        const response = await fetch('http://localhost:3000/api/clientes', {
          method: 'GET',
          credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
        });
        this.clientes = await response.json() ?? [];
      } catch (error) {
        console.error('Error al cargar los clientes:', error);
      }
    },
    async cargarMaquinas() {
      try {
        const response = await fetch('http://localhost:3000/api/maquinas', {
          method: 'GET',
          credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
        });
        this.maquinas = await response.json() ?? [];
      } catch (error) {
        console.error('Error al cargar las maquinas:', error);
      }
    },
    async cargarInstructores() {
      try {
        const response = await fetch('http://localhost:3000/api/empleados', {
          method: 'GET',
          credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
        });
        this.instructores = await response.json() ?? [];
      } catch (error) {
        console.error('Error al cargar los instructores:', error);
      }
    },

    async cargarRutinas() {
      try {
        const response = await fetch('http://localhost:3000/api/rutinas', {
          method: 'GET',
          credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
        });
        this.rutinas = await response.json() ?? [];
      } catch (error) {
        console.error('Error al cargar las rutinas:', error);
      }
    },
    async guardarRutina() {
      try {
        const nuevaRutina = {
          nombre: this.rutina.nombre,
          fecha: this.rutina.fecha,
          horas: this.rutina.horas,
          cliente: { cedula: this.rutina.cliente },
          instructor: this.rutina.instructor,
          maquina: { id_maquina: this.rutina.maquina }
        };

        // Siempre enviamos una petición POST para crear una nueva rutina
        await fetch('http://localhost:3000/api/rutinas', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
          body: JSON.stringify(nuevaRutina),
        });

        this.limpiarFormulario(); // Reiniciar el formulario después de guardar
        this.cargarRutinas(); // Actualizar la lista de rutinas
      } catch (error) {
        console.error('Error al guardar la rutina:', error);
      }
    }
    ,


    async eliminarRutina(id_rutina) {
      if (confirm('¿Estás seguro de eliminar esta rutina?')) {
        try {
          await fetch(`http://localhost:3000/api/rutinas/${id_rutina}`, {
            method: 'DELETE',
          });
          this.cargarRutinas(); // Actualizar la lista de rutinas
        } catch (error) {
          console.error('Error al eliminar la rutina:', error);
        }
      }
    },
    /*async eliminarRutina(id_rutina) {
      fetch(`/api/rutinas/${id_rutina}`, {
        method: 'DELETE'
      }).then(() => {
        this.rutinas = this.rutinas.filter(rutina => rutina.id_rutina !== id_rutina);
      });
    },*/
    limpiarFormulario() {
      this.rutina = {
        nombre: '',
        fecha: '',
        horas: 0,
        cliente: '',
        instructor: '',
        maquina: ''
      };
    }
  }
};
</script>

<style scoped>
.mantenimiento-rutinas {
  padding: 20px;
}
</style>