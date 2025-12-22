import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Toaster } from '@/components/ui/sonner'
import HomePage from './pages/HomePage'
import LoginPage from './pages/LoginPage'
import SignupPage from './pages/SignupPage'
import MyPage from './pages/MyPage'
import StocksPage from './pages/StocksPage'
import StockDetailPage from './pages/StockDetailPage'
import PortfolioPage from './pages/PortfolioPage'
import TradesPage from './pages/TradesPage'
import ProtectedRoute from './components/ProtectedRoute'

function App() {
  return (
    <BrowserRouter>
      <Toaster position="top-center" richColors />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />
        <Route path="/stocks" element={<ProtectedRoute><StocksPage /></ProtectedRoute>} />
        <Route path="/stocks/:stockId" element={<ProtectedRoute><StockDetailPage /></ProtectedRoute>} />
        <Route path="/portfolio" element={<ProtectedRoute><PortfolioPage /></ProtectedRoute>} />
        <Route path="/trades" element={<ProtectedRoute><TradesPage /></ProtectedRoute>} />
        <Route path="/me" element={<ProtectedRoute><MyPage /></ProtectedRoute>} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
