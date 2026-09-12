import { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import { authService, UserDto, LoginPayload, RegisterPayload } from '@/services/auth.service'

/**
 * Security Notice:
 * Tokens are stored in localStorage for MVP demo session persistence.
 * Trade-off: localStorage is susceptible to token extraction if the application
 * has an XSS vulnerability. In production, consider HttpOnly secure cookies.
 */
interface AuthContextType {
  user: UserDto | null
  token: string | null
  role: string | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (payload: LoginPayload) => Promise<UserDto>
  register: (payload: RegisterPayload) => Promise<UserDto>
  logout: () => void
  refreshUser: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<UserDto | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState<boolean>(true)

  const normalizeUser = (userData: UserDto): UserDto => {
    return {
      ...userData,
      role: userData.role || userData.roleName,
      roleName: userData.roleName || (userData.role as string),
    }
  }

  useEffect(() => {
    const initializeAuth = async () => {
      const storedToken = localStorage.getItem('access_token')
      const storedUser = localStorage.getItem('auth_user')

      if (storedToken && storedUser) {
        try {
          // Parse cached user immediately for fast UI restore
          const parsed = JSON.parse(storedUser)
          setUser(normalizeUser(parsed))
          setToken(storedToken)

          // Verify token validity against /api/auth/me
          const currentUser = await authService.getCurrentUser()
          const normalized = normalizeUser(currentUser)
          setUser(normalized)
          localStorage.setItem('auth_user', JSON.stringify(normalized))
        } catch {
          // Token expired or invalid
          localStorage.removeItem('access_token')
          localStorage.removeItem('auth_user')
          setUser(null)
          setToken(null)
        }
      }
      setIsLoading(false)
    }

    initializeAuth()
  }, [])

  const login = async (payload: LoginPayload): Promise<UserDto> => {
    setIsLoading(true)
    try {
      const data = await authService.login(payload)
      const normalizedUser = normalizeUser(data.user)
      localStorage.setItem('access_token', data.accessToken)
      localStorage.setItem('auth_user', JSON.stringify(normalizedUser))
      setToken(data.accessToken)
      setUser(normalizedUser)
      return normalizedUser
    } finally {
      setIsLoading(false)
    }
  }

  const register = async (payload: RegisterPayload): Promise<UserDto> => {
    setIsLoading(true)
    try {
      const registeredUser = await authService.register(payload)
      return normalizeUser(registeredUser)
    } finally {
      setIsLoading(false)
    }
  }

  const logout = () => {
    localStorage.removeItem('access_token')
    localStorage.removeItem('auth_user')
    setUser(null)
    setToken(null)
  }

  const refreshUser = async () => {
    try {
      const currentUser = await authService.getCurrentUser()
      const normalized = normalizeUser(currentUser)
      setUser(normalized)
      localStorage.setItem('auth_user', JSON.stringify(normalized))
    } catch {
      logout()
    }
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        role: user?.role || user?.roleName || null,
        isAuthenticated: !!token && !!user,
        isLoading,
        login,
        register,
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
