import { apiClient } from './client'

export const getPortfolio = async () => {
  const response = await apiClient.get('/api/portfolio')
  return response.data
}
