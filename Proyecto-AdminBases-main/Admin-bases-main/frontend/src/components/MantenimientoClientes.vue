<template>
  <div class="mantenimiento-clientes container">
    <h2 class="text-center mb-4">Mantenimiento de Clientes</h2>

    <!-- Formulario para agregar o actualizar clientes -->
    <form @submit.prevent="saveCliente" class="form">
      <div class="form-group mb-3">
        <label for="cedula" class="form-label">Cédula:</label>
        <input
          type="text"
          v-model="cliente.cedula"
          @blur="checkClienteExistente"
          class="form-control"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="nombre" class="form-label">Nombre:</label>
        <input
          type="text"
          v-model="cliente.nombre"
          class="form-control"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="apellido1" class="form-label">Primer Apellido:</label>
        <input
          type="text"
          v-model="cliente.apellido1"
          class="form-control"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="apellido2" class="form-label">Segundo Apellido:</label>
        <input
          type="text"
          v-model="cliente.apellido2"
          class="form-control"
        />
      </div>
      <div class="form-group mb-3">
        <label for="direccion" class="form-label">Dirección:</label>
        <input
          type="text"
          v-model="cliente.direccion"
          class="form-control"
          required
        />
      </div>
      <div class="form-group mb-3">
        <label for="e_mail" class="form-label">Email:</label>
        <input
          type="email"
          v-model="cliente.e_mail"
          class="form-control"
          required
        />
      </div>

      <button type="submit" class="btn btn-orange w-100">
        {{ esClienteExistente ? 'Actualizar Cliente' : 'Agregar Cliente' }}
      </button>
    </form>

    <!-- Listado de clientes -->
    <h3 class="text-center mt-5 mb-3">Lista de Clientes</h3>
    <table class="table table-dark table-striped">
      <thead>
        <tr>
          <th>Cédula</th>
          <th>Nombre</th>
          <th>Apellido 1</th>
          <th>Apellido 2</th>
          <th>Dirección</th>
          <th>Email</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="cliente in clientes" :key="cliente.cedula">
          <td>{{ cliente.cedula }}</td>
          <td>{{ cliente.nombre }}</td>
          <td>{{ cliente.apellido1 }}</td>
          <td>{{ cliente.apellido2 }}</td>
          <td>{{ cliente.direccion }}</td>
          <td>{{ cliente.e_mail }}</td>
          <td>
            <button @click="editCliente(cliente)" class="btn btn-warning btn-sm">Editar</button>
            <button @click="deleteCliente(cliente.cedula)" class="btn btn-danger btn-sm">Eliminar</button>
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
      cliente: {
        cedula: '',
        nombre: '',
        apellido1: '',
        apellido2: '',
        direccion: '',
        e_mail: ''
      },
      clientes: [],
      esClienteExistente: false
    };
  },
  methods: {
    async fetchClientes() {
      const response = await fetch('http://localhost:3000/api/clientes', { credentials: 'include' });
      this.clientes = await response.json();
    },
    async saveCliente() {
      const method = this.esClienteExistente ? 'PUT' : 'POST';
      const url = this.esClienteExistente
        ? `http://localhost:3000/api/clientes/${this.cliente.cedula}`
        : 'http://localhost:3000/api/clientes';
      await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(this.cliente)
      });
      this.fetchClientes();
      this.resetForm();
    },
    async checkClienteExistente() {
      if (this.cliente.cedula) {
        const response = await fetch(`http://localhost:3000/api/clientes/${this.cliente.cedula}`);
        if (response.ok) {
          const clienteExistente = await response.json();
          if (clienteExistente) {
            this.cliente = clienteExistente;
            this.esClienteExistente = true;
          } else {
            this.esClienteExistente = false;
          }
        }
      }
    },
    editCliente(cliente) {
      this.cliente = { ...cliente };
      this.esClienteExistente = true;
    },
    async deleteCliente(cedula) {
      await fetch(`http://localhost:3000/api/clientes/${cedula}`, { method: 'DELETE', credentials: 'include' });
      this.fetchClientes();
    },
    resetForm() {
      this.cliente = { cedula: '', nombre: '', apellido1: '', apellido2: '', direccion: '', e_mail: '' };
      this.esClienteExistente = false;
    }
  },
  mounted() {
    this.fetchClientes();
  }
};
</script>

<style scoped>
body {
  background-color: #000;
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
}

.form-label {
  color: #ffa500;
}

.form-control {
  background-color: #333;
  color: #fff;
  border: 1px solid #ffa500;
}

.form-control:focus {
  box-shadow: 0 0 5px #ffa500;
}

.btn-orange {
  background-color: #ffa500;
  color: #fff;
  border: none;
  transition: background-color 0.3s;
}

.btn-orange:hover {
  background-color: #ff8c00;
}

.table {
  color: #fff;
}

.table-dark {
  background-color: #333;
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: #2a2a2a;
}

.table th, .table td {
  border-color: #ffa500;
}
</style>
