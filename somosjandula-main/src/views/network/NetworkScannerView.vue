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
      <h1>📡 Monitorización de Redes</h1>

      <div class="controls">
        <button @click="cargarDatos" class="btn-refresh" :disabled="cargando">🔄 Actualizar ahora</button>
        <p>Última actualización: {{ ultimaActualizacion }}</p>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>Red (SSID)</th>
            <th>Estado</th>
            <th>Fecha Reporte</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="registro in historial" :key="registro.id">
            <td><strong>{{ registro.ssid }}</strong></td>

            <td :class="getEstadoClass(registro.estado)">
              {{ registro.estado }}
            </td>

            <td>{{ formatearFecha(registro.fechaReporte) }}</td>
          </tr>
          <tr v-if="historial.length === 0">
            <td colspan="3">No hay registros aún.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { obtenerEstadoActual } from '@/services/network';

const router = useRouter();
const activeTab = ref('monitor');

const historial = ref([] as Array<{ id?: number; ssid: string; estado: string; fechaReporte?: string }>);
const ultimaActualizacion = ref('-');
const cargando = ref(false);

const goTab = (value: 'monitor' | 'admin') => {
  if (value === 'admin') {
    router.push('/network/admin');
  }
  else {
    router.push('/network/scanner');
  }
};

const cargarDatos = async () => {
  try {
    cargando.value = true;
    const data = await obtenerEstadoActual();
    historial.value = data || [];
    ultimaActualizacion.value = new Date().toLocaleTimeString();
  }
  catch (error) {
    console.error('Error cargando historial:', error);
    alert('Error conectando con el Backend de redes.');
  }
  finally {
    cargando.value = false;
  }
};

const getEstadoClass = (estado: string) => {
  if (estado === 'Conectado') return 'status-ok';
  if (estado === 'Fallo de Auth') return 'status-warn';
  return 'status-error';
};

const formatearFecha = (fecha?: string) => {
  if (!fecha) return '-';
  return new Date(fecha).toLocaleString();
};

let refreshInterval: number | undefined;
onMounted(async () => {
  await cargarDatos();
  refreshInterval = window.setInterval(cargarDatos, 20000);
});

onBeforeUnmount(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval);
  }
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

h1 {
  color: #2c3e50;
  text-align: center;
}

.container {
  padding: 20px;
  max-width: 900px;
  margin: 0 auto;
}

.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 12px;
  flex-wrap: wrap;
}

.btn-refresh {
  background-color: #42b983;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
}

.btn-refresh:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.data-table th,
.data-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.data-table th {
  background-color: #f2f2f2;
}

.status-ok {
  color: green;
  font-weight: bold;
  background-color: #e6fffa;
}

.status-warn {
  color: orange;
  font-weight: bold;
  background-color: #fffaf0;
}

.status-error {
  color: red;
  font-weight: bold;
  background-color: #fff5f5;
}
</style>

