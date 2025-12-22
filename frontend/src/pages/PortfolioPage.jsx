import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import { ArrowRightLeft } from 'lucide-react'
import BottomAppBar from '@/components/BottomAppBar'
import LoadingSpinner from '@/components/LoadingSpinner'
import { usePortfolio } from '@/hooks/usePortfolio'

export default function PortfolioPage() {
  const navigate = useNavigate()
  const { holdings, stockValuationAmount, totalProfit, exchangeRate, isLoading } = usePortfolio()

  const hasUSDHoldings = holdings.some(h => h.currency && h.currency !== 'null' && h.currency !== 'KRW')

  const formatPrice = (price, currency) => {
    const validCurrency = currency && currency !== 'null' ? currency : 'USD'
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: validCurrency,
      maximumFractionDigits: 0,
    }).format(price)
  }

  const formatKRW = (value) => {
    return new Intl.NumberFormat('ko-KR').format(Math.round(value))
  }

  return (
    <div className="min-h-screen flex flex-col bg-background pb-16">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <h2 className="text-xl font-bold mb-4 px-2">보유 주식</h2>

        <Card className="border-0 mb-4">
          <CardContent className="pt-4 pb-4">
            <p className="text-sm text-muted-foreground mb-1">총 평가 금액</p>
            <div className="flex items-baseline gap-2">
              <p className="text-2xl font-bold">
                {formatKRW(stockValuationAmount)}원
              </p>
              {totalProfit !== 0 && (
                <span className={`text-sm ${totalProfit >= 0 ? 'text-red-500' : 'text-blue-500'}`}>
                  {totalProfit >= 0 ? '+' : ''}{formatKRW(totalProfit)}원
                </span>
              )}
            </div>
            {hasUSDHoldings && (
              <div className="flex items-center gap-1 text-xs text-muted-foreground mt-1">
                <ArrowRightLeft className="w-3 h-3" />
                <span>$1 = ₩{formatKRW(exchangeRate)}</span>
              </div>
            )}
          </CardContent>
        </Card>

        {isLoading ? (
          <LoadingSpinner />
        ) : holdings.length === 0 ? (
          <Card className="border-0">
            <CardContent className="py-8 text-center text-muted-foreground">
              보유 중인 주식이 없습니다
            </CardContent>
          </Card>
        ) : (
          <Card className="border-0">
            <CardContent className="p-0">
              <ul className="divide-y">
                {holdings.map((holding, index) => {
                  const isPositive = holding.profit >= 0
                  const totalValue = holding.currentPrice * holding.quantity

                  return (
                    <li
                      key={index}
                      className="px-4 py-3 cursor-pointer hover:bg-muted/50 transition-colors active:bg-muted"
                      onClick={() => navigate(`/stocks/${holding.ticker}`, { state: { holding } })}
                    >
                      <div className="flex justify-between items-start mb-1">
                        <div>
                          <p className="font-medium">{holding.name}</p>
                          <p className="text-sm text-muted-foreground">
                            {holding.ticker} · {holding.quantity}주
                          </p>
                        </div>
                        <div className="text-right">
                          <p className="font-medium">
                            {formatPrice(totalValue, holding.currency)}
                          </p>
                          <p className={`text-sm ${isPositive ? 'text-red-500' : 'text-blue-500'}`}>
                            {isPositive ? '+' : ''}{formatPrice(holding.profit, holding.currency)} ({holding.returnRate.toFixed(2)}%)
                          </p>
                        </div>
                      </div>
                      <div className="flex justify-between text-sm text-muted-foreground">
                        <span>평균 {formatPrice(holding.avgPrice, holding.currency)}</span>
                        <span>현재 {formatPrice(holding.currentPrice, holding.currency)}</span>
                      </div>
                    </li>
                  )
                })}
              </ul>
            </CardContent>
          </Card>
        )}
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
