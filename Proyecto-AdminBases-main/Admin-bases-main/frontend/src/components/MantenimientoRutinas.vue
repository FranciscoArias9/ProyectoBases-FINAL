<template>
  <div class="mantenimiento-rutinas">
    <h2>Mantenimiento de Rutinas</h2>
    
    <!-- Formulario para agregar o actualizar rutinas -->
    <form @submit.prevent="guardarRutina">
      <div class="form-group">
        <label for="nombre">Nombre de la Rutina:</label>
        <input type="text" id="nombre" v-model="rutina.nombre" required />
      </div>

      <div class="form-group">
        <label for="fecha">Fecha:</label>
        <input type="date" id="fecha" v-model="rutina.fecha" required />
      </div>

      <div class="form-group">
        <label for="horas">Horas:</label>
        <input type="number" id="horas" v-model="rutina.horas" required />
      </div>

      <div class="form-group">
        <label for="cliente">Cliente:</label>
        <select id="cliente" v-model="rutina.cliente">
          <option v-for="cliente in clientes" :key="cliente.cedula" :value="cliente.cedula">
            {{ cliente.nombre }} - {{ cliente.cedula }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="instructor">Instructor:</label>
        <select id="instructor" v-model="rutina.instructor">
          <option v-for="instructor in instructores" :key="instructor.id" :value="instructor.id">
            {{ instructor.nombre }} - {{ instructor.id }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="maquina">Máquina:</label>
        <select id="maquina" v-model="rutina.maquina">
          <option v-for="maquina in maquinas" :key="maquina.id_maquina" :value="maquina.id_maquina">
            {{ maquina.descripcion }} - {{ maquina.id_maquina }}
          </option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary">Guardar Rutina</button>
    </form>

    <!-- Lista de rutinas en una tabla -->
    <h3>Lista de Rutinas</h3>
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Fecha</th>
          <th>Horas</th>
          <th>Cliente</th>
          <th>Instructor</th>
          <th>Máquina</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="rutina in rutinas" :key="rutina.id_rutina">
          <td>{{ rutina.id_rutina }}</td>
          <td>{{ rutina.nombre }}</td>
          <td>{{ rutina.fecha }}</td>
          <td>{{ rutina.horas }}</td>
          <td>{{ rutina.cliente.cedula }}</td>
          <td>{{ rutina.instructor }}</td>
          <td>{{ rutina.maquina.id_maquina }}</td>
          <td>
            <button @click="eliminarRutina(rutina.id_rutina)" class="btn btn-danger">Eliminar</button>
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
          credentials: 'include'
        });
        const data = await response.json();
        this.clientes = data.filter(cliente => cliente.estado === '"inscrito"');
      } catch (error) {
        console.error('Error al cargar los clientes:', error);
      }
    },
    async cargarMaquinas() {
      const response = await fetch('http://localhost:3000/api/maquinas', {
        method: 'GET',
        credentials: 'include'
      });
      this.maquinas = await response.json();
    },
    async cargarInstructores() {
      const response = await fetch('http://localhost:3000/api/empleados', {
        method: 'GET',
        credentials: 'include'
      });
      this.instructores = await response.json();
    },
    async cargarRutinas() {
      const response = await fetch('http://localhost:3000/api/rutinas', {
        method: 'GET',
        credentials: 'include'
      });
      this.rutinas = await response.json();
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
        await fetch('http://localhost:3000/api/rutinas', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
          body: JSON.stringify(nuevaRutina)
        });
        this.limpiarFormulario();
        this.cargarRutinas();
      } catch (error) {
        console.error('Error al guardar la rutina:', error);
      }
    },
    async eliminarRutina(id_rutina) {
      if (confirm('¿Estás seguro de eliminar esta rutina?')) {
        try {
          await fetch(`http://localhost:3000/api/rutinas/${id_rutina}`, {
            method: 'DELETE',
            credentials: 'include'
          });
          this.cargarRutinas();
        } catch (error) {
          console.error('Error al eliminar la rutina:', error);
        }
      }
    },
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
  max-width: 80%;
  margin: 40px auto;
  background-color: #1c1c1c;
  padding: 30px;
  border-radius: 15px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.7);
  color:#fff
}

h2, h3 {
  color: #ffa500;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

input, select {
  width: 100%;
  padding: 8px;
  margin: 5px 0;
  background-color: #222;
  color: #ffa500;
  border: 1px solid #ffa500;
  border-radius: 5px;
}

.btn {
  background-color: #ffa500;
  padding: 8px 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn:hover {
  background-color: #ff8c00;
}

.btn-danger {
  background-color: #ff4444;
  color: #fff;
}

.btn-danger:hover {
  background-color: #e63939;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.table th, .table td {
  border: 1px solid #ffa500;
  padding: 10px;
  text-align: center;
  color:#fff;
}

.table th {
  background-color: #222;
}

.table td {
  background-color: #111;
}
</style>
