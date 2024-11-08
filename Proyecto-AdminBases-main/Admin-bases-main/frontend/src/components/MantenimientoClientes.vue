<template>
  <div>
    <h2>Mantenimiento de Clientes</h2>
    <!-- Formulario para agregar o actualizar clientes -->
    <form @submit.prevent="saveCliente">
      <div>
        <label for="cedula">Cédula:</label>
        <input type="text" v-model="cliente.cedula" @blur="checkClienteExistente" required />
      </div>
      <div>
        <label for="nombre">Nombre:</label>
        <input type="text" v-model="cliente.nombre" required />
      </div>
      <div>
        <label for="apellido1">Primer Apellido:</label>
        <input type="text" v-model="cliente.apellido1" required />
      </div>
      <div>
        <label for="apellido2">Segundo Apellido:</label>
        <input type="text" v-model="cliente.apellido2" />
      </div>
      <div>
        <label for="direccion">Dirección:</label>
        <input type="text" v-model="cliente.direccion" required />
      </div>
      <div>
        <label for="e_mail">Email:</label>
        <input type="email" v-model="cliente.e_mail" required />
      </div>
      <!-- Cambiar el texto del botón basándonos en si el cliente existe o no -->
      <button type="submit">{{ esClienteExistente ? 'Actualizar' : 'Agregar' }} Cliente</button>
    </form>

    <!-- Listado de clientes con opciones de edición y eliminación -->
    <ul>
      <li v-for="cliente in clientes" :key="cliente.cedula">
        {{ cliente.cedula }} - {{ cliente.nombre }} - {{ cliente.apellido1 }} {{ cliente.apellido2 }}
        <button @click="editCliente(cliente)">Editar</button>
        <button @click="deleteCliente(cliente.cedula)">Eliminar</button>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cliente: {
        cedula: '',
        nombre: '',
        apellido1: '',
        apellido2: '',  // Añadimos el campo apellido2
        direccion: '',
        e_mail: ''
      },
      clientes: [],  // Lista de clientes
      esClienteExistente: false, // Indica si el cliente ya existe
    };
  },
  methods: {
    async fetchClientes() {
      const response = await fetch('http://localhost:3000/api/clientes',{
    method: 'GET',
    credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
  });
      this.clientes = await response.json();
    },
    async saveCliente() {
      const method = this.esClienteExistente ? 'PUT' : 'POST';
      const url = this.esClienteExistente ? `http://localhost:3000/api/clientes/${this.cliente.cedula}` : 'http://localhost:3000/api/clientes';
      await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(this.cliente),
      });
      this.fetchClientes();
      this.cliente = { cedula: '', nombre: '', apellido1: '', apellido2: '', direccion: '', e_mail: '' };  // Limpiar formulario
      this.esClienteExistente = false; // Restablecer el estado
    },
    async checkClienteExistente() {
      if (this.cliente.cedula) {
        // Verificar si el cliente existe cuando se sale del campo "cedula"
        const response = await fetch(`http://localhost:3000/api/clientes/${this.cliente.cedula}`);
        if (response.ok) {
          const clienteExistente = await response.json();
          if (clienteExistente) {
            this.cliente = clienteExistente; // Llenar el formulario con el cliente existente
            this.esClienteExistente = true;
          } else {
            this.esClienteExistente = false;
          }
        } else {
          this.esClienteExistente = false;
        }
      }
    },
    editCliente(cliente) {
      this.cliente = { ...cliente };  // Rellenar el formulario con los datos del cliente
      this.esClienteExistente = true; // Indicar que es un cliente existente
    },
    async deleteCliente(cedula) {
      await fetch(`http://localhost:3000/api/clientes/${cedula}`, { method: 'DELETE',credentials: 'include' });
      this.fetchClientes();
    }
  },
  mounted() {
    this.fetchClientes();  // Cargar la lista de clientes cuando el componente se monte
  }
};
</script>

<style scoped>
/* Estilos personalizados para el formulario */
</style>
