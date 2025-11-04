// src/pages/CartPage.jsx
import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { toast } from 'react-toastify';
import LoadingSpinner from '../components/LoadingSpinner';
import { useNavigate } from 'react-router-dom';

// Importações do MUI
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import ListItemText from '@mui/material/ListItemText';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import Divider from '@mui/material/Divider';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import ShoppingCartCheckoutIcon from '@mui/icons-material/ShoppingCartCheckout';
import CardMedia from '@mui/material/CardMedia';



function CartPage() {
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(true);
const [checkoutLoading, setCheckoutLoading] = useState(false); // 2. NOVO ESTADO DE LOADING
  const navigate = useNavigate();

  const fetchCart = async () => {
    try {
      setLoading(true);
      const response = await api.get('/cart');
      setCart(response.data);
    } catch (error) {
      console.error("Erro ao buscar o carrinho:", error);
      toast.error("Não foi possível carregar seu carrinho.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  const handleRemoveItem = async (gameAppId) => {
    try {
      const response = await api.delete(`/cart/items/${gameAppId}`);
      setCart(response.data);
      toast.success('Item removido com sucesso!');
    } catch (error) {
      console.error('Erro ao remover item:', error);
      toast.error('Não foi possível remover o item.');
    }
  };
  const handleCheckout = async () => {
    setCheckoutLoading(true); // Inicia loading específico do botão
    try {
      // 1. Chama o endpoint de checkout (agora o real)
            const response = await api.post('/orders/checkout?paymentMethod=MERCADO_PAGO');
            const paymentInfo = response.data; // Este é o nosso PaymentInitiationDTO

            toast.info(`Pedido ${paymentInfo.orderId} criado. Redirecionando para o pagamento...`);

            // 2. **A MUDANÇA PRINCIPAL:** Redireciona o usuário para a página do Mercado Pago
            // O campo 'paymentDetails' agora contém a URL 'init_point'
            window.location.href = paymentInfo.paymentDetails;

    } catch (error) {
            console.error("Erro ao iniciar checkout:", error);
            const errorMessage = error.response?.data?.error || 'Falha ao iniciar a compra.';
            toast.error(errorMessage);
            setCheckoutLoading(false);
          }
          // Não precisamos do 'finally' aqui, pois o 'setCheckoutLoading(false)' só é necessário em caso de erro.
          // Se for bem-sucedido, o usuário será redirecionado.
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!cart || cart.items.length === 0) {
    return (
      <Container sx={{ py: 4, textAlign: 'center' }}>
        <Typography variant="h4" gutterBottom>Carrinho de Compras</Typography>
        <Typography>Seu carrinho está vazio.</Typography>
      </Container>
    );
  }

  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Carrinho de Compras
      </Typography>
      
      {/* Paper é um container com fundo claro */}
      <Paper elevation={3} sx={{ mb: 2 }}>
        <List>
          {cart.items.map((item, index) => (
            <React.Fragment key={item.gameAppId}>
              <ListItem
                secondaryAction={
                  <IconButton edge="end" aria-label="delete" onClick={() => handleRemoveItem(item.gameAppId)}>
                    <DeleteIcon />
                  </IconButton>
                }
              >
                <ListItemAvatar>
                  <CardMedia 
                    component="img" 
                    src={`${import.meta.env.VITE_API_BASE_URL}${item.gameImageUrl}` || 'https://via.placeholder.com/100'} 
                    alt={item.gameName}
                    sx={{ width: 80, height: 80, mr: 2 }}
                  />
                </ListItemAvatar>
                <ListItemText
                  primary={item.gameName}
                  secondary={
                    <Typography component="span">
                      {item.quantity} x R$ {item.price.toFixed(2)} = 
                      <Box component="strong" sx={{ ml: 1, color: 'text.primary' }}>
                        R$ {item.subtotal.toFixed(2)}
                      </Box>
                    </Typography>
                  }
                />
              </ListItem>
              {index < cart.items.length - 1 && <Divider variant="inset" component="li" />}
            </React.Fragment>
          ))}
        </List>
      </Paper>
      
      <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
        <Typography variant="h5" sx={{ mr: 2 }}>
          Total: 
          <Box component="strong" sx={{ ml: 1, color: 'green' }}>
             R$ {cart.total.toFixed(2)}
          </Box>
        </Typography>
        <Button 
          variant="contained" 
          color="success" 
          size="large"
          startIcon={<ShoppingCartCheckoutIcon />}
          onClick={handleCheckout} // A função agora redireciona
                    disabled={!cart || cart.items.length === 0 || checkoutLoading}
                  >
          {checkoutLoading ? 'Processando...' : 'Finalizar Compra'}
        </Button>
      </Box>
    </Container>
  );
}

export default CartPage;