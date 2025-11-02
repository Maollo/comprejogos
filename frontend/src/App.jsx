import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext'; 

import { Header } from './components/Header';
import { ProtectedRoute } from './components/ProtectedRoute';
import MyOrdersPage from './pages/MyOrdersPage'; 
import MyGamesPage from './pages/MyGamesPage'; 

import CssBaseline from '@mui/material/CssBaseline';
// Supondo que você tenha estes componentes de página
import HomePage from './pages/HomePage';
import GamesPage from './pages/GamesPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProfilePage from './pages/ProfilePage';
import CartPage from './pages/CartPage';
import GameDetailPage from './pages/GameDetailPage';

import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
    // 1. Envolva toda a aplicação com o AuthProvider
    <AuthProvider>
      <Router>
        <CssBaseline />
        <ToastContainer
          position="top-right" // Posição
          autoClose={3000}     // Fecha automaticamente após 3 segundos
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light" // ou "dark"
        />
        <Header /> {/* O cabeçalho agora tem acesso ao contexto */}
        <main>
          <Routes>
            {/* Rotas Públicas */}
            <Route path="/" element={<HomePage />} />
            <Route path="/games" element={<GamesPage />} />
            <Route path="/games/:appId" element={<GameDetailPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Rotas Protegidas */}
            <Route 
              path="/profile" 
              element={
                <ProtectedRoute>
                  <ProfilePage />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/cart" 
              element={
                <ProtectedRoute>
                  <CartPage />
                </ProtectedRoute>
              } 
            />
            <Route 
              path="/my-orders" 
              element={<ProtectedRoute><MyOrdersPage /></ProtectedRoute>} 
            />
            <Route 
              path="/my-games" 
              element={<ProtectedRoute><MyGamesPage /></ProtectedRoute>} 
            />
          </Routes>
        </main>
      </Router>
    </AuthProvider>
  );
}

export default App;