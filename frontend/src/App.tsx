import { BrowserRouter } from 'react-router-dom'
import AppRoutes from '@/routes/AppRoutes'

/**
 * Root application component.
 * BrowserRouter wraps the entire app for client-side routing.
 * AuthContext and other global providers will be added in Phase 2.
 */
function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  )
}

export default App
