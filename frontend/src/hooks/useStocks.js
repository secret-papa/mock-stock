import { useQuery } from '@tanstack/react-query'
import { getStocks, searchStocks } from '@/api/stocks'

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
