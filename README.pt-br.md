🌐 Idioma:
[Inglês](./README.md) | [Português](./README.pt-br.md)

# Queue Master 🚀

O **Queue Master** é um sistema de gerenciamento de filas de produção industrial em tempo real. Ele foi projetado para coordenar o fluxo de ordens de serviço dentro de uma fábrica, dividindo os processos em três grandes estações de trabalho: **Retirada de Estoque (Stock Withdrawal)**, **Impressão de Identificação (Printing)** e **Corte de Cabos (Wire Cutting)**.

O sistema utiliza **WebSockets (STOMP)** para atualizar instantaneamente os painéis globais e os feeds privados dos operadores assim que qualquer alteração de status ou movimentação de ordens acontece.

---

## 🏗️ Arquitetura e Organização do Projeto

O projeto adota princípios de **Clean Architecture** e **SOLID**, sendo totalmente estruturado em torno de subdomínios de negócio. A camada de apresentação (`presentation`) reflete essa divisão tanto nos controllers REST quanto nos publicadores de eventos em tempo real.

```
src/main/java/com/benevenuto/queue_master/
│
├── application/             # Casos de Uso (Use Cases) isolados por domínio (printing_details, stock_withdrawal_details, wire_cutting_details, user)
├── domain/                  # Entidades, repositórios e enums, isolados por subdomínio
│   ├── common/              # Contratos compartilhados (IBaseRepository, IBaseRepositoryOrder, OrderStatus)
│   ├── printing_details/    # Entidade, repositório e enums da Impressão
│   ├── stock_withdrawal_details/
│   ├── wire_cutting_details/
│   └── user/
├── infra/                   # Infraestrutura (Segurança, configuração WebSocket, implementações JPA) isolada por subdomínio
│
└── presentation/            # Camada de Entrada (Controladores, DTOs e Eventos)
    ├── auth/                # Controller e DTOs de autenticação
    ├── common/dto/          # DTOs compartilhados (BaseOrderRequestDTO, OrderDataNotificationDTO)
    ├── exception/           # Tratamento global de exceções
    ├── interfaces/           # Contratos e abstrações (IQueueEventPublisher)
    ├── printing/             # Controller REST, DTOs e eventos WebSocket da Impressão
    ├── stock_withdrawal/     # Controller REST e eventos WebSocket do Estoque
    └── wire_cutting/         # Controller REST, DTOs e eventos WebSocket do Corte de Cabos
```

## 🔐 Matriz de Permissões e Segurança

A API possui controle de acesso baseado em perfis (**RBAC**) utilizando Spring Security e tokens JWT. Os perfis são divididos em:

* **ADMIN**: Controle total do sistema, incluindo gerenciamento de usuários.
* **INVENTOR**: Supervisor/Gestor da fábrica. Acessa painéis globais e gerencia ordens, mas não cria usuários.
* **OPERATOR**: Operador de chão de fábrica. Restrito ao escopo de suas próprias atividades e de seu número de registro (`operatorNumber`).

### Endpoints HTTP REST

| Mapeamento | Método | Endpoint | ADMIN | INVENTOR | OPERATOR |
| :--- | :---: | :--- | :---: | :---: | :---: |
| **Autenticação** | `POST` | `/auth/login` | permitAll | permitAll | permitAll |
| **Autenticação** | `POST` | `/auth/register` | ✅ | ❌ | ❌ |
| **Usuários** | `POST` | `/users` | ✅ | ❌ | ❌ |
| **Usuários** | `GET` | `/users` | ✅ | ❌ | ❌ |
| **Usuários** | `PUT` | `/users/{id}` | ✅ | ❌ | ❌ |
| **Usuários** | `DELETE` | `/users/{id}` | ✅ | ❌ | ❌ |
| **Usuários** | `PATCH` | `/users/{id}/password` | apenas o próprio | apenas o próprio | apenas o próprio |
| **Ordens** | `POST` | `/orders/**` | ✅ | ✅ | ✅ |
| **Ordens** | `PATCH` | `/orders/**/status` | ✅ | ✅ | ❌ |
| **Ordens** | `GET` | `/orders/**/operator/{opNumber}` | ✅ | ✅ | ✅ |
| **Ordens** | `GET` | `/orders/**` *(Listar Todos)* | ✅ | ✅ | ❌ |
| **Ordens** | `DELETE` | `/orders/**` | ✅ | ✅ | ❌ |

