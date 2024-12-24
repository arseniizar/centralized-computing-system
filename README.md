# Centralized Computing System (CCS)

## Overview

This project implements a **Centralized Computing System (CCS)** in Java. The application acts as a computing server that supports service discovery, client communication, and statistics reporting.

The server is launched with a specific port number and provides the following functionalities:
- **Service Discovery** via UDP.
- **Client Communication** using TCP for arithmetic operations.
- **Statistics Reporting** summarizing server activity.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK 1.8)**: Ensure you have JDK 1.8 installed. Using a higher version may lead to incompatibilities.
- A working network environment for UDP and TCP communication.

---

### How to Run

1. **Compile the Application**:
   ```bash
   javac -d out src/*.java
