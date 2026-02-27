<template>
  <div class="network-page">
    <div class="tabs">
      <button class="tab-button" :class="{ active: activeTab === 'monitor' }" @click="goTab('monitor')">
        Monitorización de redes
      </button>
      <button class="tab-button" :class="{ active: activeTab === 'admin' }" @click="goTab('admin')">
        Administración de redes
      </button>
    </div>

    <div class="container">
      <div class="header-row">
        <h1>📡 Monitorización de Redes</h1>
        <div class="controls">
          <button @click="cargarDatos" class="btn-refresh" :disabled="cargando">🔄 Actualizar ahora</button>
          <button @click="cambiarTiempoMonitorizacion" class="btn-interval" :disabled="cargando">
            ⏱ Tiempo monitorización ({{ refreshLabel }})
          </button>
          <p>Última actualización global: {{ ultimaActualizacion }}</p>
        </div>
      </div>

      <div v-if="showIntervalSettings" class="interval-settings">
        <div class="interval-settings-row">
          <label class="interval-label" for="interval-minutes">Actualizar cada (minutos)</label>
          <input
            id="interval-minutes"
            class="interval-input"
            type="number"
            min="1"
            step="1"
            v-model.number="pendingRefreshMinutes"
            :disabled="cargando"
          />

          <button class="btn-save" type="button" @click="guardarTiempoMonitorizacion" :disabled="cargando">
            Guardar
          </button>
          <button class="btn-cancel" type="button" @click="cancelarTiempoMonitorizacion" :disabled="cargando">
            Cancelar
          </button>
        </div>

        <p v-if="intervalError" class="interval-error">{{ intervalError }}</p>
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
import { computed, onBeforeUnmount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { guardarConfiguracionMonitorizacion, obtenerConfiguracionMonitorizacion, obtenerEstadoActual } from '@/services/network';

const router = useRouter();
const activeTab = ref('monitor');

const historial = ref([] as Array<{ id?: number; ssid: string; estado: string; fechaReporte?: string }>);
const ultimaActualizacion = ref('-');
const cargando = ref(false);

const DEFAULT_REFRESH_SECONDS = 20;

const refreshMinutes = ref<number | null>(null);
const refreshSeconds = computed(() => (refreshMinutes.value ? refreshMinutes.value * 60 : DEFAULT_REFRESH_SECONDS));
const refreshLabel = computed(() => (refreshMinutes.value ? `${refreshMinutes.value}min` : `${DEFAULT_REFRESH_SECONDS}s`));

const showIntervalSettings = ref(false);
const pendingRefreshMinutes = ref<number>(1);
const intervalError = ref('');

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

const aplicarIntervalo = (seconds: number) => {
  const nextSeconds = Number.isFinite(seconds) ? Math.floor(seconds) : DEFAULT_REFRESH_SECONDS;
  const clampedSeconds = nextSeconds > 0 ? nextSeconds : DEFAULT_REFRESH_SECONDS;
  if (refreshInterval) {
    clearInterval(refreshInterval);
  }

  refreshInterval = window.setInterval(cargarDatos, clampedSeconds * 1000);
};

const cambiarTiempoMonitorizacion = () => {
  intervalError.value = '';
  pendingRefreshMinutes.value = refreshMinutes.value ?? 1;
  showIntervalSettings.value = true;
};

const cancelarTiempoMonitorizacion = () => {
  intervalError.value = '';
  showIntervalSettings.value = false;
  pendingRefreshMinutes.value = refreshMinutes.value ?? 1;
};

const guardarTiempoMonitorizacion = async () => {
  intervalError.value = '';

  const minutes = Number(pendingRefreshMinutes.value);
  if (!Number.isFinite(minutes) || Math.floor(minutes) !== minutes || minutes < 1) {
    intervalError.value = 'Introduce un número entero válido (mínimo 1 minuto).';
    return;
  }

  try {
    cargando.value = true;
    await guardarConfiguracionMonitorizacion(minutes);
    refreshMinutes.value = minutes;
    aplicarIntervalo(refreshSeconds.value);
    showIntervalSettings.value = false;
    await cargarDatos();
  }
  catch (error) {
    console.error('Error guardando intervalo:', error);
    intervalError.value = 'No se pudo guardar el intervalo en el servidor.';
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

  try {
    const config = await obtenerConfiguracionMonitorizacion();
    const minutes = config?.refreshMinutes;
    if (typeof minutes === 'number' && Number.isFinite(minutes) && Math.floor(minutes) === minutes && minutes >= 1) {
      refreshMinutes.value = minutes;
    }
  }
  catch (error) {
    console.warn('No se pudo cargar la configuración de monitorización; usando 20s por defecto.', error);
  }

  aplicarIntervalo(refreshSeconds.value);
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

.btn-interval {
  background-color: #2c3e50;
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

.btn-interval:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.interval-settings {
  margin-bottom: 16px;
  padding: 14px 16px;
  border-radius: 14px;
  border: 1px solid #d7dbe7;
  background: #fff;
}

.interval-settings-row {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.interval-label {
  font-weight: 700;
  color: #2c3e50;
}

.interval-input {
  width: 120px;
  padding: 10px 12px;
  border-radius: 10px;
  border: 1px solid #d0d0d0;
}

.btn-save {
  background-color: #42b983;
  color: #fff;
  border: none;
  padding: 10px 18px;
  cursor: pointer;
  border-radius: 10px;
  font-weight: 700;
}

.btn-cancel {
  background-color: #f7f7f7;
  color: #2c3e50;
  border: 1px solid #d0d0d0;
  padding: 10px 18px;
  cursor: pointer;
  border-radius: 10px;
  font-weight: 700;
}

.interval-error {
  margin: 10px 0 0;
  color: #ef4444;
  font-weight: 700;
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
  color: #f9fafb;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.ssid {
  font-size: 24px;
  font-weight: 800;
  color: inherit;
}

.estado {
  font-weight: 800;
  padding: 8px 12px;
  border-radius: 999px;
  font-size: 16px;
  letter-spacing: 0.2px;
}

.card-body .label {
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.6px;
  color: rgba(249, 250, 251, 0.78);
  margin-bottom: 4px;
}

.card-body .value {
  font-size: 18px;
  font-weight: 700;
  color: inherit;
}

.card-ok {
  background-color: #064e3b;
  border-color: #10b981;
}

.card-ok .estado {
  background: #10b981;
  color: #fff;
}

.card-warn {
  background-color: #c2410c;
  border-color: #f59e0b;
}

.card-warn .estado {
  background: #f59e0b;
  color: #fff;
}

.card-error {
  background-color: #991b1b;
  border-color: #ef4444;
}

.card-error .estado {
  background: #ef4444;
  color: #fff;
}
</style>

