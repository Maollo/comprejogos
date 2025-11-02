import React from 'react';
import { Link as RouterLink } from 'react-router-dom'; // Supondo que você use react-router-dom
import { useAuth } from '../context/AuthContext';

// Importações do MUI
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import StorefrontIcon from '@mui/icons-material/Storefront'; // Ícone da loja
import AdminPanelSettingsIcon from '@mui/icons-material/AdminPanelSettings'; // Ícone de admin

export function Header() {
  const { isAuthenticated, user, logout } = useAuth();
return (
    // Box é um <div> com superpoderes
    <Box sx={{ flexGrow: 1 }}>
      {/* AppBar é a barra de navegação */}
      <AppBar position="static">
        <Toolbar>
          <StorefrontIcon sx={{ mr: 1 }} />
          {/* Typography é usado para textos */}
          <Typography 
            variant="h6" 
            component={RouterLink} // Faz o texto ser um Link
            to="/" 
            sx={{ flexGrow: 1, color: 'inherit', textDecoration: 'none' }}
          >
            CompreJogos
          </Typography>

          {/* Renderização condicional */}
          {isAuthenticated ? (
            <>
              {/* Mostra link de Admin se o usuário for admin */}
              {user?.roles?.some(role => role.name === 'ROLE_ADMIN') && (
                <Button 
                  color="inherit" 
                  component={RouterLink} 
                  to="/admin/games"
                  startIcon={<AdminPanelSettingsIcon />}
                >
                  Admin
                </Button>
              )}
              <Button color="inherit" component={RouterLink} to="/my-games">Meus Jogos</Button>
              <Button color="inherit" component={RouterLink} to="/my-orders">Pedidos</Button>
              <Button color="inherit" component={RouterLink} to="/profile">Perfil</Button>
              <Button color="inherit" component={RouterLink} to="/cart">Carrinho</Button>
              <Button color="inherit" onClick={logout}>Sair</Button>
            </>
          ) : (
            <>
              <Button color="inherit" component={RouterLink} to="/login">Entrar</Button>
              <Button color="inherit" component={RouterLink} to="/register">Cadastrar</Button>
            </>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}