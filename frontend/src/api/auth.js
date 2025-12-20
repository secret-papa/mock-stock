import { apiClient } from './client'

export const signUp = async ({ email, password, alias }) => {
  const response = await apiClient.post('/sign-up', {
    email,
    password,
    alias,
  })
  return response.data
}
