import { apiClient } from './client'

export const signUp = async ({ email, password, alias }) => {
  const response = await apiClient.post('/auth/sign-up', {
    email,
    password,
    alias,
  })
  return response.data
}

export const login = async ({ email, password }) => {
  const response = await apiClient.post('/auth/login', {
    email,
    password,
  })
  return response.data
}

export const getMe = async () => {
  const response = await apiClient.get('/auth/me')
  return response.data
}
