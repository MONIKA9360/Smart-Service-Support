import api from './api'

export interface UserDto {
  id: number
  firstName: string
  lastName: string
  email: string
  phone?: string
  roleId?: number
  roleName: 'ROLE_ADMIN' | 'ROLE_AGENT' | 'ROLE_CUSTOMER' | string
  role?: 'ROLE_ADMIN' | 'ROLE_AGENT' | 'ROLE_CUSTOMER' | string
  isActive: boolean
  createdAt?: string
  updatedAt?: string
}

export interface AuthResponseData {
  accessToken: string
  tokenType: string
  expiresIn: number
  user: UserDto
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  errorCode?: string
  data: T
  details?: Record<string, string>
}

export interface LoginPayload {
  email: string
  password: string
}

export interface RegisterPayload {
  firstName: string
  lastName: string
  email: string
  phone?: string
  password: string
}

export const authService = {
  async login(payload: LoginPayload): Promise<AuthResponseData> {
    const response = await api.post<ApiResponse<AuthResponseData>>('/auth/login', payload)
    return response.data.data
  },

  async register(payload: RegisterPayload): Promise<UserDto> {
    const response = await api.post<ApiResponse<UserDto>>('/auth/register', payload)
    return response.data.data
  },

  async getCurrentUser(): Promise<UserDto> {
    const response = await api.get<ApiResponse<UserDto>>('/auth/me')
    return response.data.data
  },
}
