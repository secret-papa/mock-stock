import { useState, useMemo } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { ArrowLeft, Minus, Plus, TrendingUp, TrendingDown } from 'lucide-react'
import { AreaChart, Area, XAxis, YAxis, ResponsiveContainer, Tooltip } from 'recharts'
import { toast } from 'sonner'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Drawer, DrawerContent } from '@/components/ui/drawer'
import { useBuyStock, useSellStock } from '@/hooks/useTrades'
import { usePortfolio } from '@/hooks/usePortfolio'
import { useStockChart } from '@/hooks/useStocks'
import LoadingSpinner from '@/components/LoadingSpinner'

export default function StockDetailPage() {
  const location = useLocation()
  const navigate = useNavigate()
  const [quantity, setQuantity] = useState(1)
  const [drawerOpen, setDrawerOpen] = useState(false)
  const [orderType, setOrderType] = useState(null) // 'buy' or 'sell'
  const [chartRange, setChartRange] = useState('1h') // '1h', '1d', '1w', '1m'

  const { mutate: buyStock, isPending: isBuying } = useBuyStock()
  const { mutate: sellStock, isPending: isSelling } = useSellStock()
  const { holdings } = usePortfolio()

  // stock 또는 holding에서 데이터 가져오기
  const stock = location.state?.stock
  const holdingFromState = location.state?.holding

  // 현재 보유 중인지 확인
  const holding = holdingFromState || holdings.find(h => h.ticker === stock?.ticker)
  const hasHolding = !!holding && holding.quantity > 0

  // stock 정보 구성 (stock 또는 holding에서)
  const stockInfo = stock || (holding ? {
    id: holding.stockId,
    ticker: holding.ticker,
    name: holding.name,
    currentPrice: holding.currentPrice,
    currency: holding.currency,
  } : null)

  // 차트 데이터 조회
  const { data: chartHistory = [], isLoading: isChartLoading } = useStockChart(stockInfo?.id, chartRange)

  if (!stockInfo) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <p className="text-muted-foreground">종목 정보를 찾을 수 없습니다.</p>
      </div>
    )
  }

  // 기간별 설정
  const chartPeriods = [
    { key: '1h', label: '1시간' },
    { key: '1d', label: '1일' },
    { key: '1w', label: '1주' },
    { key: '1m', label: '1개월' },
  ]

  const chartData = useMemo(() => {
    if (!chartHistory.length) return []

    // 시간 포맷 결정
    const formatTime = (dateStr) => {
      const date = new Date(dateStr)
      switch (chartRange) {
        case '1h':
        case '1d':
          return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false })
        case '1w':
          return date.toLocaleDateString('ko-KR', { weekday: 'short' })
        case '1m':
          return date.toLocaleDateString('ko-KR', { month: 'numeric', day: 'numeric' })
        default:
          return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit', hour12: false })
      }
    }

    return chartHistory.map(item => ({
      time: formatTime(item.time),
      fullTime: new Date(item.time).toLocaleString('ko-KR'),
      price: item.price,
    }))
  }, [chartHistory, chartRange])

  // 가격 변동 계산 (첫번째와 마지막 데이터 비교)
  const firstPrice = chartData.length > 0 ? chartData[0].price : stockInfo.currentPrice
  const lastPrice = chartData.length > 0 ? chartData[chartData.length - 1].price : stockInfo.currentPrice
  const priceChange = lastPrice - firstPrice
  const priceChangePercent = firstPrice > 0 ? ((priceChange / firstPrice) * 100).toFixed(2) : 0
  const isPositive = priceChange >= 0

  const formatPrice = (price, currency) => {
    const validCurrency = currency && currency !== 'null' ? currency : 'USD'
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: validCurrency,
      maximumFractionDigits: 0,
    }).format(price)
  }

  const totalPrice = stockInfo.currentPrice * quantity
  const maxSellQuantity = holding?.quantity || 0

  const handleQuantityChange = (value) => {
    const num = parseInt(value, 10)
    if (!isNaN(num) && num >= 1) {
      if (orderType === 'sell' && num > maxSellQuantity) {
        setQuantity(maxSellQuantity)
      } else {
        setQuantity(num)
      }
    }
  }

  const openDrawer = (type) => {
    setOrderType(type)
    setQuantity(1)
    setDrawerOpen(true)
  }

  const handleBuy = () => {
    buyStock(
      { ticker: stockInfo.ticker, quantity },
      {
        onSuccess: () => {
          toast.success(`${stockInfo.name} ${quantity}주 매수 완료`)
          setDrawerOpen(false)
          navigate(-1)
        },
        onError: (error) => {
          toast.error(error.response?.data?.message || '매수 주문에 실패했습니다.')
        },
      }
    )
  }

  const handleSell = () => {
    sellStock(
      { ticker: stockInfo.ticker, quantity },
      {
        onSuccess: () => {
          toast.success(`${stockInfo.name} ${quantity}주 매도 완료`)
          setDrawerOpen(false)
          navigate(-1)
        },
        onError: (error) => {
          toast.error(error.response?.data?.message || '매도 주문에 실패했습니다.')
        },
      }
    )
  }

  const handleSellAll = () => {
    setQuantity(maxSellQuantity)
  }

  const isPending = isBuying || isSelling

  return (
    <div className="min-h-screen flex flex-col bg-background">
      <motion.main
        className="flex-1 px-4 pt-6 pb-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <button onClick={() => navigate(-1)} className="mb-4 p-1">
          <ArrowLeft className="w-5 h-5" />
        </button>

        <Card className="border-0 mb-4">
          <CardContent className="pt-5 pb-5">
            {/* 종목 정보 */}
            <div className="mb-4">
              <div className="flex items-center gap-2 mb-1">
                <p className="font-bold text-lg">{stockInfo.name}</p>
                <span className="text-sm text-muted-foreground">
                  {stockInfo.ticker}
                </span>
              </div>
              <div className="flex items-baseline gap-3">
                <p className="text-3xl font-bold">
                  {formatPrice(stockInfo.currentPrice, stockInfo.currency)}
                </p>
                <div className={`flex items-center gap-1 ${isPositive ? 'text-red-500' : 'text-blue-500'}`}>
                  {isPositive ? (
                    <TrendingUp className="w-4 h-4" />
                  ) : (
                    <TrendingDown className="w-4 h-4" />
                  )}
                  <span className="text-sm font-medium">
                    {isPositive ? '+' : ''}{formatPrice(priceChange, stockInfo.currency)} ({isPositive ? '+' : ''}{priceChangePercent}%)
                  </span>
                </div>
              </div>
            </div>

            {/* 기간 선택 탭 */}
            <div className="flex gap-1 mb-3">
              {chartPeriods.map((period) => (
                <button
                  key={period.key}
                  onClick={() => setChartRange(period.key)}
                  className={`px-3 py-1 rounded-full text-xs font-medium transition-colors ${
                    chartRange === period.key
                      ? 'bg-primary text-primary-foreground'
                      : 'bg-muted text-muted-foreground hover:bg-muted/80'
                  }`}
                >
                  {period.label}
                </button>
              ))}
            </div>

            {/* 차트 */}
            <div className="h-40 -mx-2">
              {isChartLoading ? (
                <LoadingSpinner className="h-full py-0" />
              ) : chartData.length > 0 ? (
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={chartData}>
                    <defs>
                      <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                        <stop
                          offset="5%"
                          stopColor={isPositive ? '#ef4444' : '#3b82f6'}
                          stopOpacity={0.3}
                        />
                        <stop
                          offset="95%"
                          stopColor={isPositive ? '#ef4444' : '#3b82f6'}
                          stopOpacity={0}
                        />
                      </linearGradient>
                    </defs>
                    <XAxis
                      dataKey="time"
                      hide
                    />
                    <YAxis
                      domain={['dataMin - 2', 'dataMax + 2']}
                      hide
                    />
                    <Tooltip
                      content={({ active, payload }) => {
                        if (active && payload && payload.length) {
                          return (
                            <div className="bg-white px-2 py-1 rounded shadow-lg border text-xs">
                              <p className="text-muted-foreground">{payload[0].payload.fullTime}</p>
                              <p className="font-medium">{formatPrice(payload[0].value, stockInfo.currency)}</p>
                            </div>
                          )
                        }
                        return null
                      }}
                    />
                    <Area
                      type="monotone"
                      dataKey="price"
                      stroke={isPositive ? '#ef4444' : '#3b82f6'}
                      strokeWidth={2}
                      fill="url(#colorPrice)"
                    />
                  </AreaChart>
                </ResponsiveContainer>
              ) : (
                <div className="h-full flex items-center justify-center text-muted-foreground text-sm">
                  해당 기간의 데이터가 없습니다
                </div>
              )}
            </div>
          </CardContent>
        </Card>

        {/* 보유 현황 (보유 중인 경우에만 표시) */}
        {hasHolding && (
          <Card className="border-0 mb-4">
            <CardContent className="pt-5 pb-5">
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">보유</span>
                  <span className="font-medium">{holding.quantity}주 · 평균 {formatPrice(holding.avgPrice, holding.currency)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">평가 손익</span>
                  <span className={`font-medium ${holding.profit >= 0 ? 'text-red-500' : 'text-blue-500'}`}>
                    {holding.profit >= 0 ? '+' : ''}{formatPrice(holding.profit, holding.currency)} ({holding.returnRate?.toFixed(2)}%)
                  </span>
                </div>
              </div>
            </CardContent>
          </Card>
        )}

        {/* 매수/매도 버튼 */}
        {hasHolding ? (
          <div className="flex gap-2">
            <Button
              onClick={() => openDrawer('buy')}
              className="flex-1 h-12 text-base font-medium"
            >
              매수
            </Button>
            <Button
              onClick={() => openDrawer('sell')}
              className="flex-1 h-12 text-base font-medium bg-rose-500 hover:bg-rose-600 text-white"
            >
              매도
            </Button>
          </div>
        ) : (
          <Button
            onClick={() => openDrawer('buy')}
            className="w-full h-12 text-base font-medium"
          >
            매수하기
          </Button>
        )}
      </motion.main>

      {/* 주문 바텀시트 */}
      <Drawer open={drawerOpen} onOpenChange={setDrawerOpen}>
        <DrawerContent className="bg-white">
          <div className="px-4 pt-6 pb-8">
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <p className="text-muted-foreground">수량</p>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                    disabled={quantity <= 1}
                  >
                    <Minus className="w-4 h-4" />
                  </Button>
                  <Input
                    type="number"
                    value={quantity}
                    onChange={(e) => handleQuantityChange(e.target.value)}
                    className="w-16 h-8 text-center bg-white border shadow-sm"
                    min="1"
                  />
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => setQuantity(quantity + 1)}
                    disabled={orderType === 'sell' && quantity >= maxSellQuantity}
                  >
                    <Plus className="w-4 h-4" />
                  </Button>
                  {orderType === 'sell' && hasHolding && (
                    <button
                      onClick={handleSellAll}
                      className="text-xs text-muted-foreground hover:text-foreground ml-1"
                    >
                      전량
                    </button>
                  )}
                </div>
              </div>

              <div className="flex items-center justify-between py-4 border-t">
                <p className="text-muted-foreground">총 {orderType === 'buy' ? '매수' : '매도'} 금액</p>
                <p className="text-xl font-bold">
                  {formatPrice(totalPrice, stockInfo.currency)}
                </p>
              </div>

              <Button
                onClick={orderType === 'buy' ? handleBuy : handleSell}
                disabled={isPending || (orderType === 'sell' && quantity > maxSellQuantity)}
                className={`w-full h-12 text-base font-medium ${
                  orderType === 'sell' ? 'bg-rose-500 hover:bg-rose-600' : ''
                }`}
              >
                {isPending ? '처리 중...' : orderType === 'buy' ? '매수하기' : '매도하기'}
              </Button>
            </div>
          </div>
        </DrawerContent>
      </Drawer>
    </div>
  )
}
