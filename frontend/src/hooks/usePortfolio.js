import { useQuery } from '@tanstack/react-query'
import { getPortfolio } from '@/api/portfolio'
import { useExchangeRate } from './useExchangeRate'

export const usePortfolio = () => {
  const query = useQuery({
    queryKey: ['portfolio'],
    queryFn: getPortfolio,
  })

  const { data: exchangeRate = 1300 } = useExchangeRate()

  const holdings = query.data ?? []

  // 통화를 KRW로 환산
  const convertToKRW = (amount, currency) => {
    if (!currency || currency === 'null' || currency === 'KRW') {
      return amount
    }
    // USD 등 다른 통화는 환율 적용
    return amount * exchangeRate
  }

  // 총 주식 평가액 (KRW 기준)
  const stockValuationAmount = holdings.reduce((sum, h) => {
    const value = h.currentPrice * h.quantity
    return sum + convertToKRW(value, h.currency)
  }, 0)

  // 총 수익 (KRW 기준)
  const totalProfit = holdings.reduce((sum, h) => {
    return sum + convertToKRW(h.profit, h.currency)
  }, 0)

  return {
    ...query,
    holdings,
    stockValuationAmount,
    totalProfit,
    exchangeRate,
    convertToKRW,
  }
}
