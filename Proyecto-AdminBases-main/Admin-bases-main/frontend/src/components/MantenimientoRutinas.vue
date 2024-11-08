<template>
    <div class="mantenimiento-rutinas">
      <h1>Mantenimiento de Rutinas</h1>
      <form @submit.prevent="guardarRutina">
        <div>
          <label for="nombre">Nombre de la Rutina:</label>
          <input type="text" id="nombre" v-model="rutina.nombre" required />
        </div>
  
        <div>
          <label for="fecha">Fecha:</label>
          <input type="date" id="fecha" v-model="rutina.fecha" required />
        </div>
  
        <div>
          <label for="horas">Horas:</label>
          <input type="number" id="horas" v-model="rutina.horas" required />
        </div>
  
        <div>
          <label for="cliente">Cliente:</label>
          <select id="cliente" v-model="rutina.cliente">
            <option v-for="cliente in clientes" :key="cliente.cedula" :value="cliente.cedula">
              {{ cliente.nombre }} - {{ cliente.cedula }}
            </option>
          </select>
        </div>
  
        <div>
          <label for="instructor">Instructor:</label>
          <select id="instructor" v-model="rutina.instructor">
            <option v-for="instructor in instructores" :key="instructor.id" :value="instructor.id">
              {{ instructor.nombre }} - {{ instructor.id }}
            </option>
          </select>
        </div>
  
        <div>
          <label for="maquina">Máquina:</label>
          <select id="maquina" v-model="rutina.maquina">
            <option v-for="maquina in maquinas" :key="maquina.id_maquina" :value="maquina.id_maquina">
              {{ maquina.descripcion }} - {{ maquina.id_maquina }}
            </option>
          </select>
        </div>
  
        <button type="submit" class="btn btn-primary">
        GUARDAR RUTINA
        </button>
      </form>
  
      <!-- Lista de rutinas -->
      <h2>Lista de Rutinas</h2>
      <ul>
        <li v-for="rutina in rutinas" :key="rutina.id_rutina">
          {{ rutina.nombre }} - {{ rutina.fecha }} - {{ rutina.horas }} horas
          <button @click="eliminarRutina(rutina.id_rutina)">Eliminar</button>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        rutina: {
          nombre: '',
          fecha: '',
          horas: 0,
          cliente: '',
          instructor: '',
          maquina: ''
        },
        clientes: [],
        instructores: [],
        maquinas: [],
        rutinas: []
      };
    },
    created() {
      this.cargarClientes();
      this.cargarInstructores();
      this.cargarMaquinas();
      this.cargarRutinas();
    },
    methods: {
      async cargarClientes() {
    try {
      const response = await fetch('http://localhost:3000/api/clientes', {
        method: 'GET',
        credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
      });
      const data = await response.json();
      
      // Filtrar los clientes con estado 'inscrito'
      this.clientes = data.filter(cliente => cliente.estado === '"inscrito"');
      // Imprimir los estados de todos los clientes
      this.imprimirEstadosClientes(data);
    } catch (error) {

    
      console.error('Error al cargar los clientes:', error);
    }
  },
  
  
  imprimirEstadosClientes(clientes) {
  clientes.forEach(cliente => {
    console.log(`Cliente: ${cliente.nombre}, Estado: ${cliente.estado}`);
  });
},
    async cargarMaquinas() {
      const response = await fetch('http://localhost:3000/api/maquinas',{
    method: 'GET',
    credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
  });
      this.maquinas = await response.json();
    }, 
    async cargarInstructores() {
      const response = await fetch('http://localhost:3000/api/empleados',{
    method: 'GET',
    credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
  });
      this.instructores = await response.json();
    } ,

    async cargarRutinas() {
      const response = await fetch('http://localhost:3000/api/rutinas',{
    method: 'GET',
    credentials: 'include', // Asegúrate de que las cookies de sesión sean enviadas
  });
      this.rutinas = await response.json();
    }
      ,
      async guardarRutina() {try {
  const nuevaRutina = {
    nombre: this.rutina.nombre,
    fecha: this.rutina.fecha,
    horas: this.rutina.horas,
    cliente: { cedula: this.rutina.cliente },
    instructor: this.rutina.instructor,
    maquina: { id_maquina: this.rutina.maquina }
  };

  // Siempre enviamos una petición POST para crear una nueva rutina
  await fetch('http://localhost:3000/api/rutinas', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(nuevaRutina),
  });

  this.limpiarFormulario(); // Reiniciar el formulario después de guardar
  this.cargarRutinas(); // Actualizar la lista de rutinas
} catch (error) {
  console.error('Error al guardar la rutina:', error);
}}
,


async eliminarRutina(id_rutina) {
  if (confirm('¿Estás seguro de eliminar esta rutina?')) {
    try {
      await fetch(`http://localhost:3000/api/rutinas/${id_rutina}`, {
        method: 'DELETE',
        credentials: 'include', // Agregado para incluir las credenciales
      });
      this.cargarRutinas(); // Actualizar la lista de rutinas
    } catch (error) {
      console.error('Error al eliminar la rutina:', error);
    }
  }
}
,
      /*async eliminarRutina(id_rutina) {
        fetch(`/api/rutinas/${id_rutina}`, {
          method: 'DELETE'
        }).then(() => {
          this.rutinas = this.rutinas.filter(rutina => rutina.id_rutina !== id_rutina);
        });
      },*/
      limpiarFormulario() {
        this.rutina = {
          nombre: '',
          fecha: '',
          horas: 0,
          cliente: '',
          instructor: '',
          maquina: ''
        };
      }
    }
  };
  </script>
  
  <style scoped>
  .mantenimiento-rutinas {
    padding: 20px;
  }
  </style>
   
