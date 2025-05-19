# Crypto Trading Platform

A web application that simulates a cryptocurrency trading platform, allowing users to view real-time prices of cryptocurrencies, maintain a virtual account balance, and track trading history.

## Features

- Real-time cryptocurrency price updates using the Kraken API
- Virtual account balance for buying and selling crypto
- Transaction history tracking
- Asset portfolio management
- Account reset functionality

## Prerequisites
- Java 17 or higher
- Gradle 7+
- MS SQL (or compatible relational database but then is needed additional configuration)
- Node.js v14 or higher
- npm v6 or higher

## Installation
I. For the frontend aaplication:
1. Clone the repository:
```
git clone https://github.com/TsvetanValchinov/Crypto-trading-sim.git
cd crypto-trading-sim
```
2. Install dependencies:
```
npm install
```

3. Start the development server:
```
npm start
```

4. Make sure your Spring Boot backend is running on port 8080.

II. For the backend application
1. Navigate to the backend directory
3. Configure the database connection
```
  spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=crypto_trading;encrypt=true;trustServerCertificate=true
  spring.datasource.username=<your_username>
  spring.datasource.password=<your_password>
  spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
  server.port=8081 // if running on port different from 8080
```
5. Build and run the backend

## Backend API Integration

The frontend connects to a Spring Boot backend with the following endpoints:

- `POST /api/account/user/{userId}` - Create or get a user account
- `GET /api/account/{accountId}/balance` - Get account balance
- `GET /api/account/{accountId}/assets` - Get user's crypto assets
- `GET /api/account/{accountId}/transactions` - Get transaction history
- `POST /api/account/{accountId}/buy` - Buy cryptocurrency
- `POST /api/account/{accountId}/sell` - Sell cryptocurrency
- `POST /api/account/{accountId}/reset` - Reset account balance

##Notes

- The application uses React Bootstrap for UI components
- Real-time price updates are simulated in the current implementation
- In a not testing environment, replace the mock data with actual Kraken WebSocket API integration by changing the usage of cryptoService with krakenService in 
