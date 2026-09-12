import { NavLink } from 'react-router-dom'
import {
  LayoutDashboard,
  Ticket,
  Settings,
  Users,
  UserCircle,
  Bell,
  BarChart2,
  Briefcase,
  X,
  type LucideIcon,
} from 'lucide-react'
import clsx from 'clsx'
import { useAuth } from '@/context/AuthContext'

interface SidebarProps {
  isOpen: boolean
  onClose: () => void
}

interface NavItem {
  label: string
  to: string
  icon: LucideIcon
}

export default function Sidebar({ isOpen, onClose }: SidebarProps) {
  const { user } = useAuth()
  const role = user?.role

  let navItems: NavItem[] = []

  if (role === 'ROLE_ADMIN') {
    navItems = [
      { label: 'Admin Dashboard', to: '/admin', icon: LayoutDashboard },
      { label: 'Tickets', to: '/tickets', icon: Ticket },
      { label: 'Services', to: '/services', icon: Settings },
      { label: 'Users & Profile', to: '/profile', icon: Users },
      { label: 'Reports', to: '/reports', icon: BarChart2 },
      { label: 'Notifications', to: '/notifications', icon: Bell },
    ]
  } else if (role === 'ROLE_AGENT') {
    navItems = [
      { label: 'Agent Dashboard', to: '/agent', icon: Briefcase },
      { label: 'Assigned Tickets', to: '/tickets', icon: Ticket },
      { label: 'Notifications', to: '/notifications', icon: Bell },
      { label: 'Profile', to: '/profile', icon: UserCircle },
    ]
  } else {
    // Customer default
    navItems = [
      { label: 'Customer Dashboard', to: '/customer', icon: UserCircle },
      { label: 'My Tickets', to: '/tickets', icon: Ticket },
      { label: 'Services', to: '/services', icon: Settings },
      { label: 'Notifications', to: '/notifications', icon: Bell },
      { label: 'Profile', to: '/profile', icon: UserCircle },
    ]
  }

  return (
    <aside
      className={clsx(
        'fixed inset-y-0 left-0 z-30 w-64 flex flex-col bg-white border-r border-surface-border shadow-sm',
        'transition-transform duration-300 ease-in-out',
        'lg:relative lg:translate-x-0 lg:flex',
        isOpen ? 'translate-x-0' : '-translate-x-full'
      )}
      aria-label="Main navigation"
    >
      {/* ── Logo / Brand ─────────────────────────────────────────────────── */}
      <div className="flex items-center justify-between h-16 px-4 border-b border-surface-border flex-shrink-0">
        <div className="flex items-center gap-2.5">
          <div className="w-8 h-8 rounded-lg bg-brand flex items-center justify-center flex-shrink-0">
            <LayoutDashboard size={18} className="text-white" />
          </div>
          <div className="min-w-0">
            <p className="text-sm font-bold text-slate-900 truncate leading-tight">SmartSupport</p>
            <p className="text-[10px] text-slate-400 leading-tight">
              {role === 'ROLE_ADMIN' ? 'Admin Portal' : role === 'ROLE_AGENT' ? 'Agent Workspace' : 'Customer Portal'}
            </p>
          </div>
        </div>
        {/* Close button on mobile */}
        <button
          onClick={onClose}
          className="lg:hidden p-1.5 rounded-lg hover:bg-slate-100 text-slate-500"
          aria-label="Close sidebar"
        >
          <X size={18} />
        </button>
      </div>

      {/* ── Navigation ───────────────────────────────────────────────────── */}
      <nav className="flex-1 overflow-y-auto py-4 px-3 space-y-0.5">
        <p className="px-3 mb-2 text-[10px] font-semibold text-slate-400 uppercase tracking-widest">
          Navigation
        </p>
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            onClick={onClose}
            className={({ isActive }) =>
              clsx('nav-item', isActive && 'active')
            }
          >
            <item.icon size={18} className="flex-shrink-0" />
            <span className="truncate">{item.label}</span>
          </NavLink>
        ))}
      </nav>

      {/* ── Footer ───────────────────────────────────────────────────────── */}
      <div className="px-4 py-3 border-t border-surface-border flex-shrink-0">
        <p className="text-[11px] text-slate-400 text-center">
          Smart Service & Support v1.0
        </p>
      </div>
    </aside>
  )
}
