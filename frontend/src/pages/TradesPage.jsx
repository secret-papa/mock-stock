import { motion } from 'framer-motion'
import { Card, CardContent } from '@/components/ui/card'
import BottomAppBar from '@/components/BottomAppBar'
import LoadingSpinner from '@/components/LoadingSpinner'
import { useTrades } from '@/hooks/useTrades'

export default function TradesPage() {
  const { data: trades = [], isLoading } = useTrades()

  const formatPrice = (price, currency) => {
    const validCurrency = currency && currency !== 'null' ? currency : 'USD'
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: validCurrency,
      maximumFractionDigits: 0,
    }).format(price)
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return new Intl.DateTimeFormat('ko-KR', {
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    }).format(date)
  }

  return (
    <div className="min-h-screen flex flex-col bg-background pb-16">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <h2 className="text-xl font-bold mb-4 px-2">거래 내역</h2>

        {isLoading ? (
          <LoadingSpinner />
        ) : trades.length === 0 ? (
          <Card className="border-0">
            <CardContent className="py-8 text-center text-muted-foreground">
              거래 내역이 없습니다
            </CardContent>
          </Card>
        ) : (
          <Card className="border-0">
            <CardContent className="p-0">
              <ul className="divide-y">
                {trades.map((trade, index) => (
                  <li key={index} className="px-4 py-3">
                    <div className="flex justify-between items-start mb-1">
                      <div className="flex items-center gap-2">
                        <span
                          className={`px-2 py-0.5 rounded text-xs font-medium ${
                            trade.tradeType === 'BUY'
                              ? 'bg-red-100 text-red-600'
                              : 'bg-blue-100 text-blue-600'
                          }`}
                        >
                          {trade.tradeType === 'BUY' ? '매수' : '매도'}
                        </span>
                        <span className="font-medium">{trade.name}</span>
                      </div>
                      <p className="font-medium">
                        {formatPrice(trade.price * trade.quantity, trade.currency)}
                      </p>
                    </div>
                    <div className="flex justify-between text-sm text-muted-foreground">
                      <span>
                        {trade.ticker} · {trade.quantity}주 × {formatPrice(trade.price, trade.currency)}
                      </span>
                      <span>{formatDate(trade.tradeDate)}</span>
                    </div>
                  </li>
                ))}
              </ul>
            </CardContent>
          </Card>
        )}
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
