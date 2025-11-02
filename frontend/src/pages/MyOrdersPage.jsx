// src/pages/MyOrdersPage.jsx
import React, { useState, useEffect } from 'react';
import api from '../services/api';
import LoadingSpinner from '../components/LoadingSpinner';
import { toast } from 'react-toastify'; // Importe toast para erros

// --- Importações do Material-UI ---
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Accordion from '@mui/material/Accordion';
import AccordionSummary from '@mui/material/AccordionSummary';
import AccordionDetails from '@mui/material/AccordionDetails';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import Box from '@mui/material/Box';
import List from '@mui/material/List';         // Garanta esta importação
import ListItem from '@mui/material/ListItem';   // Garanta esta importação
import ListItemText from '@mui/material/ListItemText'; // Garanta esta importação
// --- Fim das Importações MUI ---

function MyOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        const response = await api.get('/orders/my-orders');
        setOrders(response.data);
      } catch (error) {
        console.error("Erro ao buscar pedidos:", error);
        toast.error("Não foi possível carregar seus pedidos."); // Adicione feedback de erro
      } finally {
        setLoading(false);
      }
    };
    fetchOrders();
  }, []);

  if (loading) {
    return <LoadingSpinner />;
  }

  if (orders.length === 0) {
    return (
      <Container sx={{ py: 4, textAlign: 'center' }}>
        <Typography variant="h4" gutterBottom>Meus Pedidos</Typography>
        <Typography>Você ainda não fez nenhum pedido.</Typography>
      </Container>
    );
  }

  return (
    <Container sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Meus Pedidos
      </Typography>
      {orders.map(order => (
        <Accordion key={order.id} sx={{ mb: 1 }}>
          <AccordionSummary
            expandIcon={<ExpandMoreIcon />}
            aria-controls={`panel-${order.id}-content`}
            id={`panel-${order.id}-header`}
          >
            <Box sx={{ display: 'flex', justifyContent: 'space-between', width: '100%', alignItems: 'center', flexWrap: 'wrap', pr: 2 }}> {/* Adicionado flexWrap */}
              <Typography variant="subtitle1" sx={{ mr: 2 }}>Pedido Nº: {order.id}</Typography>
              <Typography sx={{ mr: 2 }}>Data: {new Date(order.createdAt).toLocaleDateString()}</Typography>
              <Typography sx={{ mr: 2 }}>Status: {order.status}</Typography>
              <Typography variant="subtitle1" color="green">Total: R$ {order.total != null ? order.total.toFixed(2) : 'N/A'}</Typography> {/* Verificação de null */}
            </Box>
          </AccordionSummary>
          <AccordionDetails>
            <Typography variant="h6" gutterBottom>Itens do Pedido:</Typography>
            {/* Verifica se order.items é um array antes de mapear */}
            {Array.isArray(order.items) ? (
              <List>
                {order.items.map(item => (
                  <ListItem key={item.gameAppId}>
                    <ListItemText 
                      primary={item.gameName || 'Nome Indisponível'} // Fallback
                      secondary={`Qtd: ${item.quantity} - Preço Pago: R$ ${item.price != null ? item.price.toFixed(2) : 'N/A'}`} // Verificação de null
                    />
                  </ListItem>
                ))}
              </List>
            ) : (
              <Typography color="error">Erro ao carregar itens do pedido.</Typography> // Mensagem se items não for array
            )}
          </AccordionDetails>
        </Accordion>
      ))}
    </Container>
  );
}

export default MyOrdersPage;