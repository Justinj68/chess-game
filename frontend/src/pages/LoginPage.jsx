import React, { useState } from "react";
import { NavBar, Footer } from "../components/PageComponents";

function LoginPage() {
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
                <h1 className="text-4xl font-bold mb-8">Chess Game</h1>
                <form className="flex flex-col space-y-6 w-full max-w-md px-6">
                    <input
                        type="text"
                        placeholder="Username"
                        className={`w-full p-3 rounded-lg border ${
                            isDarkMode ? 'border-gray-700 bg-gray-800 text-white' : 'border-gray-300 bg-gray-100 text-black'
                        } focus:outline-none focus:ring-2 focus:ring-primary`}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        className={`w-full p-3 rounded-lg border ${
                            isDarkMode ? 'border-gray-700 bg-gray-800 text-white' : 'border-gray-300 bg-gray-100 text-black'
                        } focus:outline-none focus:ring-2 focus:ring-primary`}
                    />
                    <div className="flex justify-between space-x-4">
                        <button
                            type="button"
                            className="w-full py-3 bg-blue-500 hover:bg-blue-600 text-white font-semibold rounded-lg transition-all"
                        >
                            Register
                        </button>
                        <button
                            type="submit"
                            className="w-full py-3 bg-green-500 hover:bg-green-600 text-white font-semibold rounded-lg transition-all"
                        >
                            Login
                        </button>
                    </div>
                </form>
            </main>

            <Footer isDarkMode={isDarkMode} />
        </div>
    );
}

export default LoginPage;
