# Blockchain Access Control System

A decentralized access control system using smart contracts, Spring Boot API, and React frontend.

## Project Structure
- **Smart Contracts**: [Access_Control_Mechanism](https://github.com/batuhantnrkulu/Access_Control_Mechanism)
- **Backend & Frontend**: [blockchain-access-control](https://github.com/batuhantnrkulu/blockchain-access-control)

## Prerequisites
- Node.js v16+
- Java 17 JDK
- MySQL 8.0+
- Ganache CLI
- Truffle
- Maven
- npm/yarn

# Access Control Mechanism using Blockchain

This project consists of **Smart Contracts (Solidity)**, a **Spring Boot Backend (REST API)**, and a **React Frontend** to manage access control mechanisms using blockchain.

## Repository Structure
- **Smart Contracts Repository**: [Access Control Mechanism](https://github.com/batuhantnrkulu/Access_Control_Mechanism)
- **Backend & Frontend Repository**: [Blockchain Access Control](https://github.com/batuhantnrkulu/blockchain-access-control)

---

## Getting Started

### 1. Smart Contracts Setup

#### Clone the Repository
```bash
git clone https://github.com/batuhantnrkulu/Access_Control_Mechanism.git
cd Access_Control_Mechanism
```

#### Install Dependencies
```bash
npm install
```

#### Start Ganache (Local Blockchain) (in one terminal)
```bash
npx ganache -a 25
```

#### Deploy Smart Contracts (in other terminal(ganache should be run while deploying contracts))
```bash
truffle migrate --network development
```
> If you encounter a security error, run:
```powershell
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

#### Copy Deployed Contract Addresses
Once deployed, copy the **contract addresses** from the Truffle migration output.

---

### 2. Backend (Spring Boot API) Setup

#### Clone the Repository
```bash
git clone https://github.com/batuhantnrkulu/blockchain-access-control.git
cd blockchain-access-control/backend
```

#### Configure Environment Variables
Update `application.properties` with the deployed **contract addresses** and **Ganache test account**:
```properties
spring.application.name=access-control-system
spring.config.import=optional:file:.env[.properties]

ganache.address=http://127.0.0.1:8545

#(this should be the first address that ganache provide)
admin.address=0x1B32742d7af2e266b388A950c62097B61c8a9D01 
admin.private-key=0xd9b1e163cdf532fe13723ab2b3f9cc21e939a131aca76636eaf18c45db9bd3be

contract.roleTokenAddress=0x60Ee94CECD5D92A4893fFd35Af76D577f6FD5164
contract.accessControlFactoryAddress=0x77FeC261C37dF5e991e138d2e4D55Dd5D16e5A31

spring.datasource.url=jdbc:mysql://localhost:3306/blockchain_system?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# this will automatically come from env file.
jasypt.encryptor.password=${JASYPT_ENCRYPTOR_SECRET_KEY}
```

#### Setup MySQL Database
- Open **MySQL Workbench** or any MySQL client.
- Create a database schema named **blockchain_system**.
- The database will be automatically updated when Spring Boot starts.

#### Run Spring Boot Application (Without IDE)
```bash
cd blockchain-access-control/backend
./mvnw spring-boot:run
```
> Alternative method:
```bash
mvn clean install
java -jar target/*.jar
```

The backend will run on **http://localhost:8080**.

---

### 3. Frontend (React) Setup

#### Navigate to Frontend Directory
```bash
cd blockchain-access-control/frontend
```

#### Install Dependencies
```bash
npm install
```

#### Start React Application
```bash
npm start
```

The frontend will run on **http://localhost:3000**.

---

## Contributor Guidelines

### Setting Up GitHub for Contribution
1. **Fork the repository** on GitHub.
2. **Clone your fork** to your local machine:
   ```bash
   git clone https://github.com/batuhantnrkulu/blockchain-access-control.git
   cd blockchain-access-control
   ```
3. **Create a new branch** for your feature or fix:
   ```bash
   git checkout -b feature-branch-name
   ```
4. **Make changes & commit**:
   ```bash
   git add .
   git commit -m "Description of changes"
   ```
5. **Pull the latest updates from the main repository**:
   ```bash
   git pull origin main
   ```
6. **Push your changes to your fork**:
   ```bash
   git push origin feature-branch-name
   ```
7. **Create a Pull Request (PR)** on GitHub to merge your changes.

---

## Troubleshooting

### Common Issues & Fixes
- **Ganache not running**: Ensure **npx ganache -a 25** is running before deploying contracts.
- **Contracts not deploying**: Check the Truffle migration logs for errors and retry.
- **Database errors**: Ensure MySQL is running and schema **blockchain_system** exists.
- **React frontend not working**: Try `npm install` again and restart.

For any additional questions or issues, feel free to open an **Issue** on GitHub.

---

## License
This project is open-source and available under the [MIT License](LICENSE).
