import { useInfiniteQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getTrades, buyStock, sellStock } from '@/api/trades'

export const useTrades = () => {
  return useInfiniteQuery({
    queryKey: ['trades'],
    queryFn: ({ pageParam = 0 }) => getTrades({ page: pageParam }),
    getNextPageParam: (lastPage) => {
      if (lastPage.last) return undefined
      return lastPage.number + 1
    },
    initialPageParam: 0,
  })
}

export const useBuyStock = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: buyStock,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trades'] })
      queryClient.invalidateQueries({ queryKey: ['portfolio'] })
      queryClient.invalidateQueries({ queryKey: ['me'] })
    },
  })
}

export const useSellStock = () => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: sellStock,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['trades'] })
      queryClient.invalidateQueries({ queryKey: ['portfolio'] })
      queryClient.invalidateQueries({ queryKey: ['me'] })
    },
  })
}
