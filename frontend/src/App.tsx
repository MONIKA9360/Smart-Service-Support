import { BrowserRouter } from 'react-router-dom'
import { AuthProvider } from '@/context/AuthContext'
import AppRoutes from '@/routes/AppRoutes'

/**
 * Root application component.
 * BrowserRouter and AuthProvider wrap the entire application.
 */
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  )
}

export default App
