import { useQuery } from '@tanstack/react-query'
import { getStocks, searchStocks, getStockChart } from '@/api/stocks'

export const useStocks = () => {
  return useQuery({
    queryKey: ['stocks'],
    queryFn: getStocks,
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
