// src/pages/HomePage.jsx
import React from 'react';
import { Link } from 'react-router-dom';

function HomePage() {
  return (
    <div style={{ padding: '2rem', textAlign: 'center' }}>
      <h1>Bem-vindo à CompreJogos!</h1>
      <p>A sua nova plataforma para descobrir e comprar jogos incríveis.</p>
      <Link to="/games">
        <button style={{ padding: '10px 20px', fontSize: '1rem', cursor: 'pointer' }}>
          Ver Catálogo de Jogos
        </button>
      </Link>
    </div>
  );
}

export default HomePage;