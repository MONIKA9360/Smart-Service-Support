import { Menu, Bell, User, LogOut } from 'lucide-react'
import { useAuth } from '@/context/AuthContext'
import { useNavigate } from 'react-router-dom'

interface TopBarProps {
  onMenuClick: () => void
}

export default function TopBar({ onMenuClick }: TopBarProps) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const formatRoleName = (role?: string) => {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'Administrator'
      case 'ROLE_AGENT':
        return 'Support Agent'
      case 'ROLE_CUSTOMER':
        return 'Customer'
      default:
        return 'User'
    }
  }

  const roleBadgeColor = (role?: string) => {
    switch (role) {
      case 'ROLE_ADMIN':
        return 'bg-purple-100 text-purple-700 border-purple-200'
      case 'ROLE_AGENT':
        return 'bg-blue-100 text-blue-700 border-blue-200'
      case 'ROLE_CUSTOMER':
        return 'bg-emerald-100 text-emerald-700 border-emerald-200'
      default:
        return 'bg-slate-100 text-slate-700 border-slate-200'
    }
  }

  return (
    <header className="h-16 bg-white border-b border-surface-border flex items-center px-4 gap-4 flex-shrink-0 z-10">
      {/* ── Hamburger (mobile) ─────────────────────────────────────────────── */}
      <button
        onClick={onMenuClick}
        className="lg:hidden p-2 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
        aria-label="Open navigation"
      >
        <Menu size={20} />
      </button>

      {/* ── App title (desktop) ────────────────────────────────────────────── */}
      <div className="hidden lg:flex items-center gap-2 text-slate-400 text-sm">
        <span className="font-semibold text-slate-900">
          Smart Service & Support Management System
        </span>
      </div>

      {/* ── Spacer ─────────────────────────────────────────────────────────── */}
      <div className="flex-1" />

      {/* ── Notification bell ──────────────────────────────────────────────── */}
      <button
        onClick={() => navigate('/notifications')}
        className="relative p-2 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
        aria-label="Notifications"
      >
        <Bell size={20} />
        <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-red-500" />
      </button>

      {/* ── User info & Role Badge ──────────────────────────────────────────── */}
      <div
        onClick={() => navigate('/profile')}
        className="flex items-center gap-2.5 pl-2 border-l border-surface-border cursor-pointer hover:bg-slate-50 rounded-lg px-2 py-1.5 transition-colors"
        title="View profile"
      >
        <div className="w-8 h-8 rounded-full bg-brand flex items-center justify-center flex-shrink-0">
          <User size={16} className="text-white" />
        </div>
        <div className="hidden sm:block text-left">
          <p className="text-sm font-semibold text-slate-800 leading-tight">
            {user ? `${user.firstName} ${user.lastName}` : 'Guest User'}
          </p>
          <span
            className={`inline-block mt-0.5 text-[10px] font-medium px-1.5 py-0.2 rounded border ${roleBadgeColor(
              user?.role
            )}`}
          >
            {formatRoleName(user?.role)}
          </span>
        </div>
      </div>

      {/* ── Logout Button ──────────────────────────────────────────────────── */}
      <button
        onClick={handleLogout}
        className="p-2 rounded-lg hover:bg-rose-50 text-slate-500 hover:text-rose-600 transition-colors"
        title="Sign out"
        aria-label="Sign out"
      >
        <LogOut size={18} />
      </button>
    </header>
  )
}
