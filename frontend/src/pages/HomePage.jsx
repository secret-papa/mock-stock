import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import { ChevronRight, Wallet, TrendingUp } from 'lucide-react'
import { useMe } from '@/hooks/useMe'
import { usePortfolio } from '@/hooks/usePortfolio'
import { useTrades } from '@/hooks/useTrades'
import BottomAppBar from '@/components/BottomAppBar'
import LoadingSpinner from '@/components/LoadingSpinner'

export default function HomePage() {
  const navigate = useNavigate()
  const { data: me, isLoading: isLoadingMe } = useMe()
  const { holdings, stockValuationAmount, isLoading: isLoadingPortfolio } = usePortfolio()
  const { data: tradesData, isLoading: isLoadingTrades } = useTrades()

  const isLoading = isLoadingMe || isLoadingPortfolio

  const formatKRW = (value) => {
    return new Intl.NumberFormat('ko-KR').format(Math.round(value))
  }

  const formatPrice = (price, currency) => {
    const validCurrency = currency && currency !== 'null' ? currency : 'USD'
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: validCurrency,
      maximumFractionDigits: 0,
    }).format(price)
  }

  const balance = me?.balance ?? 0
  const totalAsset = balance + stockValuationAmount

  // 상위 3개 보유 주식
  const topHoldings = [...(holdings || [])]
    .sort((a, b) => (b.currentPrice * b.quantity) - (a.currentPrice * a.quantity))
    .slice(0, 3)

  // 최근 3개 거래
  const recentTrades = (tradesData?.pages?.[0]?.content || []).slice(0, 3)

  return (
    <div className="min-h-screen flex flex-col bg-background pb-16">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <div className="mb-6 px-2">
          <p className="text-muted-foreground text-sm">안녕하세요.</p>
          <h1 className="text-xl font-bold">{isLoading ? '...' : me?.alias}님</h1>
        </div>

        {isLoading ? (
          <LoadingSpinner />
        ) : (
          <>
            {/* 총 자산 */}
            <Card className="border-0 mb-4">
              <CardContent className="pt-5 pb-5">
                <p className="text-muted-foreground text-sm mb-1">총 자산</p>
                <div className="flex items-baseline gap-1 mb-3">
                  <span className="text-3xl font-bold">
                    {formatKRW(totalAsset)}
                  </span>
                  <span className="text-lg font-medium">원</span>
                </div>
                <div className="flex gap-4 text-sm">
                  <div className="flex items-center gap-1.5 text-muted-foreground">
                    <Wallet className="w-3.5 h-3.5" />
                    <span className="text-foreground font-medium">{formatKRW(balance)}원</span>
                  </div>
                  <div className="flex items-center gap-1.5 text-muted-foreground">
                    <TrendingUp className="w-3.5 h-3.5" />
                    <span className="text-foreground font-medium">{formatKRW(stockValuationAmount)}원</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* 보유 주식 TOP */}
            {topHoldings.length > 0 && (
              <Card className="border-0 mb-4">
                <CardContent className="pt-5 pb-3">
                  <div
                    className="flex justify-between items-center mb-3 cursor-pointer"
                    onClick={() => navigate('/portfolio')}
                  >
                    <p className="text-sm font-medium">보유 주식</p>
                    <ChevronRight className="w-4 h-4 text-muted-foreground" />
                  </div>
                  <div className="space-y-3">
                    {topHoldings.map((holding, index) => {
                      const isPositive = holding.profit >= 0
                      return (
                        <div
                          key={index}
                          className="flex justify-between items-center cursor-pointer"
                          onClick={() => navigate(`/stocks/${holding.ticker}`, { state: { holding } })}
                        >
                          <div>
                            <p className="font-medium text-sm">{holding.name}</p>
                            <p className="text-xs text-muted-foreground">{holding.quantity}주</p>
                          </div>
                          <div className="text-right">
                            <p className="font-medium text-sm">
                              {formatPrice(holding.currentPrice * holding.quantity, holding.currency)}
                            </p>
                            <p className={`text-xs ${isPositive ? 'text-red-500' : 'text-blue-500'}`}>
                              {isPositive ? '+' : ''}{holding.returnRate?.toFixed(2)}%
                            </p>
                          </div>
                        </div>
                      )
                    })}
                  </div>
                </CardContent>
              </Card>
            )}

            {/* 최근 거래 */}
            {!isLoadingTrades && recentTrades.length > 0 && (
              <Card className="border-0 mb-4">
                <CardContent className="pt-5 pb-3">
                  <div
                    className="flex justify-between items-center mb-3 cursor-pointer"
                    onClick={() => navigate('/trades')}
                  >
                    <p className="text-sm font-medium">최근 거래</p>
                    <ChevronRight className="w-4 h-4 text-muted-foreground" />
                  </div>
                  <div className="space-y-3">
                    {recentTrades.map((trade, index) => (
                      <div key={index} className="flex justify-between items-center">
                        <div className="flex items-center gap-2">
                          <span className={`text-xs px-1.5 py-0.5 rounded ${
                            trade.tradeType === 'BUY'
                              ? 'bg-red-50 text-red-600'
                              : 'bg-blue-50 text-blue-600'
                          }`}>
                            {trade.tradeType === 'BUY' ? '매수' : '매도'}
                          </span>
                          <p className="font-medium text-sm">{trade.name}</p>
                        </div>
                        <p className="text-sm text-muted-foreground">{trade.quantity}주</p>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            )}
          </>
        )}
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
