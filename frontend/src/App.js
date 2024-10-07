import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css'; 
import LoginPage from './components/LoginPage';
import ContactsList from './components/ContactsList';
import ContactDetail from './components/ContactDetail';
import ErrorPage from './components/ErrorPage';
import Header from './components/Header';

function App() {
  return (
    <Router>
      <Header /> {/* Optional: Add a header component for navigation */}
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/contacts" element={<ContactsList />} />
        <Route path="/contacts/:id" element={<ContactDetail />} />
        <Route path="*" element={<ErrorPage />} /> {/* Catch-all for error handling */}
      </Routes>
    </Router>
  );
}

export default App;
