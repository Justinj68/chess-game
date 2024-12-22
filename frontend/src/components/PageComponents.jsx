import { Moon, Sun } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { FaGithub } from 'react-icons/fa';

export const NavBar = ({ isDarkMode, toggleDarkMode }) => {
  const navClass = isDarkMode ? 'bg-black text-white' : 'bg-gray-100 text-gray-800';
  const navigate = useNavigate();

  const handleNavigateHome = () => {
    navigate('/');
  };

  const handleNavigateLogin = () => {
    navigate('/login');
  }

  return (
    <nav className={`w-full px-4 py-6 flex justify-between items-center ${navClass}`}>
      <h1
        className="text-3xl font-bold cursor-pointer"
        onClick={handleNavigateHome}
      >
        Chess Game
      </h1>
      <div className="flex items-center space-x-6">
        <a
          onClick={handleNavigateLogin}
          className={`cursor-pointer hover:text-primary transition-colors ${
            isDarkMode ? 'text-gray-300' : 'text-gray-600'
          }`}
        >
          Login
        </a>
        <button
          onClick={toggleDarkMode}
          className="p-2 rounded-full bg-primary text-primary-foreground"
        >
          {isDarkMode ? <Moon className="text-gray-300" /> : <Sun className="text-gray-800" />}
        </button>
      </div>
    </nav>
  );
};


export const Footer = ({ isDarkMode }) => {
  const footerClass = isDarkMode ? 'bg-black text-white' : 'bg-gray-100 text-gray-800';

  return (
    <footer
      className={`w-full px-4 py-6 text-center flex justify-center items-center space-x-2 ${footerClass} mt-auto`}
    >
      <a
        href="https://github.com/Justinj68"
        target="_blank"
        rel="noopener noreferrer"
        className="flex items-center space-x-2 hover:text-primary transition-colors"
      >
        <FaGithub className="w-5 h-5" />
        <span>Justinj68</span>
      </a>
    </footer>
  );
}

