import { useState } from 'react'
import { Link } from 'react-router-dom'
import { motion } from 'framer-motion'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { useLogin } from '@/hooks/useLogin'

export default function LoginPage() {
  const { mutate: login, isPending, error } = useLogin()
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  })

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    login({
      email: formData.email,
      password: formData.password,
    })
  }

  return (
    <div className="min-h-screen flex flex-col bg-background">
      <motion.div
        className="flex-1 flex flex-col px-6 pt-16 pb-8"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.4 }}
      >
        <div className="mb-12">
          <h1 className="text-3xl font-bold">로그인</h1>
          <p className="text-muted-foreground mt-2">
            계정에 로그인하여 서비스를 이용하세요
          </p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6 flex-1">
          <div className="space-y-2">
            <Label htmlFor="email" className="text-base">이메일</Label>
            <Input
              id="email"
              name="email"
              type="email"
              placeholder="example@email.com"
              value={formData.email}
              onChange={handleChange}
              required
              className="h-12 text-base"
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password" className="text-base">비밀번호</Label>
            <Input
              id="password"
              name="password"
              type="password"
              placeholder="비밀번호를 입력하세요"
              value={formData.password}
              onChange={handleChange}
              required
              className="h-12 text-base"
            />
          </div>
        </form>

        <div className="space-y-4 mt-auto pt-8">
          {error && (
            <p className="text-sm text-red-500 text-center">
              {error.response?.data?.message || '로그인에 실패했습니다.'}
            </p>
          )}
          <Button
            type="submit"
            className="w-full h-12 text-base"
            onClick={handleSubmit}
            disabled={isPending}
          >
            {isPending ? '로그인 중...' : '로그인'}
          </Button>
          <p className="text-center text-sm text-muted-foreground">
            계정이 없으신가요?{' '}
            <Link to="/signup" className="text-primary font-medium hover:underline">
              회원가입
            </Link>
          </p>
        </div>
      </motion.div>
    </div>
  )
}
