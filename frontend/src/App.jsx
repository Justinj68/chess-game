import './App.css'
import ChessBoard from './components/ChessBoard';
import { useState } from 'react';

function App() {
  const [gameStarted, setGameStarted] = useState(false);
  const handlePlayClick = () => {
    setGameStarted(true);
  };


  return (
    <div>
      <h1>Chess Game</h1>

      {!gameStarted ?
        (<button className="play-button" onClick={handlePlayClick}>PLAY</button>)
        :
        (<ChessBoard />)
      }
      
      <a 
        href="https://github.com/Justinj68" 
        target="_blank" 
        rel="noopener noreferrer" 
        className="github-link"
      >
        <img 
          src="https://img.icons8.com/ios11/512/FFFFFF/github.png" 
          alt="GitHub Logo"
          className="github-logo"
        />
        <span>Justinj68</span>
      </a>
    </div>
  );
}

export default App;