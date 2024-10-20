<template>
    <div class="mantenimiento-maquinas">
      <h2>Mantenimiento de Máquinas</h2>
  
      <!-- Formulario para agregar o actualizar máquinas -->
      <form @submit.prevent="guardarMaquina">
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
  
        <button type="submit" class="btn btn-primary">
          {{ esEdicion ? 'Actualizar Máquina' : 'Agregar Máquina' }}
        </button>
      </form>
  
      <!-- Listado de máquinas -->
      <h3>Lista de Máquinas</h3>
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
          estado: '',
        },
        maquinas: [],
        esEdicion: false, // Estado para saber si estamos editando o creando
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
            // Si estamos editando, enviamos una petición PUT
            await fetch(`http://localhost:3000/api/maquinas/${this.maquina.id_maquina}`, {
              method: 'PUT',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(this.maquina),
            });
          } else {
            // Si estamos creando una nueva máquina, enviamos una petición POST
            await fetch('http://localhost:3000/api/maquinas', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              credentials: 'include',
              body: JSON.stringify(this.maquina),
            });
          }
          this.resetearFormulario(); // Reiniciar el formulario después de guardar
          this.obtenerMaquinas(); // Actualizar la lista de máquinas
        } catch (error) {
          console.error('Error al guardar la máquina:', error);
        }
      },
      seleccionarMaquina(maquina) {
        this.maquina = { ...maquina }; // Copiar la máquina seleccionada al formulario
        this.esEdicion = true; // Cambiar a modo edición
      },
      async eliminarMaquina(id_maquina) {
        if (confirm('¿Estás seguro de eliminar esta máquina?')) {
          try {
            await fetch(`http://localhost:3000/api/maquinas/${id_maquina}`, {
              method: 'DELETE',
            });
            this.obtenerMaquinas(); // Actualizar la lista de máquinas
          } catch (error) {
            console.error('Error al eliminar la máquina:', error);
          }
        }
      },
      resetearFormulario() {
        this.maquina = { id_maquina: null, descripcion: '', estado: '' };
        this.esEdicion = false;
      },
    },
    mounted() {
      this.obtenerMaquinas(); // Cargar la lista de máquinas cuando se monta el componente
    },
  };
  </script>
  
  <style scoped>
  .mantenimiento-maquinas {
    padding: 20px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  table {
    margin-top: 20px;
    width: 100%;
    border-collapse: collapse;
  }
  
  table, th, td {
    border: 1px solid black;
  }
  
  th, td {
    padding: 8px;
    text-align: left;
  }
  </style>
  