import { useAuth } from '@/context/AuthContext'
import { User, Mail, Phone, Shield, CheckCircle2, XCircle, KeyRound } from 'lucide-react'

export default function ProfilePage() {
  const { user } = useAuth()

  if (!user) {
    return (
      <div className="p-6">
        <p className="text-slate-500">No profile data available. Please log in.</p>
      </div>
    )
  }

  const roleString = user.role || user.roleName || ''

  const formatRoleName = (role?: string) => {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'System Administrator'
      case 'ROLE_AGENT':
        return 'Customer Support Agent'
      case 'ROLE_CUSTOMER':
        return 'Customer / Client'
      default:
        return role ?? 'User'
    }
  }

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* ── Page Header ─────────────────────────────────────────────────── */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900">My Profile</h1>
        <p className="text-sm text-slate-500 mt-1">
          View your authenticated account details and role permissions.
        </p>
      </div>

      {/* ── Profile Card ────────────────────────────────────────────────── */}
      <div className="bg-white rounded-xl border border-surface-border shadow-sm overflow-hidden">
        {/* Banner */}
        <div className="h-28 bg-gradient-to-r from-brand to-brand-dark p-6 flex items-end">
          <div className="flex items-center gap-4 translate-y-8">
            <div className="w-20 h-20 rounded-2xl bg-white p-1 shadow-md">
              <div className="w-full h-full rounded-xl bg-brand-light flex items-center justify-center text-brand">
                <User size={36} />
              </div>
            </div>
          </div>
        </div>

        {/* Header Info */}
        <div className="pt-10 px-6 pb-6 border-b border-surface-border flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h2 className="text-xl font-bold text-slate-900">
              {user.firstName} {user.lastName}
            </h2>
            <p className="text-sm text-slate-500 flex items-center gap-1.5 mt-0.5">
              <Mail size={14} /> {user.email}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <span className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-brand-light text-brand">
              <Shield size={13} /> {formatRoleName(roleString)}
            </span>
            {user.isActive ? (
              <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
                <CheckCircle2 size={13} /> Active
              </span>
            ) : (
              <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full text-xs font-medium bg-rose-50 text-rose-700 border border-rose-200">
                <XCircle size={13} /> Inactive
              </span>
            )}
          </div>
        </div>

        {/* Details Grid */}
        <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">First Name</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-medium text-slate-800">
              {user.firstName}
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Last Name</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-medium text-slate-800">
              {user.lastName}
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Email Address</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-medium text-slate-800 flex items-center gap-2">
              <Mail size={16} className="text-slate-400" />
              {user.email}
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Phone Number</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-medium text-slate-800 flex items-center gap-2">
              <Phone size={16} className="text-slate-400" />
              {user.phone || 'Not provided'}
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">User ID</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-mono text-slate-600 flex items-center gap-2">
              <KeyRound size={16} className="text-slate-400" />
              {user.id}
            </div>
          </div>

          <div className="space-y-1">
            <label className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Assigned Role</label>
            <div className="p-3 bg-slate-50 rounded-lg border border-slate-100 text-sm font-mono text-slate-700 flex items-center gap-2">
              <Shield size={16} className="text-slate-400" />
              {roleString}
            </div>
          </div>
        </div>

        {/* Security Notice */}
        <div className="px-6 py-4 bg-slate-50 border-t border-surface-border text-xs text-slate-500 flex items-center gap-2">
          <Shield size={14} className="text-brand flex-shrink-0" />
          <span>
            Security Notice: Passwords and sensitive credentials are encrypted using BCrypt and are never exposed via client interfaces.
          </span>
        </div>
      </div>
    </div>
  )
}
