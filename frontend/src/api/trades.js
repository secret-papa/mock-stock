import { apiClient } from './client'

export const getTrades = async ({ page = 0, size = 20 }) => {
  const response = await apiClient.get('/trades', {
    params: { page, size },
  })
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
