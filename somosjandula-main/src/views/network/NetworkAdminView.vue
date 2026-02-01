<template>
  <div class="network-page">
    <div class="tabs">
      <button class="tab-button" :class="{ active: activeTab === 'monitor' }" @click="goTab('monitor')">
        Monitorización de redes
      </button>
      <button class="tab-button" :class="{ active: activeTab === 'admin' }" @click="goTab('admin')">
        Añadir redes
      </button>
    </div>

    <div class="container">
      <h1>⚙️ Administración de Redes</h1>

      <div class="card">
        <h2>Añadir Nueva Red Objetivo</h2>
        <form @submit.prevent="guardarRed" class="form-grid">
          <div class="form-group">
            <label>Nombre de Red (SSID):</label>
            <input v-model="nuevaRed.nombre" placeholder="Ej: MiCasa_WiFi" required />
          </div>

          <div class="form-group">
            <label>Usuario:</label>
            <input v-model="nuevaRed.usuario" placeholder="Usuario de la red" required />
          </div>

          <div class="form-group">
            <label>Contraseña:</label>
            <input v-model="nuevaRed.contrasena" type="text" placeholder="Contraseña de la red" required />
          </div>

          <div class="form-group">
            <label>Seguridad:</label>
            <select v-model="nuevaRed.seguridad">
              <option value="WPA2">WPA2</option>
              <option value="WEP">WEP</option>
              <option value="OPEN">Abierta (Sin clave)</option>
            </select>
          </div>

          <button type="submit" class="btn-save">Guardar Red</button>
        </form>
      </div>

      <div class="list-section">
        <h2>Redes Monitorizadas Actualmente</h2>
        <ul>
          <li v-for="red in redes" :key="red.id">
            <strong>{{ red.ssid }}</strong> <small>({{ red.seguridad }})</small>
            <button class="btn-delete" @click="eliminarRedItem(red.id)">Eliminar</button>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { eliminarRed, obtenerRedesConfiguradas, registrarRed } from '@/services/network';

const router = useRouter();
const activeTab = ref('admin');

const redes = ref([]);
const nuevaRed = ref({ nombre: '', usuario: '', contrasena: '', seguridad: 'WPA2' });

const goTab = (value) => {
  if (value === 'monitor') {
    router.push('/network/scanner');
  }
  else {
    router.push('/network/admin');
  }
};

const cargarRedes = async () => {
  try {
    redes.value = await obtenerRedesConfiguradas();
  }
  catch (error) {
    console.error('Error cargando redes:', error);
  }
};

const guardarRed = async () => {
  try {
    await registrarRed(nuevaRed.value);
    alert('Red guardada correctamente');
    nuevaRed.value = { nombre: '', usuario: '', contrasena: '', seguridad: 'WPA2' };
    await cargarRedes();
  }
  catch (error) {
    console.error(error);
    alert('Error al guardar. Revisa la consola.');
  }
};

const eliminarRedItem = async (id) => {
  if (!confirm('¿Seguro que deseas eliminar esta red?')) return;
  try {
    await eliminarRed(id);
    await cargarRedes();
  }
  catch (error) {
    console.error(error);
    alert('Error al eliminar. Revisa la consola.');
  }
};

onMounted(() => {
  cargarRedes();
});
</script>

<style scoped>
.network-page {
  padding: 10px 0 20px 0;
}

.tabs {
  max-width: 900px;
  margin: 0 auto 20px auto;
  display: flex;
  gap: 12px;
  padding: 0 20px;
}

.tab-button {
  flex: 1;
  padding: 10px 14px;
  border-radius: 10px;
  border: 1px solid #d0d0d0;
  background: #f7f7f7;
  cursor: pointer;
  font-weight: 600;
}

.tab-button.active {
  background: #2c3e50;
  color: #fff;
  border-color: #2c3e50;
}

h1, h2 {
  color: #2c3e50;
}

.container {
  padding: 20px;
  max-width: 700px;
  margin: 0 auto;
}

.card {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input, .form-group select {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

.btn-save {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 10px 20px;
  width: 100%;
  cursor: pointer;
  font-size: 16px;
  border-radius: 5px;
}

.btn-save:hover {
  background-color: #2980b9;
}

.list-section {
  margin-top: 30px;
}

.btn-delete {
  margin-left: 10px;
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 6px 10px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-delete:hover {
  background-color: #c0392b;
}
</style>
