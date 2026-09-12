import { Link } from 'react-router-dom'
import { HeadphonesIcon, Mail, Lock, ArrowRight } from 'lucide-react'

/**
 * Login page — Phase 0 scaffold.
 * Phase 2: Will wire to AuthService, JWT storage, and redirect by role.
 */
export default function LoginPage() {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // TODO Phase 2: Call AuthService.login() and redirect
    alert('Authentication will be implemented in Phase 2')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-primary-950 to-primary-900
                    flex items-center justify-center p-4">
      <div className="w-full max-w-md">

        {/* Card */}
        <div className="bg-white rounded-2xl shadow-modal p-8">
          {/* Logo */}
          <div className="flex flex-col items-center mb-8">
            <div className="w-12 h-12 rounded-xl bg-brand flex items-center justify-center mb-3 shadow-lg">
              <HeadphonesIcon size={24} className="text-white" />
            </div>
            <h1 className="text-2xl font-bold text-slate-900">Welcome back</h1>
            <p className="text-sm text-slate-500 mt-1">Sign in to your SmartSupport account</p>
          </div>

          {/* Phase 0 notice */}
          <div className="mb-6 px-4 py-3 rounded-lg bg-amber-50 border border-amber-200 text-amber-700 text-xs text-center">
            ⚠️ Phase 0 Scaffold — Authentication will be implemented in Phase 2
          </div>

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label htmlFor="email" className="label">Email address</label>
              <div className="relative">
                <Mail size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="email" type="email" className="input pl-9"
                       placeholder="you@example.com" autoComplete="email" />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between mb-1">
                <label htmlFor="password" className="label mb-0">Password</label>
                <a href="#" className="text-xs text-brand hover:underline">Forgot password?</a>
              </div>
              <div className="relative">
                <Lock size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="password" type="password" className="input pl-9"
                       placeholder="••••••••" autoComplete="current-password" />
              </div>
            </div>

            <button type="submit" className="btn-primary w-full py-2.5 mt-2">
              Sign In
              <ArrowRight size={16} />
            </button>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Don&apos;t have an account?{' '}
            <Link to="/register" className="font-medium text-brand hover:underline">
              Create one
            </Link>
          </p>
        </div>

        <p className="text-center text-xs text-slate-400 mt-4">
          <Link to="/" className="hover:text-white transition-colors">← Back to home</Link>
        </p>
      </div>
    </div>
  )
}
