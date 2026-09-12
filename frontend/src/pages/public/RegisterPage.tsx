import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { yupResolver } from '@hookform/resolvers/yup'
import * as yup from 'yup'
import { HeadphonesIcon, User, Mail, Lock, Phone, ArrowRight, Eye, EyeOff, AlertCircle, CheckCircle2 } from 'lucide-react'
import { useAuth } from '@/context/AuthContext'

interface RegisterFormData {
  firstName: string
  lastName: string
  email: string
  phone?: string
  password: string
  confirmPassword: string
}

const registerSchema = yup.object({
  firstName: yup.string().required('First name is required').max(50, 'First name is too long'),
  lastName: yup.string().required('Last name is required').max(50, 'Last name is too long'),
  email: yup.string().email('Enter a valid email address').required('Email is required'),
  phone: yup.string().optional(),
  password: yup
    .string()
    .required('Password is required')
    .min(8, 'Password must be at least 8 characters long'),
  confirmPassword: yup
    .string()
    .required('Please confirm your password')
    .oneOf([yup.ref('password')], 'Passwords must match'),
}).required()

export default function RegisterPage() {
  const { register: registerAuth } = useAuth()
  const navigate = useNavigate()

  const [showPassword, setShowPassword] = useState(false)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const [successMessage, setSuccessMessage] = useState<string | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: yupResolver(registerSchema),
  })

  const onSubmit = async (data: RegisterFormData) => {
    setErrorMessage(null)
    setSuccessMessage(null)
    setIsSubmitting(true)

    try {
      await registerAuth({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        phone: data.phone || undefined,
        password: data.password,
      })

      setSuccessMessage('Registration successful! Redirecting to sign in...')
      setTimeout(() => {
        navigate('/login', { replace: true })
      }, 1500)
    } catch (err: unknown) {
      const error = err as { response?: { data?: { message?: string } } }
      const msg = error.response?.data?.message || 'Registration failed. Please check your details.'
      setErrorMessage(msg)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-primary-950 to-primary-900 flex items-center justify-center p-4 py-12">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-modal p-8">
          {/* Logo & Header */}
          <div className="flex flex-col items-center mb-6">
            <div className="w-12 h-12 rounded-xl bg-brand flex items-center justify-center mb-3 shadow-lg">
              <HeadphonesIcon size={24} className="text-white" />
            </div>
            <h1 className="text-2xl font-bold text-slate-900">Create account</h1>
            <p className="text-sm text-slate-500 mt-1">Join SmartSupport as a customer</p>
          </div>

          {/* Success Alert */}
          {successMessage && (
            <div className="mb-5 p-3.5 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-sm flex items-center gap-2.5">
              <CheckCircle2 size={18} className="text-emerald-600 flex-shrink-0" />
              <span>{successMessage}</span>
            </div>
          )}

          {/* Error Alert */}
          {errorMessage && (
            <div className="mb-5 p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm flex items-start gap-2.5">
              <AlertCircle size={18} className="text-red-500 flex-shrink-0 mt-0.5" />
              <span>{errorMessage}</span>
            </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-3.5">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label htmlFor="firstName" className="label">
                  First name
                </label>
                <div className="relative">
                  <User size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input
                    id="firstName"
                    type="text"
                    className={`input pl-8 ${errors.firstName ? 'border-red-500' : ''}`}
                    placeholder="John"
                    {...register('firstName')}
                  />
                </div>
                {errors.firstName && (
                  <p className="text-[11px] text-red-500 mt-1">{errors.firstName.message}</p>
                )}
              </div>

              <div>
                <label htmlFor="lastName" className="label">
                  Last name
                </label>
                <input
                  id="lastName"
                  type="text"
                  className={`input ${errors.lastName ? 'border-red-500' : ''}`}
                  placeholder="Doe"
                  {...register('lastName')}
                />
                {errors.lastName && (
                  <p className="text-[11px] text-red-500 mt-1">{errors.lastName.message}</p>
                )}
              </div>
            </div>

            <div>
              <label htmlFor="regEmail" className="label">
                Email address
              </label>
              <div className="relative">
                <Mail size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="regEmail"
                  type="email"
                  className={`input pl-8 ${errors.email ? 'border-red-500' : ''}`}
                  placeholder="you@example.com"
                  autoComplete="email"
                  {...register('email')}
                />
              </div>
              {errors.email && (
                <p className="text-[11px] text-red-500 mt-1">{errors.email.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="phone" className="label">
                Phone <span className="text-slate-400 font-normal">(optional)</span>
              </label>
              <div className="relative">
                <Phone size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="phone"
                  type="tel"
                  className="input pl-8"
                  placeholder="+1 555 000 0000"
                  {...register('phone')}
                />
              </div>
            </div>

            <div>
              <label htmlFor="regPassword" className="label">
                Password
              </label>
              <div className="relative">
                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="regPassword"
                  type={showPassword ? 'text' : 'password'}
                  className={`input pl-8 pr-9 ${errors.password ? 'border-red-500' : ''}`}
                  placeholder="Min. 8 characters"
                  autoComplete="new-password"
                  {...register('password')}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 focus:outline-none"
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                >
                  {showPassword ? <EyeOff size={15} /> : <Eye size={15} />}
                </button>
              </div>
              {errors.password && (
                <p className="text-[11px] text-red-500 mt-1">{errors.password.message}</p>
              )}
            </div>

            <div>
              <label htmlFor="confirmPassword" className="label">
                Confirm password
              </label>
              <div className="relative">
                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input
                  id="confirmPassword"
                  type={showPassword ? 'text' : 'password'}
                  className={`input pl-8 ${errors.confirmPassword ? 'border-red-500' : ''}`}
                  placeholder="Repeat password"
                  autoComplete="new-password"
                  {...register('confirmPassword')}
                />
              </div>
              {errors.confirmPassword && (
                <p className="text-[11px] text-red-500 mt-1">{errors.confirmPassword.message}</p>
              )}
            </div>

            <button
              type="submit"
              disabled={isSubmitting}
              className="btn-primary w-full py-2.5 mt-3 flex items-center justify-center gap-2"
            >
              {isSubmitting ? (
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : (
                <>
                  <span>Create Account</span>
                  <ArrowRight size={16} />
                </>
              )}
            </button>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Already have an account?{' '}
            <Link to="/login" className="font-medium text-brand hover:underline">
              Sign in
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