> 💡 **Nota:** O prefixo `**` nos endpoints de ordens é substituído por `stock-withdrawal`, `printing` ou `wire-cutting` de acordo com a estação.
>
> 💡 **Escopo do OPERATOR:** o OPERATOR pode apenas **criar** ordens e **visualizar as suas próprias** (`GET /orders/**/operator/{opNumber}`). Ele não pode atualizar o status, listar todas as ordens ou excluir ordens.

---

## 📡 Comunicação em Tempo Real (WebSockets)

O backend expõe um endpoint centralizado utilizando o protocolo STOMP sobre WebSockets para atualizações automáticas na interface do usuário (sem necessidade de *polling*).

* **Endpoint de Conexão (Handshake):** `ws://localhost:8080/ws-queue`

### Tópicos para Inscrição (Subscriptions)

O frontend pode assinar dois tipos de tópicos dependendo do painel visual que está renderizando:

#### 1. Painéis Globais (Telões da Fábrica)
Alimentados automaticamente pelo método `findAll()` de cada estação sempre que ocorre um evento de escrita.
* `/topic/stock-withdrawal`
* `/topic/printing`
* `/topic/wire-cutting`

#### 2. Painéis Privados (Feed de cada Operador)
Alimentados de forma filtrada passando apenas as ordens vinculadas ao identificador único do operador conectado.
* `/topic/stock-withdrawal/operator/{operatorNumber}`
* `/topic/printing/operator/{operatorNumber}`
* `/topic/wire-cutting/operator/{operatorNumber}`

---

## 📋 Exemplos de Uso da API

> Todos os endpoints (exceto `/auth/**`) exigem o header `Authorization: Bearer <token>` obtido em `/auth/login`. Substitua `{{baseUrl}}` por `http://localhost:8080`.

### Autenticação

#### Cadastrar novo usuário
`POST {{baseUrl}}/auth/register`

Corpo da requisição:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "operatorNumber": 1001,
  "password": "S3cret!23",
  "role": "OPERATOR"
}
```
`role` aceita: `ADMIN`, `INVENTOR`, `OPERATOR`.

Resposta `200 OK`:
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

Corpo da requisição:
```json
{
  "email": "john.doe@example.com",
  "password": "S3cret!23"
}
```

Resposta `200 OK`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSJ9.xxxxxxxxxxxxxxxx"
}
```

---

### Gestão de Usuários (`/users`)

> `POST`, `GET`, `PUT` e `DELETE` são restritos a **ADMIN**. `PATCH /users/{id}/password` pode ser chamado por qualquer usuário autenticado, mas somente para alterar a **própria** senha.

#### Criar usuário
`POST {{baseUrl}}/users`

Novos usuários são sempre criados com a senha padrão **`padrao123`** — não existe campo `password` na requisição.

