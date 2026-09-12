import axios, { AxiosError, InternalAxiosRequestConfig } from 'axios'

/**
 * Configured Axios instance with JWT interceptors.
 *
 * Security Note: Tokens are retrieved from localStorage for MVP browser session persistence.
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// ── Request interceptor: Attach JWT Bearer token ───────────────────────────────
api.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem('access_token')
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// ── Response interceptor: Handle 401 & 403 ─────────────────────────────────────
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      // Clear expired or invalid credentials
      const currentPath = window.location.pathname
      const isAuthRoute = currentPath === '/login' || currentPath === '/register' || currentPath === '/'

      if (!isAuthRoute) {
        localStorage.removeItem('access_token')
        localStorage.removeItem('auth_user')
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default api
