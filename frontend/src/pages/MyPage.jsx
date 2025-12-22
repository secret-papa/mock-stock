import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { User } from 'lucide-react'
import { useLogout } from '@/hooks/useLogout'
import { useMe } from '@/hooks/useMe'
import BottomAppBar from '@/components/BottomAppBar'
import LoadingSpinner from '@/components/LoadingSpinner'

export default function MyPage() {
  const { logout } = useLogout()
  const { data: me, isLoading } = useMe()

  return (
    <div className="min-h-screen flex flex-col bg-background pb-16">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <h2 className="text-xl font-bold mb-6 px-2">마이페이지</h2>

        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <div className="space-y-4">
            {/* 프로필 정보 */}
            <Card className="border-0">
              <CardContent className="pt-5 pb-5">
                <div className="space-y-4">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center">
                      <User className="w-5 h-5 text-primary" />
                    </div>
                    <div>
                      <p className="font-bold text-lg">{me?.alias}</p>
                      <p className="text-sm text-muted-foreground">{me?.email}</p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* 계정 관리 */}
            <Button
              onClick={logout}
              variant="outline"
              className="w-full h-12"
            >
              로그아웃
            </Button>
          </div>
        )}
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
