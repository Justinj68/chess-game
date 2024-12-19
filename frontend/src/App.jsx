import './App.css';
import ChessBoard from './components/ChessBoard';
import { useState } from 'react';
import axios from 'axios';

function App() {
  const playerId = crypto.randomUUID();
  console.log(playerId);
  const [gameId, setGameId] = useState(null);

  const handlePlayClick = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/play', {
        playerId: playerId
      });
      setGameId(response.data.id);  // Récupère l'ID de la partie après sa création
    } catch (error) {
      console.error('Error creating a new game:', error);
    }
  };
  

  return (
    <div>
      <h1>Chess Game</h1>
      {!gameId ? (
        <button className="play-button" onClick={handlePlayClick}>
          PLAY
        </button>
      ) : (
        <ChessBoard gameId={gameId} />
      )}
    </div>
  );
}

export default App;