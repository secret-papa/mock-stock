import { apiClient } from './client'

export const getExchangeRate = async () => {
  const response = await apiClient.get('/exchange')
  return response.data
}
