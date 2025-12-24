import { apiClient } from './client'

export const getExchangeRate = async () => {
  const response = await apiClient.get('/api/exchange')
  return response.data
}
