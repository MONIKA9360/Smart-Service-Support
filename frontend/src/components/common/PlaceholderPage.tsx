import { Construction } from 'lucide-react'

interface PlaceholderPageProps {
  title: string
  subtitle?: string
  phase?: string
}

/**
 * Reusable placeholder component for pages not yet implemented.
 * Shows the page title + phase note to indicate when it will be built.
 */
export default function PlaceholderPage({ title, subtitle, phase = 'Phase 2' }: PlaceholderPageProps) {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center p-8">
      <div className="w-16 h-16 rounded-2xl bg-primary-50 border border-primary-200
                      flex items-center justify-center mb-6">
        <Construction size={30} className="text-primary-500" />
      </div>
      <h1 className="page-title mb-2">{title}</h1>
      {subtitle && <p className="page-subtitle mb-6 max-w-md">{subtitle}</p>}
      <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full
                       bg-primary-50 border border-primary-200 text-primary-700 text-xs font-medium">
        🚧 Planned for {phase}
      </span>
    </div>
  )
}
