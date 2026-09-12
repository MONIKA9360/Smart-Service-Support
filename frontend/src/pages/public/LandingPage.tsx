import { Link } from 'react-router-dom'
import {
  Ticket, Users, BarChart2, Bell, Shield, Zap,
  CheckCircle, ArrowRight, HeadphonesIcon,
} from 'lucide-react'

// Feature cards data
const features = [
  {
    icon: Ticket,
    title: 'Ticket Management',
    description: 'Create, track, and resolve support tickets with a full lifecycle workflow from OPEN to CLOSED.',
    color: 'bg-blue-50 text-blue-600',
  },
  {
    icon: Users,
    title: 'Role-Based Access',
    description: 'Admin, Support Agent, and Customer roles with fine-grained permissions and separate dashboards.',
    color: 'bg-purple-50 text-purple-600',
  },
  {
    icon: BarChart2,
    title: 'Analytics Dashboard',
    description: 'Real-time charts and metrics for ticket trends, agent performance, and customer satisfaction.',
    color: 'bg-green-50 text-green-600',
  },
  {
    icon: Bell,
    title: 'Smart Notifications',
    description: 'Instant in-app notifications for assignments, status updates, and resolution confirmations.',
    color: 'bg-amber-50 text-amber-600',
  },
  {
    icon: Shield,
    title: 'Secure & Reliable',
    description: 'JWT authentication, BCrypt password hashing, and role-based API protection for enterprise security.',
    color: 'bg-red-50 text-red-600',
  },
  {
    icon: Zap,
    title: 'Fast Resolution',
    description: 'Priority queues, assignment workflows, and performance tracking to reduce ticket resolution time.',
    color: 'bg-indigo-50 text-indigo-600',
  },
]

const steps = [
  { step: '01', title: 'Customer Raises Ticket', desc: 'Customer creates a ticket describing the issue, selects service category and priority.' },
  { step: '02', title: 'Admin Assigns Agent',    desc: 'Admin reviews the ticket and assigns it to the most suitable support agent.' },
  { step: '03', title: 'Agent Resolves Issue',   desc: 'Agent updates status, communicates via comments, and resolves the ticket.' },
  { step: '04', title: 'Customer Confirms',      desc: 'Customer confirms resolution and submits a satisfaction rating.' },
]

/**
 * Landing page — public, shown to unauthenticated users.
 * Professional SaaS-style hero + features + how-it-works + CTA.
 */
