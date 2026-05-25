🌐 Language:
[English](./README.md) | [Português](./README.pt-BR.md)

# Queue Master 🚀

**Queue Master** is a real-time industrial production queue management system. It was designed to coordinate the workflow of service orders inside a factory, splitting processes into three major workstations: **Stock Withdrawal**, **Identification Printing**, and **Wire Cutting**.

The system uses **WebSockets (STOMP)** to instantly update global dashboards and private operator feeds whenever any status change or order movement occurs.

---

## 🏗️ Project Architecture and Organization

The project follows **Clean Architecture** and **SOLID** principles, being fully structured around business subdomains. The presentation layer (`presentation`) reflects this separation in both REST controllers and real-time event publishers.

```text
src/main/java/com/benevenuto/queue_master/
│
├── application/             # Use Cases isolated by domain
├── domain/                  # Business entities and core rules
├── enums/                   # Global enums (OrderStatus, RequestType)
├── infra/                   # Infrastructure configurations (Security, WebSockets)
│
└── presentation/            # Entry Layer (Controllers and Events)
    ├── order_queue/         # REST Controllers organized by workstation
    └── websocket/           # WebSocket messaging infrastructure
        ├── interfaces/      # Contracts and abstractions (IQueueEventPublisher)
        ├── printing/        # Real-time Printing events
        ├── stock_withdrawal/# Real-time Stock Withdrawal events
        └── wire_cutting/    # Real-time Wire Cutting events
```

---

## 🔐 Permission Matrix and Security

The API uses role-based access control (**RBAC**) with Spring Security and JWT tokens. The roles are divided into:

* **ADMIN**: Full system control, including user management.
* **INVENTOR**: Factory supervisor/manager. Can access global dashboards and manage orders, but cannot create users.
* **OPERATOR**: Shop floor operator. Restricted to the scope of their own activities and registration number (`operatorNumber`).

### REST HTTP Endpoints

| Mapping | Method | Endpoint | ADMIN | INVENTOR | OPERATOR |
| :--- | :---: | :--- | :---: | :---: | :---: |
| **Authentication** | `POST` | `/auth/login` | permitAll | permitAll | permitAll |
| **Authentication** | `POST` | `/auth/register` | ✅ | ❌ | ❌ |
| **Orders** | `POST` | `/orders/**` | ✅ | ✅ | ✅ |
| **Orders** | `PATCH` | `/orders/**/status` | ✅ | ✅ | ✅ |
| **Orders** | `GET` | `/orders/**/operator/{opNumber}` | ✅ | ✅ | ✅ |
| **Orders** | `GET` | `/orders/**` *(List All)* | ✅ | ✅ | ❌ |
| **Orders** | `DELETE` | `/orders/**` | ✅ | ✅ | ❌ |

> 💡 **Note:** The `**` prefix in order endpoints is replaced by `stock-withdrawal`, `printing`, or `wire-cutting` depending on the workstation.

---

## 📡 Real-Time Communication (WebSockets)

The backend exposes a centralized endpoint using the STOMP protocol over WebSockets for automatic UI updates (without requiring *polling*).

* **Connection Endpoint (Handshake):** `ws://localhost:8080/ws-queue`

### Subscription Topics

The frontend can subscribe to two types of topics depending on the dashboard being rendered:

#### 1. Global Dashboards (Factory Displays)
Automatically fed by the `findAll()` method of each workstation whenever a write event occurs.

* `/topic/stock-withdrawal`
* `/topic/printing`
* `/topic/wire-cutting`

#### 2. Private Dashboards (Operator Feed)
Filtered feeds that only deliver orders associated with the connected operator's unique identifier.

* `/topic/stock-withdrawal/operator/{operatorNumber}`
* `/topic/printing/operator/{operatorNumber}`
* `/topic/wire-cutting/operator/{operatorNumber}`

---

## 🛠️ Technologies Used

* **Java 17**
* **Spring Boot 3.x**
* **Spring Security** (Stateless Authentication via JWT)
* **Spring WebSocket** (STOMP Messaging)
* **Lombok** (Productivity and Boilerplate Reduction)

---

## 🚀 Running the Project

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/queue-master.git
   ```