<template>
  <div class="container">
    <h1 class="my-4 text-center">Clientes</h1>
    <table class="table table-striped table-bordered">
      <thead class="thead-dark">
        <tr>
          <th>Cédula</th>
          <th>Nombre</th>
          <th>Primer Apellido</th>
          <th>Segundo Apellido</th>
          <th>Dirección</th>
          <th>Email</th>
          <th>Celular</th>
          <th>Teléfono de Habitación</th>
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
          <td>{{ cliente.celular }}</td>
          <td>{{ cliente.tel_habitacion }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ServicioCliente',
  data() {
    return {
      clientes: [],
    };
  },
  async created() {
  try {
    const response = await fetch('http://localhost:3000/api/clientes');
    if (!response.ok) {
      throw new Error('Failed to fetch');
    }
    const data = await response.json();
    console.log("Clientes recibidos:", data);  // Agrega esta línea
    this.clientes = data;
  } catch (error) {
    console.error("No se pudo obtener la lista de clientes:", error);
  }
},
};
</script>

<style scoped>
table {
  border-collapse: collapse;
}

th, td {
  border: 1px solid black;
  padding: 8px;
  text-align: left;
}

</style>
