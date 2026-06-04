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
├── application/             # Casos de Uso (Use Cases) isolados por domínio
├── domain/                  # Entidades de negócio e regras nucleares
├── enums/                   # Enumeradores globais (OrderStatus, RequestType)
├── infra/                   # Configurações de infraestrutura (Segurança, WebSockets)
│
└── presentation/            # Camada de Entrada (Controladores e Eventos)
    ├── order_queue/         # Controladores REST organizados por estação
    └── websocket/           # Infraestrutura de mensageria WebSockets
        ├── interfaces/      # Contratos e abstrações (IQueueEventPublisher)
        ├── printing/        # Eventos em tempo real da Impressão
        ├── stock_withdrawal/# Eventos em tempo real do Estoque
        └── wire_cutting/    # Eventos em tempo real do Corte de Cabos
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
| **Ordens** | `POST` | `/orders/**` | ✅ | ✅ | ✅ |
| **Ordens** | `PATCH` | `/orders/**/status` | ✅ | ✅ | ✅ |
| **Ordens** | `GET` | `/orders/**/operator/{opNumber}` | ✅ | ✅ | ✅ |
| **Ordens** | `GET` | `/orders/**` *(Listar Todos)* | ✅ | ✅ | ❌ |
| **Ordens** | `DELETE` | `/orders/**` | ✅ | ✅ | ❌ |

> 💡 **Nota:** O prefixo `**` nos endpoints de ordens é substituído por `stock-withdrawal`, `printing` ou `wire-cutting` de acordo com a estação.

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