Corpo da requisição:
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "operatorNumber": 1002,
  "role": "OPERATOR"
}
```
`role` aceita: `ADMIN`, `INVENTOR`, `OPERATOR`.

Resposta `201 Created`:
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

#### Listar usuários
`GET {{baseUrl}}/users`

Resposta `200 OK`:
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

#### Editar usuário
`PUT {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d`

Todos os campos são opcionais — apenas os campos enviados são atualizados.

Corpo da requisição:
```json
{
  "name": "Jane Smith Silva",
  "role": "INVENTOR",
  "active": true
}
```

Resposta `200 OK`: usuário atualizado, no mesmo formato da resposta de criação.

#### Remover usuário
`DELETE {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d`

Resposta: `204 No Content`.

#### Alterar senha
`PATCH {{baseUrl}}/users/7a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d/password`

O `{id}` no caminho precisa ser o mesmo do usuário autenticado que faz a requisição, caso contrário a API retorna `403 Forbidden`.

Corpo da requisição:
```json
{
  "currentPassword": "padrao123",
  "newPassword": "minhaNovaS3nha!"
}
```

Resposta: `204 No Content`.

---

### Ordens de Impressão (`/orders/printing`)

#### Criar ordem(ns)
`POST {{baseUrl}}/orders/printing?opNumber=1001`

Corpo da requisição (array de ordens):
```json
[
  {
    "workOrderNumber": "WO-1001",
    "operatorNumber": "1001",
    "quantity": 50,
    "isUrgent": false,
    "reason": "Reimpressão de etiqueta do lote 22",
    "printText": "BATCH-22-LABEL"
  }
]
```

Resposta: `201 Created` (sem corpo).

#### Listar todas as ordens
`GET {{baseUrl}}/orders/printing`

Resposta `200 OK`:
```json
[
  {
    "id": "a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d",
    "workOrderNumber": "WO-1001",
    "operatorNumber": "1001",
    "printText": "BATCH-22-LABEL",
    "quantity": 50,
    "isUrgent": false,
    "reason": "Reimpressão de etiqueta do lote 22",
    "status": "pending",
    "createdAt": "21/06/2026 10:05:00",
    "updatedAt": "21/06/2026 10:05:00"
  }
]
```

#### Listar ordens por operador
`GET {{baseUrl}}/orders/printing/operator/1001`

Resposta: mesmo formato acima, filtrado por `operatorNumber`.

#### Atualizar status da ordem (apenas ADMIN / INVENTOR)
`PATCH {{baseUrl}}/orders/printing/a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d/status?status=in_progress`

`status` aceita: `pending`, `in_progress`, `finished`.

Resposta: `204 No Content` (a mudança de status também é propagada pelos tópicos WebSocket `/topic/printing` e `/topic/printing/operator/{operatorNumber}`).

#### Remover ordem
`DELETE {{baseUrl}}/orders/printing/a3b1c2d3-e4f5-4a6b-8c7d-9e0f1a2b3c4d`

Resposta: `204 No Content`.

---

### Ordens de Retirada de Estoque (`/orders/stock-withdrawal`)

#### Criar ordem(ns)
`POST {{baseUrl}}/orders/stock-withdrawal?opNumber=1001`

Corpo da requisição (array de ordens):
```json
[
  {
    "workOrderNumber": "WO-2002",
    "operatorNumber": "1001",
    "itemName": "Parafuso M6",
    "quantity": 200,
    "isUrgent": true,
    "reason": "Reposição da linha 3"
  }
]
```

Resposta: `201 Created` (sem corpo).

#### Listar todas / por operador
`GET {{baseUrl}}/orders/stock-withdrawal`
`GET {{baseUrl}}/orders/stock-withdrawal/operator/1001`

Resposta `200 OK`:
```json
[
  {
    "id": "b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e",
    "workOrderNumber": "WO-2002",
    "operatorNumber": "1001",
    "itemName": "Parafuso M6",
    "quantity": 200,
    "isUrgent": true,
    "reason": "Reposição da linha 3",
    "status": "pending",
    "createdAt": "21/06/2026 10:10:00",
    "updatedAt": "21/06/2026 10:10:00"
  }
]
```

#### Atualizar status / Remover (apenas ADMIN / INVENTOR)
`PATCH {{baseUrl}}/orders/stock-withdrawal/b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e/status?status=finished` → `204 No Content`
`DELETE {{baseUrl}}/orders/stock-withdrawal/b4c2d3e4-f5a6-4b7c-9d8e-0f1a2b3c4d5e` → `204 No Content`

---

### Ordens de Corte de Cabos (`/orders/wire-cutting`)

#### Criar ordem(ns)
`POST {{baseUrl}}/orders/wire-cutting?opNumber=1001`

Corpo da requisição (array de ordens):
```json
[
  {
    "workOrderNumber": "WO-3003",
    "operatorNumber": "1001",
    "wireName": "Cobre 2.5mm",
    "quantity": 10,
    "isUrgent": false,
    "lengthMm": 1500.00,
    "reason": "Lote de cabeamento de painel"
  }
]
```

Resposta: `201 Created` (sem corpo).

#### Listar todas / por operador
`GET {{baseUrl}}/orders/wire-cutting`
`GET {{baseUrl}}/orders/wire-cutting/operator/1001`

Resposta `200 OK`:
```json
[
  {
    "id": "c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f",
    "workOrderNumber": "WO-3003",
    "operatorNumber": "1001",
    "wireName": "Cobre 2.5mm",
    "quantity": 10,
    "isUrgent": false,
    "lengthMm": 1500.00,
    "reason": "Lote de cabeamento de painel",
    "status": "pending",
    "createdAt": "21/06/2026 10:15:00",
    "updatedAt": "21/06/2026 10:15:00"
  }
]
```

#### Atualizar status / Remover (apenas ADMIN / INVENTOR)
`PATCH {{baseUrl}}/orders/wire-cutting/c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f/status?status=in_progress` → `204 No Content`
`DELETE {{baseUrl}}/orders/wire-cutting/c5d3e4f5-a6b7-4c8d-9e0f-1a2b3c4d5e6f` → `204 No Content`

---

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.x**
* **Spring Security** (Autenticação Stateless via JWT)
* **Spring WebSocket** (Mensageria STOMP)
* **Lombok** (Produtividade e redução de Boilerplate)

---

## 🚀 Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/seu-usuario/queue-master.git](https://github.com/seu-usuario/queue-master.git)