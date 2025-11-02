// src/pages/GameDetailPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import LoadingSpinner from '../components/LoadingSpinner';

// Importações do MUI
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Chip from '@mui/material/Chip';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import CardMedia from '@mui/material/CardMedia';

function GameDetailPage() {
  const { appId } = useParams(); // Pega o 'appId' da URL
  const [game, setGame] = useState(null);
  const [loading, setLoading] = useState(true);
  
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchGame = async () => {
      try {
        setLoading(true);
        const response = await api.get(`/games/${appId}`);
        setGame(response.data);
      } catch (error) {
        console.error("Erro ao buscar detalhes do jogo:", error);
        toast.error("Não foi possível carregar o jogo.");
      } finally {
        setLoading(false);
      }
    };
    fetchGame();
  }, [appId]); // Roda de novo se o appId mudar

  const handleAddToCart = async (gameAppId) => {
    if (!isAuthenticated) {
      toast.warn('Você precisa estar logado para adicionar itens ao carrinho.');
      navigate('/login');
      return;
    }
    try {
      await api.post('/cart/items', { gameAppId: gameAppId, quantity: 1 });
      toast.success('Jogo adicionado ao carrinho!');
    } catch (error) {
      toast.error('Não foi possível adicionar o jogo ao carrinho.');
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!game) {
    return <Container sx={{ py: 4, textAlign: 'center' }}>
             <Typography variant="h5">Jogo não encontrado.</Typography>
           </Container>;
  }

  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom>
        {game.name}
      </Typography>
      
      <Grid container spacing={4}>
        {/* Coluna da Esquerda (Imagem e Ação) */}
        <Grid item xs={12} md={4}>
          <CardMedia
            component="img"
            image={`${import.meta.env.VITE_API_BASE_URL}${game.imageUrl}` || 'https://via.placeholder.com/300x140'}
            alt={game.name}
            sx={{ width: '100%', mb: 2, borderRadius: 1 }}
          />
          <Paper sx={{ p: 2 }}>
            <Typography variant="h4" color="green" gutterBottom>
              R$ {game.price?.toFixed(2)}
            </Typography>
            <Button
              variant="contained"
              color="success"
              size="large"
              startIcon={<AddShoppingCartIcon />}
              fullWidth
              onClick={() => handleAddToCart(game.appId)}
            >
              Adicionar ao Carrinho
            </Button>
            <Typography variant="body2" sx={{ mt: 2 }}><strong>Desenvolvedor:</strong> {game.developer}</Typography>
            <Typography variant="body2"><strong>Publicadora:</strong> {game.publisher}</Typography>
            <Typography variant="body2"><strong>Lançamento:</strong> {new Date(game.releaseDate).toLocaleDateString()}</Typography>
          </Paper>
        </Grid>
        
        {/* Coluna da Direita (Descrição e Requisitos) */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3, mb: 3 }}>
            <Typography variant="h5" gutterBottom>Descrição</Typography>
            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
              {game.description || "Nenhuma descrição disponível."}
            </Typography>
          </Paper>
          
          <Paper sx={{ p: 3 }}>
            <Typography variant="h5" gutterBottom>Requisitos de Sistema</Typography>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <Typography variant="h6">Mínimos</Typography>
                <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                  {game.minimumRequirements || "Não informado."}
                </Typography>
              </Grid>
              <Grid item xs={12} sm={6}>
                <Typography variant="h6">Recomendados</Typography>
                <Typography variant="body2" sx={{ whiteSpace: 'pre-wrap' }}>
                  {game.recommendedRequirements || "Não informado."}
                </Typography>
              </Grid>
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default GameDetailPage;