import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getTrades, buyStock, sellStock } from '@/api/trades'

export const useTrades = () => {
  return useQuery({
    queryKey: ['trades'],
    queryFn: getTrades,
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