export default function LandingPage() {
  return (
    <div className="min-h-screen bg-white flex flex-col">

      {/* ── Navigation ────────────────────────────────────────────────────── */}
      <nav className="sticky top-0 z-50 bg-white/95 backdrop-blur border-b border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-2.5">
              <div className="w-8 h-8 rounded-lg bg-brand flex items-center justify-center">
                <HeadphonesIcon size={18} className="text-white" />
              </div>
              <span className="font-bold text-slate-900 text-base">SmartSupport</span>
            </div>
            <div className="flex items-center gap-3">
              <Link to="/login"
                className="px-4 py-2 text-sm font-medium text-slate-700 hover:text-brand transition-colors">
                Sign In
              </Link>
              <Link to="/register"
                className="btn-primary text-sm px-4 py-2">
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* ── Hero ──────────────────────────────────────────────────────────── */}
      <section className="relative overflow-hidden bg-gradient-to-br from-slate-900 via-primary-950 to-primary-900">
        {/* Background decoration */}
        <div className="absolute inset-0 overflow-hidden pointer-events-none">
          <div className="absolute -top-40 -right-40 w-96 h-96 rounded-full bg-primary-600/20 blur-3xl" />
          <div className="absolute -bottom-40 -left-40 w-96 h-96 rounded-full bg-blue-600/10 blur-3xl" />
        </div>

        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-24 lg:py-32">
          <div className="max-w-3xl">
            {/* Badge */}
            <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-primary-500/20 border border-primary-400/30 text-primary-300 text-xs font-medium mb-6">
              <span className="w-1.5 h-1.5 rounded-full bg-primary-400 animate-pulse" />
              Final-Year CS Engineering Project
            </span>

            <h1 className="text-4xl lg:text-6xl font-extrabold text-white leading-tight mb-6">
              Smart Service &amp;{' '}
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-cyan-400">
                Support Management
              </span>{' '}
              System
            </h1>

            <p className="text-lg text-slate-300 leading-relaxed mb-8 max-w-2xl">
              A centralized platform that replaces scattered phone calls, emails, and
              spreadsheets with a professional ticket management system — giving customers,
              agents, and admins complete visibility into every service request.
            </p>

            <div className="flex flex-col sm:flex-row gap-4">
              <Link to="/register"
                className="btn-primary px-6 py-3 text-base font-semibold gap-2">
                Create Free Account
                <ArrowRight size={18} />
              </Link>
              <Link to="/login"
                className="inline-flex items-center justify-center px-6 py-3 text-base font-semibold
                           text-white border border-white/20 rounded-lg hover:bg-white/10 transition-colors">
                Sign In to Dashboard
              </Link>
            </div>

            {/* Trust badges */}
            <div className="flex flex-wrap items-center gap-6 mt-10 text-slate-400 text-sm">
              {['JWT Secured', 'Role-Based Access', 'Real-time Analytics', 'MySQL Powered'].map(badge => (
                <span key={badge} className="flex items-center gap-1.5">
                  <CheckCircle size={14} className="text-green-400" />
                  {badge}
                </span>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* ── Features ──────────────────────────────────────────────────────── */}
      <section className="py-20 bg-surface">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl font-bold text-slate-900 mb-4">
              Everything you need to manage support
            </h2>
            <p className="text-slate-500 max-w-xl mx-auto">
              A complete suite of tools for customers, support agents, and administrators
              to efficiently manage service requests end-to-end.
            </p>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map(({ icon: Icon, title, description, color }) => (
              <div key={title} className="card hover:shadow-md transition-shadow duration-200">
                <div className={`w-10 h-10 rounded-lg ${color} flex items-center justify-center mb-4`}>
                  <Icon size={20} />
                </div>
                <h3 className="font-semibold text-slate-900 mb-2">{title}</h3>
                <p className="text-sm text-slate-500 leading-relaxed">{description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── How It Works ──────────────────────────────────────────────────── */}
      <section className="py-20 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl font-bold text-slate-900 mb-4">How it works</h2>
            <p className="text-slate-500">Four simple steps from issue to resolution</p>
          </div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-8">
            {steps.map(({ step, title, desc }) => (
              <div key={step} className="text-center">
                <div className="w-12 h-12 rounded-full bg-primary-50 border-2 border-primary-200
                                flex items-center justify-center mx-auto mb-4">
                  <span className="text-primary-700 font-bold text-sm">{step}</span>
                </div>
                <h3 className="font-semibold text-slate-900 mb-2 text-sm">{title}</h3>
                <p className="text-xs text-slate-500 leading-relaxed">{desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── Roles Section ─────────────────────────────────────────────────── */}
      <section className="py-20 bg-gradient-to-br from-primary-50 to-slate-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-14">
            <h2 className="text-3xl font-bold text-slate-900 mb-4">Built for every role</h2>
            <p className="text-slate-500">One platform, three tailored experiences</p>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {[
              {
                role: 'Customer',
                color: 'border-blue-300 bg-blue-50',
                titleColor: 'text-blue-700',
                perks: ['Raise support tickets', 'Track ticket status', 'Communicate with agents', 'Submit satisfaction ratings'],
              },
              {
                role: 'Support Agent',
                color: 'border-purple-300 bg-purple-50',
                titleColor: 'text-purple-700',
                perks: ['View assigned tickets', 'Update ticket status', 'Add comments & files', 'Track performance stats'],
              },
              {
                role: 'Administrator',
                color: 'border-green-300 bg-green-50',
                titleColor: 'text-green-700',
                perks: ['Full system control', 'Assign tickets to agents', 'View analytics & reports', 'Manage users & services'],
              },
            ].map(({ role, color, titleColor, perks }) => (
              <div key={role} className={`card border-2 ${color}`}>
                <h3 className={`font-bold text-lg mb-4 ${titleColor}`}>{role}</h3>
                <ul className="space-y-2">
                  {perks.map(perk => (
                    <li key={perk} className="flex items-center gap-2 text-sm text-slate-600">
                      <CheckCircle size={14} className="text-green-500 flex-shrink-0" />
                      {perk}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ── CTA ───────────────────────────────────────────────────────────── */}
      <section className="py-20 bg-brand">
        <div className="max-w-3xl mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold text-white mb-4">
            Ready to streamline your support?
          </h2>
          <p className="text-blue-100 mb-8">
            Get started in minutes — no configuration needed for the demo environment.
          </p>
          <Link to="/register"
            className="inline-flex items-center gap-2 px-8 py-3.5 bg-white text-brand
                       font-semibold rounded-xl hover:bg-blue-50 transition-colors text-base">
            Create Your Account
            <ArrowRight size={18} />
          </Link>
        </div>
      </section>

      {/* ── Footer ────────────────────────────────────────────────────────── */}
      <footer className="bg-slate-900 text-slate-400 py-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col sm:flex-row items-center justify-between gap-4">
            <div className="flex items-center gap-2">
              <div className="w-7 h-7 rounded-lg bg-brand flex items-center justify-center">
                <HeadphonesIcon size={14} className="text-white" />
              </div>
              <span className="text-white font-semibold text-sm">SmartSupport</span>
            </div>
            <p className="text-xs text-center">
              Smart Service &amp; Support Management System — Final-Year CS Engineering Project © 2026
            </p>
            <div className="flex gap-4 text-xs">
              <Link to="/login"    className="hover:text-white transition-colors">Sign In</Link>
              <Link to="/register" className="hover:text-white transition-colors">Register</Link>
            </div>
          </div>
        </div>
      </footer>
    </div>
  )
}
