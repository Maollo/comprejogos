// src/pages/MyGamesPage.jsx
import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import api from '../services/api'; // Verifique o caminho
import LoadingSpinner from '../components/LoadingSpinner'; // Verifique o caminho
import { toast } from 'react-toastify'; // Para mensagens de erro

// --- Importações do Material-UI ---
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import CardActionArea from '@mui/material/CardActionArea';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import DownloadIcon from '@mui/icons-material/Download';
import Link from '@mui/material/Link'; // Importe o Link do MUI para usar com RouterLink
// --- Fim das Importações MUI ---

function MyGamesPage() {
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyGames = async () => {
      try {
        setLoading(true);
        const response = await api.get('/users/me/games');
        // Verifica se a resposta contém um array antes de atualizar o estado
        if (Array.isArray(response.data)) {
          setGames(response.data);
        } else {
          console.error("Erro: A resposta da API não é um array:", response.data);
          setGames([]); // Define como vazio para evitar erros de map
          toast.error("Formato inesperado recebido da API.");
        }
      } catch (error) {
        console.error("Erro ao buscar biblioteca de jogos:", error);
        toast.error("Não foi possível carregar sua biblioteca.");
      } finally {
        setLoading(false);
      }
    };
    fetchMyGames();
  }, []); // Array vazio para rodar apenas uma vez

  // Enquanto carrega, mostra o spinner
  if (loading) {
    return <LoadingSpinner />;
  }

  // Se, após carregar, não houver jogos, mostra a mensagem
  if (games.length === 0) {
    return (
      <Container sx={{ py: 4, textAlign: 'center' }}>
        <Typography variant="h4" gutterBottom>Meus Jogos</Typography>
        <Typography>
          Sua biblioteca está vazia. 
          {/* Use o Link do MUI combinado com o RouterLink */}
          <Link component={RouterLink} to="/games" sx={{ ml: 1 }}>
            Compre alguns jogos!
          </Link>
        </Typography>
      </Container>
    );
  }

  // Se houver jogos, renderiza a lista
  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Meus Jogos
      </Typography>
      <Grid container spacing={3}>
        {games.map(userGame => (
          <Grid item key={userGame.gameAppId} xs={12} sm={6} md={4} lg={3}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
              <CardActionArea
                component={RouterLink}
                to={`/games/${userGame.gameAppId}`} // Link para a página de detalhes
              >
                <CardMedia
                  component="img"
                  height="140"
                  // Constrói a URL completa e tem fallback
                  image={userGame.gameImageUrl ? `${import.meta.env.VITE_API_BASE_URL}${userGame.gameImageUrl}` : 'https://via.placeholder.com/300x140'}
                  alt={userGame.gameName || 'Jogo'}
                  onError={(e) => { e.target.onerror = null; e.target.src='https://via.placeholder.com/300x140'; }}
                />
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography gutterBottom variant="h6" component="h2" noWrap title={userGame.gameName || ''}>
                    {userGame.gameName || 'Nome Indisponível'}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {userGame.gameDeveloper || 'Desenvolvedor Desconhecido'}
                  </Typography>
                  {/* Verifica se a data existe antes de formatar */}
                  {userGame.purchaseDate && (
                    <Typography variant="caption" color="text.secondary">
                      Comprado em: {new Date(userGame.purchaseDate).toLocaleDateString()}
                    </Typography>
                  )}
                </CardContent>
              </CardActionArea>
              <CardActions>
                <Button 
                  size="small" 
                  variant="contained" 
                  startIcon={<DownloadIcon />}
                  // Adicione a lógica de download aqui se necessário
                  // onClick={() => handleDownload(userGame.gameAppId)} 
                >
                  Baixar
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default MyGamesPage;