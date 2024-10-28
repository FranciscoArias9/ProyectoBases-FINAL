<template>
  <div class="container my-5">
    <h2 class="text-center mb-2">Mantenimiento de Clientes</h2>

    <!-- Fila que contiene el formulario y la lista de clientes -->
    <div class="row">
      <!-- Columna del formulario de clientes -->
      <div class="col-md-6">
        <form @submit.prevent="saveCliente" class="p-4 bg-light rounded shadow-sm">
          <h4 class="mb-3">{{ esClienteExistente ? 'Actualizar' : 'Agregar' }} Cliente</h4>

          <div class="mb-1">
            <label for="cedula" class="form-label">Cédula:</label>
            <input type="text" id="cedula" v-model="cliente.cedula" @blur="checkClienteExistente" class="form-control"
              required />
          </div>

          <div class="mb-1">
            <label for="nombre" class="form-label">Nombre:</label>
            <input type="text" id="nombre" v-model="cliente.nombre" class="form-control" required />
          </div>

          <div class="mb-1">
            <label for="apellido1" class="form-label">Primer Apellido:</label>
            <input type="text" id="apellido1" v-model="cliente.apellido1" class="form-control" required />
          </div>

          <div class="mb-1">
            <label for="apellido2" class="form-label">Segundo Apellido:</label>
            <input type="text" id="apellido2" v-model="cliente.apellido2" class="form-control" />
          </div>

          <div class="mb-1">
            <label for="direccion" class="form-label">Dirección:</label>
            <input type="text" id="direccion" v-model="cliente.direccion" class="form-control" required />
          </div>

          <div class="mb-1">
            <label for="e_mail" class="form-label">Email:</label>
            <input type="email" id="e_mail" v-model="cliente.e_mail" class="form-control" required />
          </div>

          <button type="submit" class="btn btn-primary w-100">
            {{ esClienteExistente ? 'Actualizar' : 'Agregar' }} Cliente
          </button>
        </form>
      </div>
      <!-- Columna de la lista de clientes -->
      <div class="col-md-6">
        <h4 class="text-center mb-3">Lista de Clientes</h4>
        <ul class="list-group">
          <li v-for="cliente in clientes" :key="cliente.cedula"
            class="list-group-item d-flex justify-content-between align-items-center">
            <div>
              <strong>{{ cliente.nombre }}</strong> - {{ cliente.apellido1 }} {{ cliente.apellido2 }}
              <br />
              <small class="text-muted">{{ cliente.cedula }}</small>
            </div>
            <div>
              <button @click="editCliente(cliente)" class="btn btn-outline-secondary btn-sm me-2">Editar</button>
              <button @click="deleteCliente(cliente.cedula)" class="btn btn-outline-danger btn-sm">Eliminar</button>
            </div>
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
      try {
        const response = await fetch('http://localhost:3000/api/clientes', {
          method: 'GET',
          credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
        });
        this.clientes = await response.json();
      } catch (error) {
        console.error(error);
      }
    },
    async saveCliente() {
      try {
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
      } catch (error) {
        console.error(error);
      }
    },
    async checkClienteExistente() {
      try {
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
      } catch (error) {
        console.error(error);
      }
    },
    editCliente(cliente) {
      this.cliente = { ...cliente };  // Rellenar el formulario con los datos del cliente
      this.esClienteExistente = true; // Indicar que es un cliente existente
    },
    async deleteCliente(cedula) {
      try {
        await fetch(`http://localhost:3000/api/clientes/${cedula}`, { method: 'DELETE', credentials: 'include' });
        this.fetchClientes();
      } catch (error) {
        console.error(error);
      }
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
