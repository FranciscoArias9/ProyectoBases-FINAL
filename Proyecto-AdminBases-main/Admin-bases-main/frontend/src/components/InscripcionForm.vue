<template>
  <section class="inscripcion-section">
    <div class="inscripcion-container">
      <h2>Inscripción de Clientes</h2>
      
      <!-- Cuadro de búsqueda -->
      <input 
        v-model="busqueda" 
        @input="buscarCliente" 
        placeholder="Buscar cliente por cédula" 
        class="search-input"
      />
      
      <!-- Lista de clientes -->
      <ul class="clientes-list">
        <li v-for="cliente in clientesFiltrados" :key="cliente.cedula" class="cliente-item">
          <span>{{ cliente.cedula }} - {{ cliente.nombre }}</span>
          <span class="estado">Estado: {{ cliente.estado }}</span>
          <button @click="cambiarEstado(cliente, 'inscrito')" class="btn btn-inscribir">Inscribir</button>
          <button @click="cambiarEstado(cliente, 'desuscrito')" class="btn btn-desuscribir">Desuscribir</button>
        </li>
      </ul>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      clientes: [],
      busqueda: '',
    };
  },
  computed: {
    clientesFiltrados() {
      return this.clientes.filter(cliente =>
        cliente.cedula.toString().includes(this.busqueda)
      );
    },
  },
  methods: {
    async obtenerClientes() {
      const response = await fetch('http://localhost:3000/api/clientes', {
        method: 'GET',
        credentials: 'include',
      });
      this.clientes = await response.json();
    },
    async cambiarEstado(cliente, nuevoEstado) {
      const response = await fetch(`http://localhost:3000/api/clientes/${cliente.cedula}/estado`, {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ estado: nuevoEstado }),
      });
      if (response.ok) {
        cliente.estado = nuevoEstado;
      }
    },
  },
  mounted() {
    this.obtenerClientes();
  },
};
</script>

<style scoped>
.inscripcion-section {
  
  padding: 50px 20px;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* Contenedor principal */
.inscripcion-container {
  background-color: rgba(0, 0, 0, 0.7);
  padding: 30px;
  border-radius: 10px;
  width: 100%;
  max-width: 600px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.5);
  color: #ff8c00; 
  text-align: center;
}


h2 {
  margin-bottom: 20px;
  color: #ff8c00;
}


.search-input {
  width: 100%;
  padding: 10px;
  border-radius: 5px;
  border: 2px solid #ff8c00;
  margin-bottom: 20px;
  outline: none;
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
}


.clientes-list {
  list-style-type: none;
  padding: 0;
  margin: 0;
}


.cliente-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.8);
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 5px;
  color: #fff;
}


.estado {
  font-style: italic;
}


.btn {
  padding: 8px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-left: 10px;
}

.btn-inscribir {
  background-color: #28a745;
  color: #fff;
}

.btn-inscribir:hover {
  background-color: #218838;
}

.btn-desuscribir {
  background-color: #dc3545;
  color: #fff;
}

.btn-desuscribir:hover {
  background-color: #c82333;
}
</style>
