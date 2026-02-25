import { redesApiUrl } from '@/environment/apiUrls';

const parseError = async (response) => {
    const errorData = await response.json().catch(() => ({}));
    const text = errorData.message || await response.text();
    return text || 'Error en la petición';
};

const normalizarEstado = (estado) => {
    if (estado === 'FalloAuth') {
        return 'Fallo de Auth';
    }

    if (estado === 'SinSeñal') {
        return 'Sin Señal';
    }

    return estado;
};

export const obtenerEstadoActual = async () => {
    const response = await fetch(`${redesApiUrl}/registros-redes`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        const text = await parseError(response);
        console.error('Error al obtener el estado actual:', response.status, text);
        throw new Error(text || 'Error al obtener el estado actual');
    }

    const data = await response.json();

    if (!Array.isArray(data)) {
        return [];
    }

    return data.map((item) => ({
        id: item.id,
        ssid: item.ssid ?? item.nombreRed ?? '',
        estado: normalizarEstado(item.estado),
        fechaReporte: item.fechaReporte ?? item.fechaHora ?? null,
    }));
};

export const obtenerRedesConfiguradas = async () => {
    const response = await fetch(`${redesApiUrl}/configuracion-redes`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        const text = await parseError(response);
        console.error('Error al obtener redes configuradas:', response.status, text);
        throw new Error(text || 'Error al obtener redes configuradas');
    }

    return await response.json();
};

export const registrarRed = async (red) => {
    const response = await fetch(`${redesApiUrl}/configuracion-redes`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(red),
    });

    if (!response.ok) {
        const text = await parseError(response);
        console.error('Error al registrar red:', response.status, text);
        throw new Error(text || 'Error al registrar red');
    }

    return await response.text();
};

export const eliminarRed = async (id) => {
    const response = await fetch(`${redesApiUrl}/configuracion-redes/${id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    });

    if (!response.ok) {
        const text = await parseError(response);
        console.error('Error al eliminar red:', response.status, text);
        throw new Error(text || 'Error al eliminar red');
    }

    return await response.text();
};
