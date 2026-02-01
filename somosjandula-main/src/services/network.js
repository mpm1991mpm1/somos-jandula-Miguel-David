import { redesApiUrl } from '@/environment/apiUrls';

const parseError = async (response) => {
    const errorData = await response.json().catch(() => ({}));
    const text = errorData.message || await response.text();
    return text || 'Error en la petición';
};

export const obtenerEstadoActual = async () => {
    const response = await fetch(`${redesApiUrl}/estado-actual`, {
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

    return await response.json();
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
