<template>
  <div class="inicio-container">
    <h1>Inicio</h1>
    <br>
    <div class="logo-container">
      <img src="@/assets/barra.png" alt="Logo Gimnasio" class="rebote-logo" />
    </div>
    <ul>
      <li>Gimnasio Fit Studio</li>
      <li>Dirección: Calle 24, avenida 14, San José, Costa Rica</li>
      <li>Horario: Lunes a Viernes, 6 AM - 9 PM</li>
    </ul>
  </div>
</template>

<script>
export default {
  name: 'AppInicio',
  props: {}
}
</script>

<style scoped>
.inicio-container {
  text-align: center;
  margin-top: 50px;
}

.logo-container {
  display: inline-block;
  animation: rebote 2s ease infinite;
}

.rebote-logo {
  width: 150px;
  height: 150px;
}

@keyframes rebote {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-30px);
  }
  60% {
    transform: translateY(-15px);
  }
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  font-size: 1.2em;
  margin-top: 10px;
}
</style>
