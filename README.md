
# **Centralized Computing Server (CCS)**

## **Description**
The Centralized Computing Server (CCS) is a multi-threaded application built in Java to handle client-server communication using both TCP and UDP protocols. It provides a simple mechanism to:
- Discover the server on a network using UDP-based service discovery.
- Accept and process arithmetic operation requests from clients via TCP.
- Periodically report operational statistics.

This project is designed for a task where clients can dynamically connect to and communicate with the server for basic computations.

---

## **Features**
1. **Service Discovery**:
   - The server listens on a specified UDP port for discovery messages and responds to discovery requests.

2. **TCP Client Communication**:
   - Supports multiple simultaneous clients.
   - Handles arithmetic operations (`ADD`, `SUB`, `MUL`, `DIV`).
   - Responds with results or error messages for invalid requests.

3. **Statistics Reporting**:
   - Provides cumulative and periodic statistics every 10 seconds:
     - Total number of clients.
     - Total and invalid requests.
     - Sum of computed results.
     - Operation counts for each type.

---

## **Technologies Used**
- **Programming Language**: Java (JDK 8 or later).
- **Concurrency**: Multi-threading using the `Executors` framework.
- **Networking**: Java Sockets for TCP and UDP communication.

---

## **Installation**

### **Prerequisites**
- Java Development Kit (JDK) version 8 or later installed.
- A terminal or command-line interface to run the application.

### **Compiling the Project**
1. Navigate to the project directory.
2. Compile the Java source file:
   ```bash
   javac CCS.java
   ```

3. Create a JAR file:
   ```bash
   jar cfe CCS.jar CCS *.class
   ```

---

## **Usage**

### **Run the Server**
To start the CCS server, use the following command:
```bash
java -jar CCS.jar <port>
```

- Replace `<port>` with the desired port number for the server to listen on.

### **Expected Behavior**
1. The server initializes and starts listening on the specified TCP and UDP ports.
2. Clients can connect to the server via TCP and send arithmetic operation requests in the format:
   ```
   <OPERATION> <ARG1> <ARG2>
   ```
   Example:
   ```
   ADD 12 5
   ```

3. For valid requests, the server returns the computed result.
4. For invalid requests, the server responds with an error message.

---

## **Project Structure**
```
.
├── CCS.java           # Main server application
├── ClientSimulator.java # Client simulator application
├── README.md          # Documentation file
├── runscripts/        # Run scripts for Windows and Linux/Mac
```

---

## **ClientSimulator**

### **Description**
The `ClientSimulator` class is a Java application designed to simulate client behavior for testing the `CCS` server. It generates random arithmetic requests and communicates with the server over TCP. The client dynamically connects, sends multiple requests, and temporarily disconnects, mimicking real-world client behavior.

### **Usage**
To run the client simulator, use the following command:
```bash
java -jar ClientSimulator.jar <server_ip> <port>
```

- **`<server_ip>`**: IP address of the server running the `CCS`.
- **`<port>`**: The TCP port number on which the server is listening.

Example:
```bash
java -jar ClientSimulator.jar 127.0.0.1 1234
```

---

## **Run Scripts**

### **Description**
The project includes run scripts for both Windows (`.bat`) and Linux/macOS (`.sh`) environments to simplify running the server and client.

### **Server Scripts**
1. **`run-ccs.bat` (Windows)**:
   - Compiles `CCS.java`, creates the `CCS.jar` file, and starts the server.
   - Usage:
     ```
     run-ccs.bat <port>
     ```

2. **`run-ccs.sh` (Linux/macOS)**:
   - Same functionality as the Windows script but for Linux/macOS.
   - Usage:
     ```
     ./run-ccs.sh <port>
     ```

### **Client Scripts**
1. **`run-client.bat` (Windows)**:
   - Compiles `ClientSimulator.java`, creates the `ClientSimulator.jar`, and runs the client.
   - Usage:
     ```
     run-client.bat <server_ip> <port>
     ```

2. **`run-client.sh` (Linux/macOS)**:
   - Same functionality as the Windows script but for Linux/macOS.
   - Usage:
     ```
     ./run-client.sh <server_ip> <port>
     ```

---

## **Protocol Details**

### **UDP Service Discovery**
- **Request**: `DISCOVER`
- **Response**: `SERVICE_AVAILABLE`

### **TCP Communication**
- **Request Format**:
  ```
  <OPERATION> <ARG1> <ARG2>
  ```
  - `<OPERATION>`: Arithmetic operation (`ADD`, `SUB`, `MUL`, `DIV`).
  - `<ARG1>` and `<ARG2>`: Integers.

- **Response**:
  - `RESULT: <value>`: For successful computations.
  - `FAILURE: <error message>`: For invalid requests.

---

## **Statistics**
Statistics are reported every 10 seconds and include:
1. Number of connected clients.
2. Total and invalid requests.
3. Operation counts (`ADD`, `SUB`, `MUL`, `DIV`).
4. Sum of all computed values.

---

## **Error Handling**
- **Invalid Arguments**: Returns `FAILURE: Invalid numbers provided.`
- **Division by Zero**: Returns `FAILURE: Division by zero.`
- **Unknown Operation**: Returns `FAILURE: Unknown operation.`

---

## **Known Issues**
- The server does not support secure communication (e.g., SSL/TLS).
- UDP discovery assumes clients and the server are on the same network.

---
