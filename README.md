
# Jobsity Coding Challenge

Assessment to apply for Java Developer Role



## Requirements
### Backend
Java17, SpringBoot 3.2.10, Maven 3.0.4 (See pom.xml for detailed dependencies)
### Frontend
React 18, React Bootstrap, Axios
## Clone project
Using git, clone the project from Github

```bash
  git clone https://github.com/djfunes/test-jobsity.git
```
## Run Backend


Access to the backend directory

```bash
  cd test-jobsity
  cd backend
```

Install dependencies

```bash
  mvn clean install
```

Run the application

```bash
  mvn exec:java
```

## Run Frontend

Access to the frontend directory

```bash
  cd test-jobsity
  cd frontend
```

Install dependencies

```bash
  npm install
```

Run the application
```bash
  npm run start
```
## Backend Test
The backend is now runing on http://localhost:8080, You can now test the endpoints using Postman or any other tool you may like.
### Authenticate endpoint
By executing a POST request to http://localhost:8080/authenticate with the following payload:
```
{
    "username":"david.funes",
    "password":"EuE4mmV5JzEu"
}
```
The endpoint should return a JWT token that will be used to test the remainging endpoints by adding the header Authorization as Bearer.

### Contacts List
By executing a GET request to http://localhost:8080/contacts with the header Authorization that should include the token from the authenticate endpoint.

### Contact Detail
By executing a GET request to http://localhost:8080/contacts/:id with the header Authorization that should include the token from the authenticate endpoint and by passing the :id of the desired contact.

## Frontend Test
The fontend is now runing on http://localhost:3000, open your browser and type the URL. After the application loads, provide the following credentials:
```
User: david.funes
Password: EuE4mmV5JzEu
```
After successfull login, the contact list should load, Now you can click on any contact to view the details.