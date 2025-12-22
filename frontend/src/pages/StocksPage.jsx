import { useState, useMemo, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Search } from 'lucide-react'
import { Input } from '@/components/ui/input'
import { Card, CardContent } from '@/components/ui/card'
import BottomAppBar from '@/components/BottomAppBar'
import LoadingSpinner from '@/components/LoadingSpinner'
import { useStocks, useSearchStocks } from '@/hooks/useStocks'

export default function StocksPage() {
  const navigate = useNavigate()
  const [searchQuery, setSearchQuery] = useState('')
  const [debouncedQuery, setDebouncedQuery] = useState('')
  const [selectedExchange, setSelectedExchange] = useState('전체')

  const { data: allStocks = [], isLoading: isLoadingAll } = useStocks()
  const { data: searchResults = [], isLoading: isSearching } = useSearchStocks(debouncedQuery)

  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedQuery(searchQuery.trim())
    }, 300)
    return () => clearTimeout(timer)
  }, [searchQuery])

  const isSearchMode = !!debouncedQuery
  const stocks = isSearchMode ? searchResults : allStocks
  const isLoading = isSearchMode ? isSearching : isLoadingAll

  const exchanges = useMemo(() => {
    const uniqueExchanges = [...new Set(allStocks.map((stock) => stock.exchange))]
    return ['전체', ...uniqueExchanges]
  }, [allStocks])

  const isValidStock = (stock) => {
    return (
      stock.id != null &&
      stock.name &&
      stock.name !== 'Unkwon' &&
      stock.currentPrice > 0
    )
  }

  const filteredStocks = useMemo(() => {
    return stocks
      .filter(isValidStock)
      .filter((stock) => {
        return selectedExchange === '전체' || stock.exchange === selectedExchange
      })
  }, [stocks, selectedExchange])

  const formatPrice = (price, currency) => {
    const validCurrency = currency && currency !== 'null' ? currency : 'USD'
    return new Intl.NumberFormat('ko-KR', {
      style: 'currency',
      currency: validCurrency,
      maximumFractionDigits: 0,
    }).format(price)
  }

  return (
    <div className="min-h-screen flex flex-col bg-background pb-16">
      <motion.main
        className="flex-1 px-4 pt-6"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <h2 className="text-xl font-bold mb-4 px-2">종목</h2>

        <div className="relative mb-4">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
          <Input
            type="text"
            placeholder="종목명 또는 종목 코드 검색"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9 bg-white border-0 shadow-sm"
          />
        </div>

        <div className="flex gap-2 overflow-x-auto pb-3 mb-2 scrollbar-hide">
          {exchanges.map((exchange) => (
            <button
              key={exchange}
              onClick={() => setSelectedExchange(exchange)}
              className={`px-3 py-1.5 rounded-full text-sm whitespace-nowrap transition-colors ${
                selectedExchange === exchange
                  ? 'bg-primary text-primary-foreground'
                  : 'bg-white text-muted-foreground shadow-sm hover:bg-muted'
              }`}
            >
              {exchange}
            </button>
          ))}
        </div>

        <Card className="border-0">
          <CardContent className="p-0">
            {isLoading ? (
              <LoadingSpinner />
            ) : filteredStocks.length === 0 ? (
              <div className="py-8 text-center text-muted-foreground">
                검색 결과가 없습니다
              </div>
            ) : (
              <ul className="divide-y">
                {filteredStocks.map((stock) => (
                  <li
                    key={stock.id}
                    onClick={() => navigate(`/stocks/${stock.id}`, { state: { stock } })}
                    className="flex items-center justify-between px-4 py-3 hover:bg-muted/50 transition-colors cursor-pointer"
                  >
                    <div>
                      <p className="font-medium">{stock.name}</p>
                      <p className="text-sm text-muted-foreground">
                        {stock.ticker} · {stock.exchange}
                      </p>
                    </div>
                    <p className="font-medium">{formatPrice(stock.currentPrice, stock.currency)}</p>
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>
      </motion.main>

      <BottomAppBar />
    </div>
  )
}
