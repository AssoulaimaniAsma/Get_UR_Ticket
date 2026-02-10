import axios from 'axios';

const EVENT_API = 'http://localhost:8888/event-service/api';
const USER_API = 'http://localhost:8888/user-service/api';
const RESERVATION_API = 'http://localhost:8888/reservation-service/api';

// Créer une instance axios
const api = axios.create();

// Intercepteur pour ajouter le token JWT
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
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

// ========== EVENT SERVICE ==========
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