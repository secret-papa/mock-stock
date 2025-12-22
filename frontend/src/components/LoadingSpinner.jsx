import { Loader2 } from 'lucide-react'

export default function LoadingSpinner({ className = '' }) {
  return (
    <div className={`flex items-center justify-center py-8 ${className}`}>
      <Loader2 className="w-6 h-6 animate-spin text-primary" />
    </div>
  )
}
