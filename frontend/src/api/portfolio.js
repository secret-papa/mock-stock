import { apiClient } from './client'

export const getPortfolio = async () => {
  const response = await apiClient.get('/portfolio')
  return response.data
}
