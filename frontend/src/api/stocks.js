import { apiClient } from './client'

export const getStocks = async () => {
  const response = await apiClient.get('/api/stocks')
  return response.data
}

export const searchStocks = async (keyword) => {
  const response = await apiClient.get('/api/stocks/search', {
    params: { keyword },
  })
  return response.data
}

export const getStockChart = async (stockId, range = '1h') => {
  const response = await apiClient.get(`/api/stocks/${stockId}/chart`, {
    params: { range },
  })
  return response.data
}
