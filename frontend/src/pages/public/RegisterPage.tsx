import { Link } from 'react-router-dom'
import { HeadphonesIcon, User, Mail, Lock, Phone, ArrowRight } from 'lucide-react'

/**
 * Customer registration page — Phase 0 scaffold.
 * Phase 2: Will wire to AuthService.register() with validation.
 */
export default function RegisterPage() {
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    alert('Registration will be implemented in Phase 2')
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-primary-950 to-primary-900
                    flex items-center justify-center p-4 py-12">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-modal p-8">
          {/* Logo */}
          <div className="flex flex-col items-center mb-8">
            <div className="w-12 h-12 rounded-xl bg-brand flex items-center justify-center mb-3 shadow-lg">
              <HeadphonesIcon size={24} className="text-white" />
            </div>
            <h1 className="text-2xl font-bold text-slate-900">Create account</h1>
            <p className="text-sm text-slate-500 mt-1">Join SmartSupport as a customer</p>
          </div>

          {/* Phase 0 notice */}
          <div className="mb-6 px-4 py-3 rounded-lg bg-amber-50 border border-amber-200 text-amber-700 text-xs text-center">
            ⚠️ Phase 0 Scaffold — Registration will be implemented in Phase 2
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label htmlFor="firstName" className="label">First name</label>
                <div className="relative">
                  <User size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                  <input id="firstName" type="text" className="input pl-8" placeholder="John" />
                </div>
              </div>
              <div>
                <label htmlFor="lastName" className="label">Last name</label>
                <input id="lastName" type="text" className="input" placeholder="Doe" />
              </div>
            </div>

            <div>
              <label htmlFor="regEmail" className="label">Email address</label>
              <div className="relative">
                <Mail size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="regEmail" type="email" className="input pl-8" placeholder="you@example.com" />
              </div>
            </div>

            <div>
              <label htmlFor="phone" className="label">Phone <span className="text-slate-400 font-normal">(optional)</span></label>
              <div className="relative">
                <Phone size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="phone" type="tel" className="input pl-8" placeholder="+1 555 000 0000" />
              </div>
            </div>

            <div>
              <label htmlFor="regPassword" className="label">Password</label>
              <div className="relative">
                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="regPassword" type="password" className="input pl-8" placeholder="Min. 8 characters" />
              </div>
            </div>

            <div>
              <label htmlFor="confirmPassword" className="label">Confirm password</label>
              <div className="relative">
                <Lock size={15} className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
                <input id="confirmPassword" type="password" className="input pl-8" placeholder="Repeat password" />
              </div>
            </div>

            <button type="submit" className="btn-primary w-full py-2.5 mt-2">
              Create Account
              <ArrowRight size={16} />
            </button>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Already have an account?{' '}
            <Link to="/login" className="font-medium text-brand hover:underline">Sign in</Link>
          </p>
        </div>

        <p className="text-center text-xs text-slate-400 mt-4">
          <Link to="/" className="hover:text-white transition-colors">← Back to home</Link>
        </p>
      </div>
    </div>
  )
}
