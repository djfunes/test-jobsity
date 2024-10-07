
import React from 'react';
import { Alert } from 'react-bootstrap';

const ErrorPage = () => {
  return (
    <div>
      <Alert variant="danger">
        <h4>Page Not Found!</h4>
        <p>The page you are looking for does not exist.</p>
      </Alert>
    </div>
  );
};

export default ErrorPage;