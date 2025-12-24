import { useQuery, useInfiniteQuery } from '@tanstack/react-query'
import { getStocks, getExchanges, searchStocks, getStockChart } from '@/api/stocks'

export const useStocks = (exchange) => {
  return useInfiniteQuery({
    queryKey: ['stocks', exchange],
    queryFn: ({ pageParam = 0 }) => getStocks({ page: pageParam, exchange }),
    getNextPageParam: (lastPage) => {
      if (lastPage.last) return undefined
      return lastPage.number + 1
    },
    initialPageParam: 0,
  })
}

export const useExchanges = () => {
  return useQuery({
    queryKey: ['exchanges'],
    queryFn: getExchanges,
  })
}

export const useSearchStocks = (keyword) => {
  return useQuery({
    queryKey: ['stocks', 'search', keyword],
    queryFn: () => searchStocks(keyword),
    enabled: !!keyword?.trim(),
  })
}

export const useStockChart = (stockId, range = '1h') => {
  return useQuery({
    queryKey: ['stocks', stockId, 'chart', range],
    queryFn: () => getStockChart(stockId, range),
    enabled: !!stockId,
  })
}
