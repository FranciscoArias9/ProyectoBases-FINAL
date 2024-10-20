<template>
    <div>
      <h1>Mantenimiento de Rutinas</h1>
      <!-- Formulario para agregar/editar rutinas -->
      <form @submit.prevent="submitForm">
        <input v-model="rutina.nombre" placeholder="Nombre de la rutina" />
        <input v-model="rutina.horas" placeholder="Horas" type="number" />
  
        <!-- Selección del cliente -->
        <select v-model="rutina.cliente">
          <option disabled value="">Seleccione un cliente</option>
          <option v-for="cliente in clientes" :key="cliente.cedula" :value="cliente.cedula">
            {{ cliente.nombre }} {{ cliente.apellido1 }} - {{ cliente.cedula }}
          </option>
        </select>
  
        <button type="submit">Guardar</button>
      </form>
  
      <!-- Lista de rutinas -->
      <ul>
        <li v-for="rutina in rutinas" :key="rutina.id">
          {{ rutina.nombre }} - {{ rutina.horas }} horas - Cliente: {{ rutina.cliente }}
          <button @click="editarRutina(rutina)">Editar</button>
          <button @click="eliminarRutina(rutina.id)">Eliminar</button>
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
        const response = await fetch('http://localhost:3000/api/rutinas');
        this.rutinas = await response.json();
      },
      async fetchClientes() {
        const response = await fetch('http://localhost:3000/api/clientes');
        this.clientes = await response.json();
      },
      async submitForm() {
        await fetch('http://localhost:3000/api/rutinas', {
          method: 'POST',
          body: JSON.stringify(this.rutina),
          headers: { 'Content-Type': 'application/json' },
        });
        this.fetchRutinas(); // Actualizar la lista después de agregar
      },
      async eliminarRutina(id) {
        await fetch(`http://localhost:3000/api/rutinas/${id}`, { method: 'DELETE' });
        this.fetchRutinas(); // Actualizar la lista después de eliminar
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
  