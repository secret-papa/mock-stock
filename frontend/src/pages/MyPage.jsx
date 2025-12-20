import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import { LogOut } from 'lucide-react'
import { useLogout } from '@/hooks/useLogout'
import { useMe } from '@/hooks/useMe'
import BottomAppBar from '@/components/BottomAppBar'

export default function MyPage() {
  const { logout } = useLogout()
  const { data: me, isLoading } = useMe()

  const formatCurrency = (value) => {
    return new Intl.NumberFormat('ko-KR').format(value)
  }

  const totalAssets = (me?.balance ?? 0) + (me?.stockValuationAmount ?? 0)

  return (
    <div className="min-h-screen flex flex-col bg-background pb-12">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <div className="flex items-center justify-between mb-6 px-2">
          <h2 className="text-xl font-bold">프로필</h2>
          <button
            onClick={logout}
            className="p-2 rounded-lg bg-white shadow-sm hover:shadow transition-all"
          >
            <LogOut className="w-4 h-4 text-muted-foreground" />
          </button>
        </div>

        {isLoading ? (
          <p className="text-muted-foreground px-2">로딩 중...</p>
        ) : (
          <div className="space-y-4">
            <Card className="border-0">
              <CardContent className="pt-4 pb-4">
                <p className="font-bold text-lg">{me?.alias}</p>
                <p className="text-sm text-muted-foreground">{me?.email}</p>
              </CardContent>
            </Card>

            <Card className="border-0">
              <CardContent className="pt-5 pb-5">
                <p className="text-sm text-muted-foreground mb-1">총 자산</p>
                <div className="flex items-baseline gap-1 mb-4">
                  <span className="text-3xl font-bold">{formatCurrency(totalAssets)}</span>
                  <span className="text-lg font-medium">원</span>
                </div>
                <div className="space-y-3 pt-3 border-t">
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">보유 현금</span>
                    <span className="font-medium">{formatCurrency(me?.balance ?? 0)}원</span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-muted-foreground">주식 평가액</span>
                    <span className="font-medium">{formatCurrency(me?.stockValuationAmount ?? 0)}원</span>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        )}
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
