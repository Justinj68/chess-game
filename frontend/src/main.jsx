import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import App from './App';
import GamePage from './pages/GamePage'; // Assurez-vous que GamePage est défini
import LoginPage from './pages/LoginPage'; // Assurez-vous que GamePage est défini

const container = document.getElementById('root'); // Sélectionnez l'élément root
const root = createRoot(container); // Créez un root React

root.render(
  <Router>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/game/:gameId" element={<GamePage />} />
      <Route path="/login" element={<LoginPage/>}/>
    </Routes>
  </Router>,
);
