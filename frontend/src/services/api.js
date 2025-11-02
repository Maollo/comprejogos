import axios from 'axios';

// Cria uma instância do Axios com a URL base da sua API
const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// --- INTERCEPTOR: A MÁGICA DA AUTENTICAÇÃO ---
// Adiciona um interceptor que será executado ANTES de cada requisição.
api.interceptors.request.use(async config => {
  // Pega o token do localStorage (onde vamos guardá-lo após o login)
  const token = localStorage.getItem('authToken');
  if (token) {
    // Se o token existir, adiciona o cabeçalho Authorization
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

export default api;