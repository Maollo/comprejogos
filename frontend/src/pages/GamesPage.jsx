// src/pages/GamesPage.jsx
import React, { useState, useEffect,useCallback } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import api from '../services/api'; // Verifique se o caminho está correto
import { useAuth } from '../context/AuthContext'; // Verifique se o caminho está correto
import { toast } from 'react-toastify';
import LoadingSpinner from '../components/LoadingSpinner'; // Verifique se o caminho está correto

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
import Pagination from '@mui/material/Pagination';
import Box from '@mui/material/Box';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import TextField from '@mui/material/TextField'; 
import InputAdornment from '@mui/material/InputAdornment'; 
import SearchIcon from '@mui/icons-material/Search';
// --- Fim das Importações do MUI ---
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}




function GamesPage() {
  const [pageData, setPageData] = useState(null); // Armazena a resposta completa da API (inclui 'content', 'totalPages', etc.)
  const [page, setPage] = useState(1); // Página atual (MUI usa 1-based index)
  const [loading, setLoading] = useState(true);
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const pageSize = 12; // Número de jogos por página

  const [searchTerm, setSearchTerm] = useState(''); // Estado para o texto da busca
  const [tagFilter, setTagFilter] = useState('');   // Estado para o filtro de tag (simplificado)

  const fetchGames = useCallback(async (currentPage, currentSearch, currentTag) => {
    console.log(`[DEBUG] 1. Buscando jogos... Página (0-based): ${currentPage - 1}, Busca: '${currentSearch}', Tag: '${currentTag}'`);
    try {
      setLoading(true);
      
      // Constrói os parâmetros da URL dinamicamente
      const params = new URLSearchParams({
        page: currentPage - 1,
        size: pageSize,
        sort: 'name,asc',
      });
      if (currentSearch) {
        params.append('search', currentSearch);
      }
      if (currentTag) {
        params.append('tag', currentTag);
      }
      
      const response = await api.get(`/games?${params.toString()}`);
      console.log('[DEBUG] 2. Busca concluída com sucesso. Dados recebidos:', response.data);
      setPageData(response.data);
    } catch (error) {
      console.error('[DEBUG] 3. ERRO na busca de jogos:', error);
      toast.error("Não foi possível carregar os jogos.");
    } finally {
      setLoading(false);
    }
  }, [pageSize]);
  useEffect(() => {
    // Usamos uma função debounced para a busca, para não chamar a API a cada tecla
    const debouncedFetch = debounce(() => {
        fetchGames(page, searchTerm, tagFilter);
    }, 500); // Espera 500ms após o usuário parar de digitar

    debouncedFetch();

    // Cleanup function para cancelar o timeout se o componente desmontar
    return () => clearTimeout(debouncedFetch);

  }, [page, searchTerm, tagFilter, fetchGames]);


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
      console.error('Erro ao adicionar ao carrinho:', error);
      toast.error('Não foi possível adicionar o jogo ao carrinho.');
    }
  };

  const handlePageChange = (event, value) => {
    setPage(value); // Atualiza o estado da página atual
  };
