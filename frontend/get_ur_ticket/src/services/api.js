import axios from 'axios';

const GATEWAY_URL = 'http://localhost:8888';
const EVENT_API = `${GATEWAY_URL}/event-service/api`;
const USER_API = `${GATEWAY_URL}/user-service/api`;
const RESERVATION_API = `${GATEWAY_URL}/reservation-service/api`;

// Créer une instance axios avec configuration anti-cache
const api = axios.create({
    timeout: 10000,
    headers: {
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache',
        'Expires': '0',
    }
});

// Intercepteur pour ajouter le token JWT
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        console.log('🔑 Token:', token ? 'Présent' : 'Absent');
        console.log('📡 Request URL:', config.url);
        
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        
        // Force la désactivation du cache pour chaque requête
        config.headers['Cache-Control'] = 'no-cache, no-store, must-revalidate';
        config.headers['Pragma'] = 'no-cache';
        config.headers['Expires'] = '0';
        
        return config;
    },
    (error) => {
        console.error('❌ Request error:', error);
        return Promise.reject(error);
    }
);

// Intercepteur de réponse
api.interceptors.response.use(
    (response) => {
        console.log('✅ Response:', response.config.url, response.status);
        return response;
    },
    (error) => {
        console.error('❌ Response error:', error);
        
        if (error.response) {
            console.error('📊 Error status:', error.response.status);
            console.error('📋 Error data:', error.response.data);
        } else if (error.request) {
            console.error('📡 No response received:', error.request);
        }
        
        if (error.response?.status === 401) {
            console.warn('🚫 Unauthorized - redirecting to login');
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
        
        return Promise.reject(error);
    }
);

// ========== USER SERVICE ==========
export const login = async (email, password) => {
    const response = await axios.post(`${USER_API}/users/login`, { email, password });
    if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
};

export const register = async (userData) => {
    const response = await axios.post(`${USER_API}/users/register`, userData);
    if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(response.data.user));
    }
    return response.data;
};

export const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
};

export const getCurrentUser = () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
};

// ========== EVENT SERVICE - PUBLIC ==========
export const getAllEvents = async () => {
    const response = await api.get(`${EVENT_API}/events`);
    return response.data;
};

export const getEventById = async (id) => {
    const response = await api.get(`${EVENT_API}/events/${id}`);
    return response.data;
};

export const getEventsByCategory = async (categoryId) => {
    const response = await api.get(`${EVENT_API}/events/category/${categoryId}`);
    return response.data;
};

export const getAllCategories = async () => {
    const response = await api.get(`${EVENT_API}/events/categories`);
    return response.data;
};
export const getUserById = async (id) => {
    const response = await api.get(`${USER_API}/users/${id}`);
    return response.data;
};


// ========== EVENT SERVICE - ORGANIZER ==========
export const createEvent = async (eventData) => {
    const response = await api.post(`${EVENT_API}/events/organizer/create`, eventData);
    return response.data;
};

export const getMyEvents = async (organizerId) => {
    console.log('📞 Calling getMyEvents for organizer:', organizerId);
    // Force un timestamp unique pour éviter le cache
    const timestamp = new Date().getTime();
    const response = await api.get(`${EVENT_API}/events/organizer/${organizerId}?t=${timestamp}`);
    console.log('📦 Events received:', response.data);
    return response.data;
};

export const updateEvent = async (id, eventData) => {
    const response = await api.put(`${EVENT_API}/events/organizer/${id}`, eventData);
    return response.data;
};

export const deleteEvent = async (id) => {
    const response = await api.delete(`${EVENT_API}/events/organizer/${id}`);
    return response.data;
};

// ========== EVENT SERVICE - ADMIN ==========
export const getPendingEvents = async () => {
    const response = await api.get(`${EVENT_API}/events/admin/pending`);
    return response.data;
};

export const getAllEventsAdmin = async () => {
    const response = await api.get(`${EVENT_API}/events/admin/all`);
    return response.data;
};

export const approveEvent = async (id) => {
    const response = await api.put(`${EVENT_API}/events/admin/${id}/approve`);
    return response.data;
};

export const rejectEvent = async (id) => {
    const response = await api.put(`${EVENT_API}/events/admin/${id}/reject`);
    return response.data;
};

// ========== RESERVATION SERVICE ==========
export const createReservation = async (reservationData) => {
    const response = await api.post(`${RESERVATION_API}/reservations`, reservationData);
    return response.data;
};

export const getMyReservations = async (userId) => {
    const response = await api.get(`${RESERVATION_API}/reservations/user/${userId}`);
    return response.data;
};

export const getReservationDetails = async (id) => {
    const response = await api.get(`${RESERVATION_API}/reservations/${id}/details`);
    return response.data;
};

export const cancelReservation = async (id) => {
    const response = await api.put(`${RESERVATION_API}/reservations/${id}/annuler`);
    return response.data;
};

export const confirmReservation = async (id) => {
    const response = await api.put(`${RESERVATION_API}/reservations/${id}/confirmer`);
    return response.data;
};
// Réservations de l'organisateur
export const getOrganizerReservations = async (organizerId) => {
    const response = await api.get(
        `${RESERVATION_API}/reservations/organizer/${organizerId}`
    );
    return response.data;
};