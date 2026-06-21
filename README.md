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
├── application/             # Use Cases isolated by domain (printing_details, stock_withdrawal_details, wire_cutting_details, user)
├── domain/                  # Business entities, repositories and enums, isolated by subdomain
│   ├── common/              # Shared contracts (IBaseRepository, IBaseRepositoryOrder, OrderStatus)
│   ├── printing_details/    # Printing entity, repository and enums
│   ├── stock_withdrawal_details/
│   ├── wire_cutting_details/
│   └── user/
├── infra/                   # Infrastructure (Security, WebSocket config, JPA implementations) isolated by subdomain
│
└── presentation/            # Entry Layer (Controllers, DTOs and Events)
    ├── auth/                # Auth controller and DTOs
    ├── common/dto/          # Shared DTOs (BaseOrderRequestDTO, OrderDataNotificationDTO)
    ├── exception/           # Global exception handling
    ├── interfaces/           # Contracts and abstractions (IQueueEventPublisher)
    ├── printing/             # REST Controller, DTOs and WebSocket events for Printing
    ├── stock_withdrawal/     # REST Controller and WebSocket events for Stock Withdrawal
    └── wire_cutting/         # REST Controller, DTOs and WebSocket events for Wire Cutting
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
| **Users** | `POST` | `/users` | ✅ | ❌ | ❌ |
| **Users** | `GET` | `/users` | ✅ | ❌ | ❌ |
| **Users** | `PUT` | `/users/{id}` | ✅ | ❌ | ❌ |
| **Users** | `DELETE` | `/users/{id}` | ✅ | ❌ | ❌ |
| **Users** | `PATCH` | `/users/{id}/password` | own user only | own user only | own user only |
| **Orders** | `POST` | `/orders/**` | ✅ | ✅ | ✅ |
| **Orders** | `PATCH` | `/orders/**/status` | ✅ | ✅ | ❌ |
| **Orders** | `GET` | `/orders/**/operator/{opNumber}` | ✅ | ✅ | ✅ |
| **Orders** | `GET` | `/orders/**` *(List All)* | ✅ | ✅ | ❌ |
| **Orders** | `DELETE` | `/orders/**` | ✅ | ✅ | ❌ |

> 💡 **Note:** The `**` prefix in order endpoints is replaced by `stock-withdrawal`, `printing`, or `wire-cutting` depending on the workstation.
>
> 💡 **OPERATOR scope:** an OPERATOR can only **create** orders and **view their own** orders (`GET /orders/**/operator/{opNumber}`). They cannot update order status, list all orders, or delete orders.

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

## 📋 API Usage Examples

> All endpoints (except `/auth/**`) require an `Authorization: Bearer <token>` header obtained from `/auth/login`. Replace `{{baseUrl}}` with `http://localhost:8080`.

### Authentication

#### Register a new user
`POST {{baseUrl}}/auth/register`

Request body:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "operatorNumber": 1001,
  "password": "S3cret!23",
  "role": "OPERATOR"
}
```
`role` accepts: `ADMIN`, `INVENTOR`, `OPERATOR`.

Response `200 OK`:
```json
{
  "id": "6f1a2e3c-4b5d-4e8f-9a0b-1c2d3e4f5a6b",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "operatorNumber": 1001,
  "role": "OPERATOR",
  "active": true,
  "lastLogin": null,
  "createdAt": "21/06/2026 10:00:00",
  "updatedAt": "21/06/2026 10:00:00"
}
```

#### Login
`POST {{baseUrl}}/auth/login`

Request body:
```json
{
  "email": "john.doe@example.com",
  "password": "S3cret!23"
}
```

Response `200 OK`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.xxxxxxxxxxxxxxxx"
}
```

---

### User Management (`/users`)

> `POST`, `GET`, `PUT` and `DELETE` are restricted to **ADMIN**. `PATCH /users/{id}/password` can be called by any authenticated user, but only to change their **own** password.

#### Create user
`POST {{baseUrl}}/users`

New users are always created with the default password **`pass123456`** — there is no `password` field in the request.

