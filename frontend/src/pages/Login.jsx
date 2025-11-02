// Em um componente de Login (ex: src/pages/Login.jsx)
import api from '../services/api';

const handleLogin = async (email, password) => {
  try {
    const response = await api.post('/auth/login', { email, password });
    const token = response.data.token;

    // 1. Guarda o token no localStorage
    localStorage.setItem('authToken', token);

    // 2. Redireciona o usuário para a página principal
    window.location.href = '/';

  } catch (error) {
    console.error("Falha no login:", error);
    // Exibir mensagem de erro para o usuário
  }
  
  const handleLogout = () => {
  localStorage.removeItem('authToken');
  window.location.href = '/login';
};
};