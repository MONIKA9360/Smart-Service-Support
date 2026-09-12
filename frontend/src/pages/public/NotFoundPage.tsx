import { Link } from 'react-router-dom'
import { AlertTriangle } from 'lucide-react'

export default function NotFoundPage() {
  return (
    <div className="min-h-screen bg-surface flex items-center justify-center p-4">
      <div className="text-center max-w-sm">
        <div className="w-16 h-16 rounded-2xl bg-amber-50 border border-amber-200
                        flex items-center justify-center mx-auto mb-6">
          <AlertTriangle size={32} className="text-amber-500" />
        </div>
        <h1 className="text-5xl font-extrabold text-slate-900 mb-2">404</h1>
        <h2 className="text-xl font-semibold text-slate-700 mb-3">Page not found</h2>
        <p className="text-sm text-slate-500 mb-8">
          The page you're looking for doesn't exist or has been moved.
        </p>
        <Link to="/" className="btn-primary">
          Go back home
        </Link>
      </div>
    </div>
  )
}
