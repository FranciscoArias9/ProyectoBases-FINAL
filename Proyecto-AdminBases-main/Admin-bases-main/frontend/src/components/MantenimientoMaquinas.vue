<template>
  <div class="mantenimiento-maquinas">
    <h2 class="text-center">Mantenimiento de Máquinas</h2>

    <!-- Formulario para agregar o actualizar máquinas -->
    <form @submit.prevent="guardarMaquina" class="formulario-maquina">
      <div class="form-group">
        <label for="descripcion">Descripción</label>
        <input
          v-model="maquina.descripcion"
          type="text"
          id="descripcion"
          placeholder="Descripción de la máquina"
          required
        />
      </div>
      <div class="form-group">
        <label for="estado">Estado</label>
        <input
          v-model="maquina.estado"
          type="text"
          id="estado"
          placeholder="Estado de la máquina"
          required
        />
      </div>

      <button type="submit" class="btn btn-orange">
        {{ esEdicion ? 'Actualizar Máquina' : 'Agregar Máquina' }}
      </button>
    </form>

    <!-- Listado de máquinas -->
    <h3 class="text-center mt-5">Lista de Máquinas</h3>
    <table class="table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Descripción</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="maquina in maquinas" :key="maquina.id_maquina">
          <td>{{ maquina.id_maquina }}</td>
          <td>{{ maquina.descripcion }}</td>
          <td>{{ maquina.estado }}</td>
          <td>
            <button @click="seleccionarMaquina(maquina)" class="btn btn-warning">Editar</button>
            <button @click="eliminarMaquina(maquina.id_maquina)" class="btn btn-danger">Eliminar</button>
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
      maquina: {
        id_maquina: null,
        descripcion: '',
        estado: ''
      },
      maquinas: [],
      esEdicion: false
    };
  },
  methods: {
    async obtenerMaquinas() {
      try {
        const response = await fetch('http://localhost:3000/api/maquinas');
        const data = await response.json();
        this.maquinas = data;
      } catch (error) {
        console.error('Error al obtener las máquinas:', error);
      }
    },
    async guardarMaquina() {
      try {
        if (this.esEdicion) {
          await fetch(`http://localhost:3000/api/maquinas/${this.maquina.id_maquina}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(this.maquina)
          });
        } else {
          await fetch('http://localhost:3000/api/maquinas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify(this.maquina)
          });
        }
        this.resetearFormulario();
        this.obtenerMaquinas();
      } catch (error) {
        console.error('Error al guardar la máquina:', error);
      }
    },
    seleccionarMaquina(maquina) {
      this.maquina = { ...maquina };
      this.esEdicion = true;
    },
    async eliminarMaquina(id_maquina) {
      if (confirm('¿Estás seguro de eliminar esta máquina?')) {
        try {
          await fetch(`http://localhost:3000/api/maquinas/${id_maquina}`, { method: 'DELETE' });
          this.obtenerMaquinas();
        } catch (error) {
          console.error('Error al eliminar la máquina:', error);
        }
      }
    },
    resetearFormulario() {
      this.maquina = { id_maquina: null, descripcion: '', estado: '' };
      this.esEdicion = false;
    }
  },
  mounted() {
    this.obtenerMaquinas();
  }
};
</script>

<style scoped>

body {
  background-color: #000;
  color: #ffa500;
}


.mantenimiento-maquinas {
  padding: 20px;
  width: 100%;
}

h2, h3 {
  color: #ffa500;
  text-align: center;
}


.formulario-maquina {
  width: 40%;
  margin: 0 auto;
  background-color: #1c1c1c;
  padding: 20px;
  border-radius: 10px;
  border: 2px solid #ffa500;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  color: #ffa500;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  background-color: #333;
  color: #fff;
  border: 1px solid #ffa500;
  border-radius: 5px;
}

input:focus {
  outline: none;
  box-shadow: 0 0 5px #ffa500;
}

.btn {
  background-color: #ffa500;
  color: #000;
  padding: 10px;
  border: none;
  cursor: pointer;
  margin-top: 10px;
  width: 100%;
}

.btn:hover {
  background-color: #ff8c00;
}


.table {
  width: 80%;
  margin: 20px auto;
  background-color: #1c1c1c;
  color: #ffa500;
  border-collapse: collapse;
  border-radius: 15px;
}

.table th, .table td {
  padding: 10px;
  border: 1px solid #ffa500;
  background-color: #1c1c1c;
  color:#fff
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: #2a2a2a;
}

.btn {
  background-color: #ffa500;
  color: #000;
  padding: 5px 12px;
  border: none;
  border-radius: 5px; 
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn:hover {
  background-color: #ff8c00;
}


.btn-warning {
  background-color: #ffcc00;
  color: #000;
  padding: 5px 10px;
  margin-right: 5px;
}

.btn-warning:hover {
  background-color: #e6b800;
}


.btn-danger {
  background-color: #ff4444;
  color: #fff;
  padding: 5px 10px;
  margin-left: 5px;
}

.btn-danger:hover {
  background-color: #e63939;
}


.table td button {
  display: inline-block;
  width: auto; 
  margin: 2px;
}
</style>
