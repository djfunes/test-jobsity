
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Alert, Card, Container, Row, Col, Button } from 'react-bootstrap';
import { format } from 'date-fns';

const ContactDetail = () => {
  const { id } = useParams();
  const [contact, setContact] = useState(null);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchContact = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/');
        return;
      }
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_SERVER_URL}/contacts/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setContact(response.data);
      } catch (err) {
        if (err.response) {
          setError(`Error ${err.response.status}: ${err.response.data.message}`);
        } else {
          setError('An unexpected error occurred.');
        }
      }
    };
    fetchContact();
  }, [id, navigate]);

  const handleBack = () => {
    navigate('/contacts'); // Go back to contacts list
  };

  const formatDate = (dateString) => {
    return dateString ? format(new Date(dateString), 'PPpp') : '';  // Format the date if not null
  };

  return (
    <Container className="mt-5">
      <Row className="justify-content-md-center">
        <Col md={6}>
          {error && <Alert variant="danger">{error}</Alert>}
          {contact && (
            <Card>
              <Card.Body>
                <Card.Title>Contat Name: {contact.name}</Card.Title>
                <Card.Text>Contact Email: {contact.email}</Card.Text>
                <Card.Text>Created At: {formatDate(contact.createdAt)}</Card.Text>
                <Card.Text>Updated At: {formatDate(contact.updatedAt)}</Card.Text>
                <Button variant="primary" onClick={handleBack}>
                  Back to Contacts List
                </Button>
              </Card.Body>
            </Card>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default ContactDetail;
