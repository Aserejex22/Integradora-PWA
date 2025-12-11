// HTTP Gateway - Métodos para comunicación con el servidor
const HEADERS = {
    "Content-Type": "application/json",
    "Accept": "application/json"
}

const handleResponse = async (response) => {
    // Si la respuesta no es exitosa (4xx, 5xx)
    if (!response.ok) {
        const contentType = response.headers.get("content-type");
        let errorMessage = `HTTP Error ${response.status}`;

        try {
            if (contentType && contentType.includes("application/json")) {
                const errorData = await response.json();
                errorMessage = errorData.message || errorData.error || errorMessage;
            } else {
                errorMessage = await response.text() || errorMessage;
            }
        } catch (e) {
            // Si no se puede parsear el error, usar el mensaje por defecto
        }

        return { error: errorMessage, status: response.status };
    }

    // Respuesta exitosa
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        return await response.json();
    }
    return await response.text();
}

const doGet = async url => {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: HEADERS
        });
        return await handleResponse(response);
    } catch (e) {
        console.error('Error en GET:', e);
        return { error: e.message };
    }
}

const doPost = async (url, body) => {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: HEADERS,
            body: JSON.stringify(body)
        });
        return await handleResponse(response);
    } catch (e) {
        console.error('Error en POST:', e);
        return { error: e.message };
    }
}

const doPut = async (url, body) => {
    try {
        const response = await fetch(url, {
            method: 'PUT',
            headers: HEADERS,
            body: JSON.stringify(body)
        });
        return await handleResponse(response);
    } catch (e) {
        console.error('Error en PUT:', e);
        return { error: e.message };
    }
}

const doDelete = async (url) => {
    try {
        const response = await fetch(url, {
            method: 'DELETE',
            headers: HEADERS
        });
        return await handleResponse(response);
    } catch (e) {
        console.error('Error en DELETE:', e);
        return { error: e.message };
    }
}

const doPatch = async (url, body) => {
    try {
        const response = await fetch(url, {
            method: 'PATCH',
            headers: HEADERS,
            body: JSON.stringify(body)
        });
        return await handleResponse(response);
    } catch (e) {
        console.error('Error en PATCH:', e);
        return { error: e.message };
    }
}
