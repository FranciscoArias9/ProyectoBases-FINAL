<template>
  <div class="container mt-4">
    <h1 class="text-center mb-4">Cursos</h1>
    <table class="table table-bordered table-striped table-hover">
      <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Descripción</th>
          <th>Horario</th>
          <th>Disponibilidad</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="curso in cursos" :key="curso.id">
          <td>{{ curso.id }}</td>
          <td>{{ curso.descripcion }}</td>
          <td>{{ curso.horario }}</td>
          <td>{{ curso.disponibilidad }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default{
  name:'ServicioCurso',
  data(){
    return{
      cursos:[],
    };
  },
  async created() {
    try{
      const response = await fetch('http://localhost:3000/api/cursos');
      const data = await response.json();
      this.cursos = data;
    }catch(error){
      console.log("no se pudo obtener los cursos")
    }
  },
  methods: {}
}
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