// --- NOVOS HANDLERS PARA OS INPUTS ---
  const handleSearchChange = (event) => {
    setSearchTerm(event.target.value);
    setPage(1); // Volta para a primeira página ao fazer nova busca
  };

  const handleTagChange = (event) => { // Handler simplificado para TextField de tag
    setTagFilter(event.target.value);
    setPage(1); // Volta para a primeira página ao filtrar
  };
  // --- FIM DOS NOVOS HANDLERS ---
  // Log de renderização
  console.log(`[DEBUG] 4. Renderizando... Loading: ${loading}, PageData:`, pageData);

  // Renderiza o spinner enquanto os dados estão sendo carregados
  if (loading) {
    return <LoadingSpinner />;
  }

  // Renderiza mensagem se não houver jogos (após o carregamento)
  if (!pageData || !pageData.content || pageData.totalElements === 0) {
    console.log('[DEBUG] 5. Renderizando: "Nenhum jogo encontrado."');
    return (
      <Container sx={{ py: 4, textAlign: 'center' }}>
        <Typography variant="h5">Nenhum jogo encontrado.</Typography>
      </Container>
    );
  }

  // Renderiza a lista de jogos se houver dados
  console.log(`[DEBUG] 6. Renderizando ${pageData.content.length} jogos.`);

  const noGamesFound = !loading && (!pageData || !pageData.content || pageData.totalElements === 0);
  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Catálogo de Jogos
      </Typography>

      {/* --- ADICIONA A BARRA DE BUSCA E FILTRO --- */}
      <Box sx={{ display: 'flex', gap: 2, mb: 3 }}> {/* mb: 3 = margin bottom */}
        <TextField
          label="Buscar por nome"
          variant="outlined"
          fullWidth // Ocupa a maior parte do espaço
          value={searchTerm}
          onChange={handleSearchChange}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
        />
        <TextField // Usando TextField simples para tag por enquanto
            label="Filtrar por Tag"
            variant="outlined"
            value={tagFilter}
            onChange={handleTagChange}
            sx={{ minWidth: 150 }} // Largura mínima
        />
        {/* Futuramente, substituir por Select ou Autocomplete para tags */}
      </Box>
      {/* --- FIM DA BARRA DE BUSCA E FILTRO --- */}

      {/* Mostra spinner durante carregamento de novas páginas/filtros */}
      {loading && <LoadingSpinner />} 
      
      {/* Mostra mensagem se nenhum jogo for encontrado */}
      {noGamesFound && (
        <Container sx={{ py: 4, textAlign: 'center' }}>
            <Typography variant="h5">Nenhum jogo encontrado com os filtros aplicados.</Typography>
        </Container>
      )}

      {/* Renderiza o Grid apenas se houver jogos */}
      {!loading && pageData && pageData.content && pageData.totalElements > 0 && (
          <Grid container spacing={3}>
            {pageData.content.map(game => (
                <Grid item key={game.appId} xs={12} sm={6} md={4} lg={3}>
                    {/* ... (Seu Card MUI para o jogo - sem alterações aqui) ... */}
                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
                        <CardActionArea 
                          component={RouterLink} 
                          to={`/games/${game.appId}`}
                        >
                          <CardMedia
                            component="img"
                            height="140"
                            image={game.imageUrl ? `${import.meta.env.VITE_API_BASE_URL}${game.imageUrl}` : 'https://via.placeholder.com/300x140'}
                            alt={game.name}
                            onError={(e) => { e.target.onerror = null; e.target.src='https://via.placeholder.com/300x140'; }} 
                          />
                          <CardContent sx={{ flexGrow: 1 }}>
                            <Typography gutterBottom variant="h6" component="h2" noWrap title={game.name}> 
                              {game.name}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {game.developer}
                            </Typography>
                            <Typography variant="h5" color="text.primary" sx={{ mt: 1 }}>
                              R$ {game.price != null ? game.price.toFixed(2) : 'N/A'}
                            </Typography>
                          </CardContent>
                        </CardActionArea>
                        <CardActions>
                          <Button
                            size="small"
                            variant="contained"
                            startIcon={<AddShoppingCartIcon />}
                            onClick={() => handleAddToCart(game.appId)}
                          >
                            Adicionar
                          </Button>
                        </CardActions>
                      </Card>
                </Grid>
            ))}
          </Grid>
      )}

      {/* Renderiza a Paginação apenas se houver mais de uma página */}
      {!loading && pageData && pageData.totalPages > 1 && (
        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
          <Pagination
            count={pageData.totalPages}
            page={page}
            onChange={handlePageChange}
            color="primary"
            size="large"
            showFirstButton
            showLastButton
          />
        </Box>
      )}
    </Container>
  );
}

export default GamesPage;