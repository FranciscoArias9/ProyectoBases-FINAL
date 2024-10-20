<template>
  <h1>Clientes</h1>
  <table>
    <thead>
      <tr>
        <td>Cédula</td>
        <td>Nombre</td>
        <td>Primer Apellido</td>
        <td>Segundo Apellido</td>
        <td>Dirección</td>
        <td>Email</td>
        <td>Celular</td>
        <td>Teléfono de Habitación</td>
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
