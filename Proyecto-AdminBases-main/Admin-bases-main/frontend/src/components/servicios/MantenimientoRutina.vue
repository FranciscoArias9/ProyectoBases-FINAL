<template>
  <div class="container">
    <h1 class="my-4 text-center">Mantenimiento de Rutinas</h1>
    
    <!-- Formulario para agregar/editar rutinas -->
    <form @submit.prevent="submitForm" class="mb-4">
      <div class="form-group">
        <label for="nombre">Nombre de la rutina</label>
        <input v-model="rutina.nombre" id="nombre" class="form-control" placeholder="Nombre de la rutina" required />
      </div>

      <div class="form-group">
        <label for="horas">Horas</label>
        <input v-model="rutina.horas" id="horas" type="number" min="1" class="form-control" placeholder="Horas" required />
      </div>

      <!-- Selección del cliente -->
      <div class="form-group">
        <label for="cliente">Cliente</label>
        <select v-model="rutina.cliente" id="cliente" class="form-control" required>
          <option disabled value="">Seleccione un cliente</option>
          <option v-for="cliente in clientes" :key="cliente.cedula" :value="cliente.cedula">
            {{ cliente.nombre }} {{ cliente.apellido1 }} - {{ cliente.cedula }}
          </option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary mt-3">Guardar</button>
    </form>

    <!-- Lista de rutinas -->
    <ul class="list-group">
      <li v-for="rutina in rutinas" :key="rutina.id" class="list-group-item d-flex justify-content-between align-items-center">
        {{ rutina.nombre }} - {{ rutina.horas }} horas - Cliente: {{ rutina.cliente }}
        <div>
          <button @click="editarRutina(rutina)" class="btn btn-warning btn-sm mr-2">Editar</button>
          <button @click="eliminarRutina(rutina.id)" class="btn btn-danger btn-sm">Eliminar</button>
        </div>
      </li>
    </ul>
  </div>
</template>
  
  <script>
  export default {
    data() {
      return {
        rutinas: [],
        clientes: [], // Lista de clientes que existe en la base de datos
        rutina: {
          nombre: '',
          horas: 0,
          cliente: '', // Campo para almacenar el cliente seleccionado
        },
      };
    },
    methods: {
      async fetchRutinas() {
        try{
          const response = await fetch('http://localhost:3000/api/rutinas');
          this.rutinas = await response.json();
        } catch (error) {
          console.error(error);
        }
      },
      async fetchClientes() {
        try{
          const response = await fetch('http://localhost:3000/api/clientes');
          this.clientes = await response.json();
        } catch (error) {
          console.error(error);
        }
      },
      async submitForm() {
        try{
          if (this.rutina.id) {
            // Si la rutina tiene un ID, entonces es una edición
            await fetch(`http://localhost:3000/api/rutinas/${this.rutina.id}`, {
              method: 'PUT',
              body: JSON.stringify(this.rutina),
              headers: { 'Content-Type': 'application/json' },
            });
          } else {
            // Si no tiene ID, entonces es una nueva rutina
            await fetch('http://localhost:3000/api/rutinas', {
              method: 'POST',
              body: JSON.stringify(this.rutina),
              headers: { 'Content-Type': 'application/json' },
            });
          }
          this.fetchRutinas(); // Actualizar la lista después de agregar o editar
        } catch (error) {
          console.error(error);
        }
      },
      async eliminarRutina(id) {
        try{
        await fetch(`http://localhost:3000/api/rutinas/${id}`, { method: 'DELETE' });
        this.fetchRutinas(); // Actualizar la lista después de eliminar
        } catch (error) {
          console.error(error);
        }
      },
      editarRutina(rutina) {
        this.rutina = { ...rutina }; // Cargar la rutina seleccionada en el formulario
      },
    },
    mounted() {
      this.fetchRutinas();  // Cargar las rutinas cuando el componente se monta
      this.fetchClientes(); // Cargar los clientes cuando el componente se monta
    },
  };
  </script>
  