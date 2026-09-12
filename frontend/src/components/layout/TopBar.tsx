import { Menu, Bell, ChevronDown, User } from 'lucide-react'

interface TopBarProps {
  onMenuClick: () => void
}

/**
 * Top navigation bar.
 * Shows hamburger (mobile), page context, notifications, and user avatar.
 * Phase 2: Will display logged-in user name/role and notification count.
 */
export default function TopBar({ onMenuClick }: TopBarProps) {
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
        className="relative p-2 rounded-lg hover:bg-slate-100 text-slate-600 transition-colors"
        aria-label="Notifications"
      >
        <Bell size={20} />
        {/* Unread count badge — will be driven by real data in Phase 8 */}
        <span className="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-red-500" />
      </button>

      {/* ── User menu ──────────────────────────────────────────────────────── */}
      <div className="flex items-center gap-2 pl-2 border-l border-surface-border cursor-pointer
                      hover:bg-slate-50 rounded-lg px-2 py-1.5 transition-colors">
        <div className="w-8 h-8 rounded-full bg-brand flex items-center justify-center">
          <User size={16} className="text-white" />
        </div>
        <div className="hidden sm:block text-left">
          <p className="text-sm font-medium text-slate-800 leading-tight">Demo User</p>
          <p className="text-[11px] text-slate-400 leading-tight">Phase 0 Scaffold</p>
        </div>
        <ChevronDown size={16} className="text-slate-400 hidden sm:block" />
      </div>
    </header>
  )
}
