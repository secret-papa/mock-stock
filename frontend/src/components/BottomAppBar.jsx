import { Link, useLocation } from 'react-router-dom'
import { Home, User } from 'lucide-react'

const tabs = [
  { path: '/', label: '홈', icon: Home },
  { path: '/me', label: '마이페이지', icon: User },
]

export default function BottomAppBar() {
  const location = useLocation()

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-white shadow-[0_-2px_10px_rgba(0,0,0,0.08)]">
      <div className="flex items-center justify-around h-12">
        {tabs.map((tab) => {
          const isActive = location.pathname === tab.path
          const Icon = tab.icon

          return (
            <Link
              key={tab.path}
              to={tab.path}
              className={`flex flex-col items-center justify-center flex-1 h-full transition-colors ${
                isActive
                  ? 'text-primary'
                  : 'text-muted-foreground hover:text-foreground'
              }`}
            >
              <Icon className={`w-6 h-6 ${isActive ? 'stroke-[2.5]' : ''}`} />
              {isActive && (
                <div className="w-1 h-1 rounded-full bg-primary mt-1" />
              )}
            </Link>
          )
        })}
      </div>
    </nav>
  )
}
