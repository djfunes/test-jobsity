import React, { useEffect, useState } from 'react';
import axios from 'axios';
import DataTable from 'react-data-table-component';
import { Alert, Button, Container, Row, Col } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const ContactsList = () => {
  const [contacts, setContacts] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchContacts = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/');
        return;
      }
      try {
        const response = await axios.get(process.env.REACT_APP_API_SERVER_URL + '/contacts', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setContacts(response.data);
      } catch (err) {
        if (err.response) {
          setError(`Error ${err.response.status}: ${err.response.data.message}`);
        } else {
          setError('An unexpected error occurred.');
        }
      }
    };
    fetchContacts();
  }, [navigate]);

  const handleContactClick = (id) => {
    navigate(`/contacts/${id}`);
  };

  const columns = [
    {
      name: 'Name',
      selector: row => row.name,
      sortable: true,
      cell: row => <Button variant="link" onClick={() => handleContactClick(row.id)}>{row.name}</Button>
    }

  ];

  return (
    <Container className="mt-5">
      <Row>
        <Col>
          <h2>Contacts List</h2>
          {error && <Alert variant="danger">{error}</Alert>}
          <DataTable
            columns={columns}
            data={contacts}
            pagination
            paginationPerPage={10} // 10 contacts per page
            paginationRowsPerPageOptions={[10, 20, 50]} // Pagination options
            striped
            highlightOnHover
          />
        </Col>
      </Row>
    </Container>
  );
};

export default ContactsList;
