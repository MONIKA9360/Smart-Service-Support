import { Routes, Route, Navigate } from 'react-router-dom'
import MainLayout from '@/layouts/MainLayout'

// Public pages
import LandingPage    from '@/pages/public/LandingPage'
import LoginPage      from '@/pages/public/LoginPage'
import RegisterPage   from '@/pages/public/RegisterPage'

// Placeholder dashboard pages
import AdminDashboard    from '@/pages/admin/AdminDashboard'
import AgentDashboard    from '@/pages/agent/AgentDashboard'
import CustomerDashboard from '@/pages/customer/CustomerDashboard'

// Shared placeholder pages
import ProfilePage        from '@/pages/shared/ProfilePage'
import TicketListPage     from '@/pages/shared/TicketListPage'
import ServiceListPage    from '@/pages/shared/ServiceListPage'
import NotificationsPage  from '@/pages/shared/NotificationsPage'
import ReportsPage        from '@/pages/shared/ReportsPage'
import NotFoundPage       from '@/pages/public/NotFoundPage'

/**
 * Application route definitions.
 *
 * Phase 0: All routes are accessible (no auth guards yet).
 * Phase 2: ProtectedRoute HOC will enforce role-based access control.
 */
export default function AppRoutes() {
  return (
    <Routes>
      {/* ── Public routes (no layout wrapper) ─────────────────────────────── */}
      <Route path="/"         element={<LandingPage />} />
      <Route path="/login"    element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* ── Authenticated routes (inside MainLayout) ───────────────────────── */}
      <Route element={<MainLayout />}>
        {/* Dashboard redirects */}
        <Route path="/dashboard"          element={<Navigate to="/admin" replace />} />

        {/* Admin */}
        <Route path="/admin"              element={<AdminDashboard />} />

        {/* Agent */}
        <Route path="/agent"              element={<AgentDashboard />} />

        {/* Customer */}
        <Route path="/customer"           element={<CustomerDashboard />} />

        {/* Shared */}
        <Route path="/profile"            element={<ProfilePage />} />
        <Route path="/tickets"            element={<TicketListPage />} />
        <Route path="/services"           element={<ServiceListPage />} />
        <Route path="/notifications"      element={<NotificationsPage />} />
        <Route path="/reports"            element={<ReportsPage />} />
      </Route>

      {/* ── 404 ────────────────────────────────────────────────────────────── */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}
