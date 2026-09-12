import axios from 'axios'

/**
 * Configured Axios instance for all API requests.
 *
 * Phase 0: Basic instance with baseURL only.
 * Phase 2: JWT interceptors (request + response) will be added here.
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// ── Request interceptor — Phase 2 will attach JWT here ─────────────────────
api.interceptors.request.use(
  (config) => {
    // TODO Phase 2: Read token from AuthContext / localStorage and attach
    // const token = localStorage.getItem('access_token')
    // if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  },
  (error) => Promise.reject(error)
)

// ── Response interceptor — Phase 2 will handle 401 / token refresh here ────
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // TODO Phase 2: Handle 401 → redirect to /login
    // TODO Phase 2: Handle 403 → show forbidden message
    return Promise.reject(error)
  }
)

export default api
