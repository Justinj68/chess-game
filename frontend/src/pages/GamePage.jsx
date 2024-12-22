import ChessBoard from "../components/ChessBoard";
import { NavBar, Footer } from "../components/PageComponents";
import "../App.css"
import { useParams } from 'react-router-dom';
import React, { useState } from "react";

function GamePage() {
    const { gameId } = useParams();
    const [isDarkMode, setIsDarkMode] = useState(true);

    const toggleDarkMode = () => {
      setIsDarkMode(!isDarkMode);
      document.documentElement.classList.toggle('dark');
    };
  
    const mainClass = isDarkMode ? 'bg-neutral-950 text-gray-300' : 'bg-white text-gray-800';
  
    return (
      <div className="min-h-screen transition-colors duration-500 flex flex-col">
        <NavBar isDarkMode={isDarkMode} toggleDarkMode={toggleDarkMode} />

        <main className={`flex flex-col justify-center items-center flex-grow py-12 text-center ${mainClass}`}>
          <div className="relative w-full h-full">
              <ChessBoard gameId={gameId}/>
          </div>
        </main>

        <Footer isDarkMode={isDarkMode} />
      </div>
    );
}

export default GamePage;