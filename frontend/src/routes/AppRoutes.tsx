import { Routes, Route, Navigate } from 'react-router-dom'
import MainLayout from '@/layouts/MainLayout'
import ProtectedRoute from '@/components/auth/ProtectedRoute'
import { useAuth } from '@/context/AuthContext'

// Public pages
import LandingPage from '@/pages/public/LandingPage'
import LoginPage from '@/pages/public/LoginPage'
import RegisterPage from '@/pages/public/RegisterPage'

// Dashboard pages
import AdminDashboard from '@/pages/admin/AdminDashboard'
import AgentDashboard from '@/pages/agent/AgentDashboard'
import CustomerDashboard from '@/pages/customer/CustomerDashboard'

// Shared pages
import ProfilePage from '@/pages/shared/ProfilePage'
import TicketListPage from '@/pages/shared/TicketListPage'
import ServiceListPage from '@/pages/shared/ServiceListPage'
import NotificationsPage from '@/pages/shared/NotificationsPage'
import ReportsPage from '@/pages/shared/ReportsPage'
import NotFoundPage from '@/pages/public/NotFoundPage'

/**
 * Helper to dynamically redirect /dashboard to the role's appropriate home.
 */
function DashboardRedirect() {
  const { user, isAuthenticated } = useAuth()
  if (!isAuthenticated || !user) {
    return <Navigate to="/login" replace />
  }
  if (user.role === 'ROLE_ADMIN') return <Navigate to="/admin" replace />
  if (user.role === 'ROLE_AGENT') return <Navigate to="/agent" replace />
  return <Navigate to="/customer" replace />
}

/**
 * Application route definitions with RBAC guards.
 */
export default function AppRoutes() {
  return (
    <Routes>
      {/* ── Public routes ──────────────────────────────────────────────── */}
      <Route path="/" element={<LandingPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* ── Authenticated routes (MainLayout) ──────────────────────────── */}
      <Route
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        {/* Dynamic dashboard routing */}
        <Route path="/dashboard" element={<DashboardRedirect />} />

        {/* Admin only */}
        <Route
          path="/admin/*"
          element={
            <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />

        {/* Agent only */}
        <Route
          path="/agent/*"
          element={
            <ProtectedRoute allowedRoles={['ROLE_AGENT']}>
              <AgentDashboard />
            </ProtectedRoute>
          }
        />

        {/* Customer only */}
        <Route
          path="/customer/*"
          element={
            <ProtectedRoute allowedRoles={['ROLE_CUSTOMER']}>
              <CustomerDashboard />
            </ProtectedRoute>
          }
        />

        {/* Shared Authenticated Pages */}
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/tickets" element={<TicketListPage />} />
        <Route path="/services" element={<ServiceListPage />} />
        <Route path="/notifications" element={<NotificationsPage />} />
        <Route path="/reports" element={<ReportsPage />} />
      </Route>

      {/* ── 404 ───────────────────────────────────────────────────────── */}
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  )
}
