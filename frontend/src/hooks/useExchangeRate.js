import { useQuery } from '@tanstack/react-query'
import { getExchangeRate } from '@/api/exchange'

export const useExchangeRate = () => {
  return useQuery({
    queryKey: ['exchangeRate'],
    queryFn: getExchangeRate,
    staleTime: 1000 * 60 * 5, // 5분간 캐시
  })
}
