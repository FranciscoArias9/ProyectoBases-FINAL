<template>
  <section class="inicio-section">
    <div class="inicio-container">
      <h1>Inicio</h1>
      <br />
      <div class="logo-container">
        <img src="@/assets/barra.png" alt="Logo Gimnasio" class="rebote-logo" />
      </div>
      <ul>
        <li>Gimnasio Fit Studio</li>
        <li>Dirección: Calle 24, avenida 14, San José, Costa Rica</li>
        <li>Horario: Lunes a Viernes, 6 AM - 9 PM</li>
      </ul>
    </div>
  </section>
</template>

<script>
export default {
  name: 'AppInicio',
};
</script>

<style scoped>
.inicio-section {
  background-image: url('@/assets/fondoIni.jpg');
  background-size: cover;
  background-position: center;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.inicio-container {
  text-align: center;
  background-color: rgba(0, 0, 0, 0.7); 
  padding: 30px;
  border-radius: 1rem;
  color: #ff8c00;
  max-width: 600px;
  width: 100%;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.5);
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
  margin-top: 20px;
}

li {
  font-size: 1.2em;
  color: #fff; 
  margin-top: 10px;
}
</style>
