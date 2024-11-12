<template>
  <section class="contacto-section">
    <div class="card mx-auto text-center shadow contacto-card">
      <div class="card-body">
        <h2 class="card-title fw-bold text-uppercase mb-4">Contáctenos</h2>
        <ul class="list-unstyled">
          <li class="mb-3">
            <i class="fas fa-phone-alt me-2"></i>Teléfono: +506 88888888
          </li>
          <li>
            <i class="fas fa-envelope me-2"></i>Correo electrónico: gimnasio@una.cr
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'AppContacto',
};
</script>

<style scoped>
/* Imagen de fondo para toda la sección */
.contacto-section {
  background-image: url('@/assets/contactos.jpg'); /* Aquí puedes reemplazar con tu propia URL de imagen */
  background-size: cover;
  background-position: center;
  height: 100vh; /* Para que ocupe toda la pantalla */
  display: flex;
  justify-content: center;
  align-items: center;
}

/* Estilos del cuadro de contacto */
.contacto-card {
  background-color: rgba(0, 0, 0, 0.7); /* Fondo semi-transparente */
  color: #ff8c00; /* Texto anaranjado */
  border-radius: 1rem;
  padding: 2rem;
  max-width: 500px;
  width: 100%;
}

/* Estilos para el título */
.card-title {
  color: #ff8c00; /* Color anaranjado */
}

/* Estilos para la lista de contacto */
ul {
  color: white;
}

ul li i {
  color: #ff8c00; /* Iconos en anaranjado */
}
</style>
