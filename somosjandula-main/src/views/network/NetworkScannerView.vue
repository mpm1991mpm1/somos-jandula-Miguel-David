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
      <div class="header-row">
        <h1>📡 Monitorización de Redes</h1>
        <div class="controls">
          <button @click="cargarDatos" class="btn-refresh" :disabled="cargando">🔄 Actualizar ahora</button>
          <p>Última actualización global: {{ ultimaActualizacion }}</p>
        </div>
      </div>

      <div v-if="historial.length === 0" class="empty-state">
        No hay registros aún.
      </div>

      <div v-else class="cards-grid">
        <div v-for="registro in historial" :key="registro.id" class="network-card" :class="getEstadoClass(registro.estado)">
          <div class="card-header">
            <div class="ssid">{{ registro.ssid }}</div>
            <div class="estado">{{ registro.estado }}</div>
          </div>
          <div class="card-body">
            <div class="label">Última actualización</div>
            <div class="value">{{ formatearFecha(registro.fechaReporte) }}</div>
          </div>
        </div>
      </div>
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
  if (estado === 'Conectado') return 'card-ok';
  if (estado === 'Fallo de Auth') return 'card-warn';
  return 'card-error';
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
  min-height: 100vh;
  background: #f6f7fb;
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
  margin: 0;
}

.container {
  padding: 20px 24px 30px;
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
}

.header-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
  margin-bottom: 20px;
}

.controls {
  display: flex;
  align-items: center;
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

.empty-state {
  padding: 24px;
  background: #fff;
  border-radius: 14px;
  border: 1px dashed #d7dbe7;
  text-align: center;
  color: #6b7280;
  font-weight: 600;
}

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.network-card {
  border-radius: 16px;
  padding: 18px 20px;
  border: 1px solid transparent;
  display: flex;
  flex-direction: column;
  gap: 12px;
  min-height: 140px;
  box-shadow: 0 8px 22px rgba(16, 24, 40, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.ssid {
  font-size: 18px;
  font-weight: 700;
  color: #111827;
}

.estado {
  font-weight: 700;
  padding: 6px 10px;
  border-radius: 999px;
  font-size: 12px;
  letter-spacing: 0.2px;
}

.card-body .label {
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.6px;
  color: #6b7280;
  margin-bottom: 4px;
}

.card-body .value {
  font-size: 14px;
  font-weight: 600;
  color: #1f2937;
}

.card-ok {
  background: linear-gradient(135deg, #ecfdf5, #d1fae5);
  border-color: #a7f3d0;
}

.card-ok .estado {
  background: #10b981;
  color: #fff;
}

.card-warn {
  background: linear-gradient(135deg, #fff7ed, #ffedd5);
  border-color: #fed7aa;
}

.card-warn .estado {
  background: #f59e0b;
  color: #fff;
}

.card-error {
  background: linear-gradient(135deg, #fef2f2, #fee2e2);
  border-color: #fecaca;
}

.card-error .estado {
  background: #ef4444;
  color: #fff;
}
</style>

