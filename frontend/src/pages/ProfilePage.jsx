// src/pages/ProfilePage.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';
import { toast } from 'react-toastify';

function ProfilePage() {
  const { user, loading: authLoading } = useAuth();
  const [formData, setFormData] = useState({
    avatarUrl: '',
    birthDate: '',
    phone: '',
    country: '',
    city: '',
  });
  //const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  // useEffect para preencher o formulário quando os dados do usuário estiverem disponíveis
  useEffect(() => {
    if (user) {
      setFormData({
        avatarUrl: user.avatarUrl || '',
        birthDate: user.birthDate || '',
        phone: user.phone || '',
        country: user.country || '',
        city: user.city || '',
      });
    }
  }, [user]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    
    try {
      // O endpoint PUT que criamos no backend
      await api.put('/users/me', formData);
     toast.success('Perfil atualizado com sucesso!');
    } catch (error) {
      toast.error('Erro ao atualizar o perfil. Tente novamente.');
      console.error('Falha ao atualizar perfil:', error);
    } finally {
      setLoading(false);
    }
  };

  if (authLoading) {
    return <div style={{ padding: '2rem' }}>Carregando perfil...</div>;
  }

  return (
    <div style={{ padding: '2rem', maxWidth: '600px', margin: 'auto' }}>
      <h1>Meu Perfil</h1>
      <div style={{ marginBottom: '1.5rem', background: '#f4f4f4', padding: '1rem' }}>
        <p><strong>Login:</strong> {user?.login}</p>
        <p><strong>Email:</strong> {user?.email}</p>
      </div>

      <form onSubmit={handleSubmit}>
        <h3>Editar Informações</h3>
        
        <div style={{ marginBottom: '1rem' }}>
          <label>URL do Avatar</label>
          <input type="text" name="avatarUrl" value={formData.avatarUrl} onChange={handleChange} style={{ width: '100%', padding: '8px' }} />
        </div>
        
        <div style={{ marginBottom: '1rem' }}>
          <label>Data de Nascimento</label>
          <input type="date" name="birthDate" value={formData.birthDate} onChange={handleChange} style={{ width: '100%', padding: '8px' }} />
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>Telefone</label>
          <input type="text" name="phone" value={formData.phone} onChange={handleChange} style={{ width: '100%', padding: '8px' }} />
        </div>

        <div style={{ marginBottom: '1rem' }}>
          <label>País</label>
          <input type="text" name="country" value={formData.country} onChange={handleChange} style={{ width: '100%', padding: '8px' }} />
        </div>
        
        <div style={{ marginBottom: '1rem' }}>
          <label>Cidade</label>
          <input type="text" name="city" value={formData.city} onChange={handleChange} style={{ width: '100%', padding: '8px' }} />
        </div>

        <button type="submit" disabled={loading} style={{ width: '100%', padding: '10px' }}>
          {loading ? 'Salvando...' : 'Salvar Alterações'}
        </button>

        
      </form>
    </div>
  );
}

export default ProfilePage;