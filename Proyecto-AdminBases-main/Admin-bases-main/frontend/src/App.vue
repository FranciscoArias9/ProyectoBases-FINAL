<template>
  <div>
    <NavBar/>
    <!--<img alt="Vue logo" src="./assets/logo.png">-->
    <router-view></router-view>
    <!--
    <h1>Conexión con el Servidor Express</h1>
    <button @click="enviarDatos">Enviar Datos</button>
    <p>{{ mensaje }}</p>
    -->
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue';

export default {
  name: 'App',
  components: {
    NavBar,
  },
  data(){
    return{
      mensaje:'',
    };
  },
  methods: {
    enviarDatos() {
      fetch('http://localhost:3000/api/datos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ nombre: 'Juan', edad: 30 }),
      })
        .then(response => response.json())
        .then(data => {  this.mensaje= data.message;})
        .catch(error => {console.error('Error:', error); });
    },
  }
}
</script>

<style>
#app {
  background: #6a11cb;
  background: -webkit-linear-gradient(to right, rgba(106, 17, 203, 1), rgba(37, 117, 252, 1));
  background: linear-gradient(to right, rgba(106, 17, 203, 1), rgba(37, 117, 252, 1));
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>
