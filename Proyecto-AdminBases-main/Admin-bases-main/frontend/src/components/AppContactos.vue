<template>
  <section class="container py-5">
    <div class="card mx-auto text-center bg-light shadow" style="max-width: 500px; border-radius: 1rem;">
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
  name: 'AppContacto'
}
</script>

<style scoped>
h2 {
  color: #333;
}
.card {
  border: none;
}
.card-title {
  color: #333;
}
</style>
