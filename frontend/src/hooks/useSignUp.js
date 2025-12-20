import { useMutation } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import { signUp } from '@/api/auth'

export const useSignUp = () => {
  const navigate = useNavigate()

  return useMutation({
    mutationFn: signUp,
    onSuccess: () => {
      navigate('/login')
    },
  })
}
