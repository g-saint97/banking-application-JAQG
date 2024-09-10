import './App.css';
import LoginForm from './components/LoginForm';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { useState, createContext } from 'react';
import RegisterForm from './components/RegisterForm';
import BankDashBoard from './components/BankDashBoard';
import TransactionsPage from './components/TransactionPage';

export const UserContext = createContext();

function App() {
  const [user, setUser] = useState(null);

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <UserContext.Provider value={{ user, setUser }}>
        <BrowserRouter>
          <header className="bg-blue-800 text-white py-4 shadow-md">
            <div className="max-w-7xl mx-auto px-4">
              <h1 className="text-3xl font-bold">The JAQG Banking Application</h1>
            </div>
          </header>
          <main className="flex-grow p-4">
            <div className="max-w-3xl mx-auto">
              <Routes>
                <Route path="/" element={<RegisterForm />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/dashboard" element={<BankDashBoard />} />
                <Route path="/transactions/:accountNumber" element={<TransactionsPage/>} />
              </Routes>
            </div>
          </main>
          <footer className="bg-gray-800 text-white py-4">
            <div className="max-w-7xl mx-auto px-4 text-center">
              <p>&copy; 2024 The JAQG Banking Application. All rights reserved.</p>
            </div>
          </footer>
        </BrowserRouter>
      </UserContext.Provider>
    </div>
  );
}

export default App;
