import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '@/context/AuthContext'
import { ShieldAlert } from 'lucide-react'

interface ProtectedRouteProps {
  children: React.ReactNode
  allowedRoles?: string[]
}

export default function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { user, isAuthenticated, isLoading } = useAuth()
  const location = useLocation()

  if (isLoading) {
    return (
      <div className="flex h-screen items-center justify-center bg-surface">
        <div className="flex flex-col items-center gap-3">
          <div className="w-10 h-10 border-4 border-brand border-t-transparent rounded-full animate-spin" />
          <p className="text-sm font-medium text-slate-500">Verifying session...</p>
        </div>
      </div>
    )
  }

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  const currentRole = user.role || user.roleName || ''

  if (allowedRoles && !allowedRoles.includes(currentRole)) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center p-6 text-center">
        <div className="w-16 h-16 rounded-2xl bg-red-50 text-red-600 flex items-center justify-center mb-4 shadow-sm">
          <ShieldAlert size={32} />
        </div>
        <h2 className="text-2xl font-bold text-slate-900 mb-2">Access Denied (403)</h2>
        <p className="text-slate-600 max-w-md mb-6">
          Your account role (<span className="font-semibold text-slate-800">{currentRole}</span>) does not have permission to access this page.
        </p>
        <button
          onClick={() => {
            if (currentRole === 'ROLE_ADMIN') window.location.href = '/admin'
            else if (currentRole === 'ROLE_AGENT') window.location.href = '/agent'
            else window.location.href = '/customer'
          }}
          className="btn-primary"
        >
          Return to My Dashboard
        </button>
      </div>
    )
  }

  return <>{children}</>
}
