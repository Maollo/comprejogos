import React, { createContext, useState, useEffect, useContext } from 'react';
import api from '../services/api'; // O serviço Axios que criamos

// 1. Cria o Contexto
const AuthContext = createContext({});

// 2. Cria o Componente Provedor
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // Começa como true para verificar o token

  // Roda uma única vez quando a aplicação carrega
  useEffect(() => {
    async function loadUserFromToken() {
      const token = localStorage.getItem('authToken');

      if (token) {
        try {
          // O token já é enviado automaticamente pelo interceptor do Axios
          const response = await api.get('/users/me');
          setUser(response.data);
        } catch (error) {
          // Se o token for inválido/expirado, limpa o localStorage
          console.error("Sessão inválida, limpando token.");
          localStorage.removeItem('authToken');
        }
      }
      setLoading(false); // Termina o carregamento inicial
    }

    loadUserFromToken();
  }, []);

  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      const { token } = response.data;
      
      localStorage.setItem('authToken', token);
      
      // Busca os dados do usuário após o login e atualiza o estado
      const userResponse = await api.get('/users/me');
      setUser(userResponse.data);

    } catch (error) {
      console.error("Falha no login", error);
      throw error; // Lança o erro para o componente de login poder tratá-lo
    }
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setUser(null);
  };

  const authContextValue = {
    isAuthenticated: !!user, // !!user converte o objeto (ou null) para um booleano
    user,
    loading,
    login,
    logout,
  };

  // 3. Fornece o valor para todos os componentes filhos
  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  );
};

// 4. Cria um hook customizado para facilitar o uso do contexto
export const useAuth = () => {
  return useContext(AuthContext);
};