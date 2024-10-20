<template>
  <h1>cursos</h1>
  <table>
    <thead>
    <tr>
    <td>ID</td>
    <td>Descripcion</td>
    <td>Horario</td>
    <td>Disponibilidad</td>
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