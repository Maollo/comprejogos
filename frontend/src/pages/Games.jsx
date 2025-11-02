import React, { useState, useEffect } from 'react';
import api from '../services/api';

function GamesPage() {
  const [games, setGames] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Função para buscar os jogos na API
    const fetchGames = async () => {
      try {
        setLoading(true);
        const response = await api.get('/games');
        setGames(response.data);
      } catch (error) {
        console.error("Erro ao buscar jogos:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchGames();
  }, []); // O array vazio [] faz com que isso rode apenas uma vez, quando o componente montar

  if (loading) return <p>Carregando jogos...</p>;

  return (
    <div>
      <h1>Catálogo de Jogos</h1>
      <div className="game-list">
        {games.map(game => (
          <div key={game.appId}>
            <img src={game.imageUrl} alt={game.name} />
            <h2>{game.name}</h2>
            <p>R$ {game.price}</p>
            <button>Adicionar ao Carrinho</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default GamesPage;