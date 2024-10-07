
import React from 'react';
import { Navbar, Nav } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token'); // Remove the token
    navigate('/'); // Redirect to login page
  };

  return (
    <Navbar bg="light" expand="lg">
      <Navbar.Brand href="#home">Contacts App</Navbar.Brand>
      <Nav className="me-auto">
        <Nav.Link onClick={handleLogout}>Logout</Nav.Link>
      </Nav>
    </Navbar>
  );
};

export default Header;
