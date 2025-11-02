import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();

  // 1. Enquanto estiver verificando o token, mostra uma mensagem de carregamento.
  // Isso é CRUCIAL para evitar que o usuário seja redirecionado para /login antes da verificação terminar.
  if (loading) {
    return <div>Carregando sua sessão...</div>;
  }

  // 2. Se não estiver autenticado após a verificação, redireciona para o login.
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // 3. Se estiver autenticado, renderiza a página solicitada.
  return children;
}