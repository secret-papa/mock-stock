import { apiClient } from './client'

export const getTrades = async () => {
  const response = await apiClient.get('/trades')
  return response.data
}

export const buyStock = async ({ ticker, quantity }) => {
  const response = await apiClient.post('/trades/buy', { ticker, quantity })
  return response.data
}

export const sellStock = async ({ ticker, quantity }) => {
  const response = await apiClient.post('/trades/sell', { ticker, quantity })
  return response.data
}
