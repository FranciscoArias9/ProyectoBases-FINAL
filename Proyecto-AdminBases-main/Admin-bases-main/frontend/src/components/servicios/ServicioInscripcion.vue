<template>
  <section class="vh-50">
    <div class="container py-4 h-100">
      <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 col-md-8 col-lg-6 col-xl-5">
          <div class="card bg-dark text-white" style="border-radius: 1rem;">
            <div class="card-body p-4 text-center">

              <h2 class="fw-bold mb-2 text-uppercase">Inscripción</h2>
              <p class="text-white-50 mb-5">Por favor, complete el formulario para inscribirse en un curso.</p>

              <form @submit.prevent="submitForm">
                <div class="form-outline form-white mb-2">
                  <input type="text" id="nombre" v-model="nombre" class="form-control form-control-lg" />
                  <label class="form-label" for="nombre">Nombre</label>
                </div>

                <div class="form-outline form-white mb-2">
                  <input type="email" id="email" v-model="email" class="form-control form-control-lg" />
                  <label class="form-label" for="email">Email</label>
                </div>

                <div class="form-outline form-white mb-2">
                  <select id="curso" v-model="curso" class="form-select form-select-lg">
                    <option disabled value="">Seleccione un curso</option>
                    <option v-for="curso in cursos" :key="curso.id" :value="curso.id">{{ curso.nombre }}</option>
                  </select>
                  <label class="form-label" for="curso">Curso</label>
                </div>

                <div class="form-outline form-white mb-2">
                  <input type="text" id="horario" v-model="horario" class="form-control form-control-lg" />
                  <label class="form-label" for="horario">Horario</label>
                </div>

                <button class="btn btn-outline-light btn-lg px-5" type="submit">Inscribirse</button>
              </form>

              <p class="text-white-50 mt-3">{{ mensaje }}</p>

            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: 'ServicioInscripcion',
  data() {
    return {
      nombre: '',
      email: '',
      curso: '',
      horario: '',
      cursos: [], // Cargar base de datos
      mensaje: ''
    };
  },
  async created() {
    try{
      const response = await fetch('http://localhost:3000/api/cursos');
      const data = await response.json();
      this.cursos = data;
    }catch(error){
      this.cursos=null;
    }
  },
  methods: {
    async submitForm() {
      try {
        const usuarioExiste = await fetch(`http://localhost:3000/api/usuarios/${this.email}`);
        const usuario = await usuarioExiste.json();
        if (usuario.existe) {
          this.mensaje = 'El usuario ya está registrado.';
          return;
        }

        // disponibilidad del curso
        const cursoDisponible = await fetch(`http://localhost:3000/api/cursos/${this.curso}/disponibilidad?horario=${this.horario}`);
        const disponibilidad = await cursoDisponible.json();
        if (!disponibilidad.disponible) {
          this.mensaje = 'El curso no está disponible en el horario seleccionado.';
          return;
        }

        //  registrar al usuario
        const response = await fetch('http://localhost:3000/api/inscripcion', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            nombre: this.nombre,
            email: this.email,
            curso: this.curso,
            horario: this.horario
          })
        });

        const result = await response.json();
        if (result.success) {
          this.mensaje = 'Inscripción exitosa.';
        } else {
          this.mensaje = 'Error en la inscripción.';
        }
      } catch (error) {
        console.error('Error:', error);
        this.mensaje = 'Ocurrió un error al procesar la inscripción.';
      }
    }
  }
}
</script>

<style scoped>
</style>
