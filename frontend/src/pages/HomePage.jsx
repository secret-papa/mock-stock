import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import { useMe } from '@/hooks/useMe'
import BottomAppBar from '@/components/BottomAppBar'

export default function HomePage() {
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
        <div className="mb-6 px-2">
          <p className="text-muted-foreground text-sm">안녕하세요,</p>
          <h1 className="text-xl font-bold">{isLoading ? '...' : me?.alias}님</h1>
        </div>

        <Card className="border-0 mb-4">
          <CardContent className="pt-5 pb-5">
            <p className="text-muted-foreground text-sm mb-1">총 자산</p>
            <div className="flex items-baseline gap-1">
              <span className="text-3xl font-bold">
                {isLoading ? '...' : formatCurrency(totalAssets)}
              </span>
              <span className="text-lg font-medium">원</span>
            </div>
          </CardContent>
        </Card>

        <Card className="border-0 mb-4">
          <CardContent className="pt-5 pb-5">
            <div className="flex justify-between items-center mb-4">
              <p className="text-sm font-medium text-muted-foreground">자산 현황</p>
            </div>
            <div className="space-y-3">
              <div className="flex justify-between items-center">
                <span className="text-muted-foreground">보유 현금</span>
                <span className="font-medium">{isLoading ? '...' : formatCurrency(me?.balance ?? 0)}원</span>
              </div>
              <div className="flex justify-between items-center">
                <span className="text-muted-foreground">주식 평가액</span>
                <span className="font-medium">{isLoading ? '...' : formatCurrency(me?.stockValuationAmount ?? 0)}원</span>
              </div>
            </div>
          </CardContent>
        </Card>
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