Request body:
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "operatorNumber": 1002,
  "role": "OPERATOR"
}
```
`role` accepts: `ADMIN`, `INVENTOR`, `OPERATOR`.

Response `201 Created`:
```json
{
  "id": "7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d",
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "operatorNumber": 1002,
  "role": "OPERATOR",
  "active": true,
  "lastLogin": null,
  "createdAt": "21/06/2026 10:20:00",
  "updatedAt": "21/06/2026 10:20:00"
}
```

#### List users
`GET {{baseUrl}}/users`

Response `200 OK`:
```json
[
  {
    "id": "7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d",
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "operatorNumber": 1002,
    "role": "OPERATOR",
    "active": true,
    "lastLogin": null,
    "createdAt": "21/06/2026 10:20:00",
    "updatedAt": "21/06/2026 10:20:00"
  }
]
```

#### Edit user
`PUT {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d`

All fields are optional — only the provided fields are updated.

Request body:
```json
{
  "name": "Jane Smith Silva",
  "role": "INVENTOR",
  "active": true
}
```

Response `200 OK`: updated user, same shape as the create response.

#### Delete user
`DELETE {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d`

Response: `204 No Content`.

#### Change password
`PATCH {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d/password`

The `{id}` in the path must match the authenticated user making the request, otherwise the API returns `403 Forbidden`.

Request body:
```json
{
  "currentPassword": "pass123456",
  "newPassword": "myNewS3cret!"
}
```

Response: `204 No Content`.

---

### Printing Orders (`/orders/printing`)

#### Create order(s)
`POST {{baseUrl}}/orders/printing?opNumber=1001`

Request body (array of orders):
```json
[
  {
    "workOrderNumber": "WO-1001",
    "operatorNumber": "1001",
    "quantity": 50,
    "isUrgent": false,
    "reason": "Label reprint for batch 22",
    "printText": "BATCH-22-LABEL"
  }
]
```

Response: `201 Created` (no body).

#### List all orders
`GET {{baseUrl}}/orders/printing`

Response `200 OK`:
```json
[
  {
    "id": "a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d",
    "workOrderNumber": "WO-1001",
    "operatorNumber": "1001",
    "printText": "BATCH-22-LABEL",
    "quantity": 50,
    "isUrgent": false,
    "reason": "Label reprint for batch 22",
    "status": "pending",
    "createdAt": "21/06/2026 10:05:00",
    "updatedAt": "21/06/2026 10:05:00"
  }
]
```

#### List orders by operator
`GET {{baseUrl}}/orders/printing/operator/1001`

Response: same shape as above, filtered by `operatorNumber`.

#### Update order status (ADMIN / INVENTOR only)
`PATCH {{baseUrl}}/orders/printing/a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d/status?status=in_progress`

`status` accepts: `pending`, `in_progress`, `finished`.

Response: `204 No Content` (the status change is also broadcast over the `/topic/printing` and `/topic/printing/operator/{operatorNumber}` WebSocket topics).

#### Delete order
`DELETE {{baseUrl}}/orders/printing/a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d`

Response: `204 No Content`.

---

### Stock Withdrawal Orders (`/orders/stock-withdrawal`)

#### Create order(s)
`POST {{baseUrl}}/orders/stock-withdrawal?opNumber=1001`

Request body (array of orders):
```json
[
  {
    "workOrderNumber": "WO-2002",
    "operatorNumber": "1001",
    "itemName": "M6 Screw",
    "quantity": 200,
    "isUrgent": true,
    "reason": "Line 3 replenishment"
  }
]
```

Response: `201 Created` (no body).

#### List all / by operator
`GET {{baseUrl}}/orders/stock-withdrawal`
`GET {{baseUrl}}/orders/stock-withdrawal/operator/1001`

Response `200 OK`:
```json
[
  {
    "id": "b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e",
    "workOrderNumber": "WO-2002",
    "operatorNumber": "1001",
    "itemName": "M6 Screw",
    "quantity": 200,
    "isUrgent": true,
    "reason": "Line 3 replenishment",
    "status": "pending",
    "createdAt": "21/06/2026 10:10:00",
    "updatedAt": "21/06/2026 10:10:00"
  }
]
```

#### Update status / Delete (ADMIN / INVENTOR only)
`PATCH {{baseUrl}}/orders/stock-withdrawal/b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e/status?status=finished` → `204 No Content`
`DELETE {{baseUrl}}/orders/stock-withdrawal/b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e` → `204 No Content`

---

### Wire Cutting Orders (`/orders/wire-cutting`)

#### Create order(s)
`POST {{baseUrl}}/orders/wire-cutting?opNumber=1001`

Request body (array of orders):
```json
[
  {
    "workOrderNumber": "WO-3003",
    "operatorNumber": "1001",
    "wireName": "Copper 2.5mm",
    "quantity": 10,
    "isUrgent": false,
    "lengthMm": 1500.00,
    "reason": "Panel wiring batch"
  }
]
```

Response: `201 Created` (no body).

#### List all / by operator
`GET {{baseUrl}}/orders/wire-cutting`
`GET {{baseUrl}}/orders/wire-cutting/operator/1001`

Response `200 OK`:
```json
[
  {
    "id": "c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f",
    "workOrderNumber": "WO-3003",
    "operatorNumber": "1001",
    "wireName": "Copper 2.5mm",
    "quantity": 10,
    "isUrgent": false,
    "lengthMm": 1500.00,
    "reason": "Panel wiring batch",
    "status": "pending",
    "createdAt": "21/06/2026 10:15:00",
    "updatedAt": "21/06/2026 10:15:00"
  }
]
```

#### Update status / Delete (ADMIN / INVENTOR only)
`PATCH {{baseUrl}}/orders/wire-cutting/c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f/status?status=in_progress` → `204 No Content`
`DELETE {{baseUrl}}/orders/wire-cutting/c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f` → `204 No Content`

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