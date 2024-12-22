import React, { useState } from 'react';
import { Button } from './components/ui/Button';
import { NavBar, Footer } from './components/PageComponents'
import './App.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function App() {
  const [isDarkMode, setIsDarkMode] = useState(true);
  const navigate = useNavigate();

  const toggleDarkMode = () => {
    setIsDarkMode(!isDarkMode);
    document.documentElement.classList.toggle('dark');
  };

  const mainClass = isDarkMode ? 'bg-neutral-950 text-gray-300' : 'bg-white text-gray-800';

  const handlePlay = async () => {
    try {
      const response = await axios.post('http://localhost:8080/api/play');
      const playerId = response.data.playerId;
      const gameId = response.data.gameId;

      localStorage.setItem('playerId', playerId);
      navigate(`/game/${gameId}`);
    } catch (error) {
      console.error('Error creating a new game:', error);
    }
  };

  return (
    <div className="min-h-screen transition-colors duration-500 flex flex-col">
      {/* Navigation */}
      <NavBar isDarkMode={isDarkMode} toggleDarkMode={toggleDarkMode} />

      {/* Section principale */}
      <main className={`flex flex-col justify-center items-center flex-grow py-12 text-center ${mainClass}`}>
        <h2 className="hover:scale-110 transition-all duration-1000 text-4xl font-bold mb-6">
          Welcome to Chess Game!
        </h2>
        <p className="text-xl mb-8 max-w-2xl">
          Experience the classic game of chess with a modern twist. Challenge AI opponents, play
          with friends, and improve your skills.
        </p>
        <div className="flex space-x-4">
          <Button size="md" className="w-100 hover:bg-green-500 hover:scale-110" onClick={handlePlay}>
            PLAY
          </Button>
        </div>
      </main>

      {/* Footer */}
      <Footer isDarkMode={isDarkMode} />
    </div>
  );
}

export default App;
