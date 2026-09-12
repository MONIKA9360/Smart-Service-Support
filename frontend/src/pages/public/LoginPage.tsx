import { useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import * as yup from 'yup'
import { HeadphonesIcon, Mail, Lock, ArrowRight, Eye, EyeOff, AlertCircle } from 'lucide-react'
import { useAuth } from '@/context/AuthContext'
import { LoginPayload } from '@/services/auth.service'

const loginSchema = yup.object({
  email: yup.string().email('Enter a valid email address').required('Email is required'),
  password: yup.string().required('Password is required'),
}).required()

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const [showPassword, setShowPassword] = useState(false)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const from = (location.state as { from?: { pathname: string } })?.from?.pathname

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<LoginPayload>({
    resolver: yupResolver(loginSchema),
  })

  const onSubmit = async (data: LoginPayload) => {
    setErrorMessage(null)
    setIsSubmitting(true)

    try {
      const user = await login(data)
      // Redirect based on role or original intended location
      if (from && from !== '/login') {
        navigate(from, { replace: true })
      } else if (user.roleName === 'ROLE_ADMIN') {
        navigate('/admin', { replace: true })
      } else if (user.roleName === 'ROLE_AGENT') {
        navigate('/agent', { replace: true })
      } else {
        navigate('/customer', { replace: true })
      }
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } }
      const msg = error.response?.data?.message || 'Invalid email or password. Please try again.'
      setErrorMessage(msg)
    } finally {
      setIsSubmitting(false)
    }
  }

  const fillDemoCredentials = (email: string) => {
    setValue('email', email, { shouldValidate: true })
    setValue('password', 'Admin@123', { shouldValidate: true })
    setErrorMessage(null)
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-primary-950 to-primary-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Card */}
        <div className="bg-white rounded-2xl shadow-modal p-8">
          {/* Header */}
          <div className="flex flex-col items-center mb-6">
            <div className="w-12 h-12 rounded-xl bg-brand flex items-center justify-center mb-3 shadow-lg">
              <HeadphonesIcon size={24} className="text-white" />
            </div>
            <h1 className="text-2xl font-bold text-slate-900">Welcome back</h1>
            <p className="text-sm text-slate-500 mt-1">Sign in to your SmartSupport account</p>
          </div>

          {/* Demo Credentials Helper */}
          <div className="mb-6 p-3 rounded-xl bg-slate-50 border border-slate-200">
            <p className="text-[11px] font-semibold text-slate-500 uppercase tracking-wider mb-2 text-center">
              Quick Demo Login (Password: Admin@123)
            </p>
            <div className="grid grid-cols-3 gap-1.5 text-xs">
              <button
                type="button"
                onClick={() => fillDemoCredentials('admin@smartsupport.com')}
                className="px-2 py-1.5 rounded-lg bg-white border border-slate-200 hover:border-brand text-slate-700 hover:text-brand font-medium transition-colors text-center"
              >
                👑 Admin
              </button>
              <button
                type="button"
                onClick={() => fillDemoCredentials('alice@smartsupport.com')}
                className="px-2 py-1.5 rounded-lg bg-white border border-slate-200 hover:border-brand text-slate-700 hover:text-brand font-medium transition-colors text-center"
              >
                🛠️ Agent
              </button>
              <button
                type="button"
                onClick={() => fillDemoCredentials('john.doe@example.com')}
                className="px-2 py-1.5 rounded-lg bg-white border border-slate-200 hover:border-brand text-slate-700 hover:text-brand font-medium transition-colors text-center"
              >
                👤 Customer
              </button>
            </div>
          </div>

          {/* Error Alert */}
          {errorMessage && (
            <div className="mb-5 p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm flex items-start gap-2.5">
              <AlertCircle size={18} className="text-red-500 flex-shrink-0 mt-0.5" />
              <span>{errorMessage}</span>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label htmlFor="email" className="label">
                Email address
              </label>
              <div className="relative">
                <Mail size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="email"
                  type="email"
                  className={`input pl-9 ${errors.email ? 'border-red-500 focus:ring-red-500' : ''}`}
                  placeholder="you@example.com"
                  autoComplete="email"
                  {...register('email')}
                />
              </div>
              {errors.email && (
                <p className="text-xs text-red-500 mt-1">{errors.email.message}</p>
              )}
            </div>

            <div>
              <div className="flex items-center justify-between mb-1">
                <label htmlFor="password" className="label mb-0">
                  Password
                </label>
              </div>
              <div className="relative">
                <Lock size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="password"
                  type={showPassword ? 'text' : 'password'}
                  className={`input pl-9 pr-9 ${errors.password ? 'border-red-500 focus:ring-red-500' : ''}`}
                  placeholder="••••••••"
                  autoComplete="current-password"
                  {...register('password')}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 focus:outline-none"
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                >
                  {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {errors.password && (
                <p className="text-xs text-red-500 mt-1">{errors.password.message}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="btn-primary w-full py-2.5 mt-2 flex items-center justify-center gap-2"
            >
              {isSubmitting ? (
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : (
                <>
                  <span>Sign In</span>
                  <ArrowRight size={16} />
                </>
              )}
            </button>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Don&apos;t have an account?{' '}
            <Link to="/register" className="font-medium text-brand hover:underline">
              Create customer account
            </Link>
          </p>
        </div>

        <p className="text-center text-xs text-slate-400 mt-4">
          <Link to="/" className="hover:text-white transition-colors">
            ← Back to home
          </Link>
        </p>
      </div>
    </div>
  )
}
