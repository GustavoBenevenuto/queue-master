🌐 Language:
[English](./README.md) | [Portuguese](./README.pt-BR.md)

# 🚀 Queue Master

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%20%2F%2021-orange)](https://www.oracle.com/java/)
[![WebSocket](https://img.shields.io/badge/Protocol-WebSocket%20%2F%20STOMP-blue)](https://stomp.github.io/)

**Queue Master** is a reactive queue management ecosystem specifically designed to optimize workflows in dynamic production and manufacturing environments (shop floor operations).

The system organizes and prioritizes complex work orders for different workstations, distributing data in **real time** through persistent bidirectional connections.

---

## 📋 What is this project about?

In industrial and operational environments, communication bottlenecks slow down the production line. **Queue Master** acts as the orchestration brain. It receives complex requests containing multiple subtasks, processes and persists those demands, and instantly dispatches them to reactive dashboards at their respective workstations.

### Supported Workstations:
* **`wire_cutting`** (Wire/Harness Cutting)
* **`identification_printing`** (Identification Label Printing)
* **`stock_withdrawal`** (Stock Component Withdrawal)

---

## 🛠️ What problems does it solve?

1. **The End of "F5" (Inefficient Polling):** Traditional systems require operators to constantly refresh the page to check for new orders. Queue Master uses **WebSockets with STOMP** to push updates at the exact millisecond they are persisted in the database.

2. **Separation of Concerns (Clean Architecture):** Business rules and Use Cases are completely isolated from network protocols and security infrastructure.

3. **Granular Role-Based Security:** Native protection through **Spring Security (JWT)** with strict privilege validation:
   * `OPERATOR`: Exclusive permission to create orders (`POST /orders`).
   * `INVENTOR` and `ADMIN`: Full management control and workflow status modification permissions.

---

## 🏗️ Reactive Flow Architecture

The system operates using the *Publish/Subscribe (Pub/Sub)* pattern with dynamic topics divided by **Workstation** and **Status**. Here is how data flows through the architecture:

```text
[HTTP Client]
           │
           ▼ (POST /orders)
   [OrderController] ───────────(Orchestrates)──────────► [CreateOrderUseCase]
           │                                                       │
           │                                                (Persists to Database)
           │                                                       ▼
           │◄────────────(Returns Created Items)────────── [Database]
           │
           ▼
 [QueueEventPublisher] (For each affected workstation)
           │
           ├──► 1. Queries updated database state (GetQueueByStationUseCase)
           │
           └──► 2. Dispatches updated JSON payload to the STOMP Broker
                                         │
                                         ▼
                           [Subscribed Browsers/Dashboards]
                           Example: /topic/queue/wire_cutting/pending
```

## 💻 Technologies Used

* **Core:** Java 17+, Spring Boot 3.x
* **Database & Migrations:** Spring Data JPA, PostgreSQL, Flyway Migrations
* **Real-Time & Protocols:** Spring WebSocket, STOMP Messaging Framework
* **Security:** Spring Security, JWT (JSON Web Tokens), BCrypt Encryption
* **Testing:** JUnit 5, AssertJ, Spring Web Environment

---

## ⚙️ Setup and Installation

### Prerequisites
* Java 17 or higher installed.
* Maven 3.6+ or Maven Wrapper (`./mvnw`).
* A running PostgreSQL database (or use the test profile with H2).