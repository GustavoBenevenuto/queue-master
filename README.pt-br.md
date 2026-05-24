🌐 Idioma:
[Inglês](./README.md) | [Português](./README.pt-BR.md)

# 🚀 Queue Master

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%20%2F%2021-orange)](https://www.oracle.com/java/)
[![WebSocket](https://img.shields.io/badge/Protocolo-WebSocket%20%2F%20STOMP-blue)](https://stomp.github.io/)

O **Queue Master** é um ecossistema de gerenciamento de filas reativas projetado especificamente para otimizar fluxos de trabalho em ambientes dinâmicos de produção e manufatura (chão de fábrica). 

O sistema organiza e prioriza ordens de serviço complexas destinadas a diferentes estações de trabalho, distribuindo os dados em **tempo real** via conexões bidirecionais persistentes.

---

## 📋 Do que se trata o projeto?

Em ambientes industriais e operacionais, gargalos de comunicação atrasam a linha de produção. O **Queue Master** atua como o cérebro orquestrador. Ele recebe requisições complexas contendo múltiplas subtarefas, processa e persiste essas demandas e as despacha instantaneamente para painéis reativos em suas respectivas estações de trabalho.

### Estações Suportadas:
* **`wire_cutting`** (Corte de Cabos/Chicotes)
* **`identification_printing`** (Impressão de Etiquetas de Identificação)
* **`stock_withdrawal`** (Retirada de Componentes de Estoque)

---

## 🛠️ Quais problemas ele resolve?

1. **Fim do "F5" (Polling Ineficiente):** Sistemas tradicionais exigem que operadores atualizem a página constantemente para checar novas ordens. O Queue Master usa **WebSockets com STOMP** para empurrar atualizações no exato milissegundo em que elas ocorrem no banco.
2. **Separação de Responsabilidades (Clean Architecture):** As regras de negócio e UseCases estão totalmente isolados de protocolos de rede e infraestrutura de segurança.
3. **Segurança Granular baseada em Roles:** Proteção nativa via **Spring Security (JWT)** avaliando privilégios estritos:
   * `OPERATOR`: Permissão exclusiva de inserção (`POST /orders`).
   * `INVENTOR` e `ADMIN`: Controle total de gerenciamento e alteração de status da esteira.

---

## 🏗️ Arquitetura do Fluxo Reativo

O sistema opera sob o conceito de publicação/assinatura (*Pub/Sub*) utilizando tópicos dinâmicos divididos por **Estação** e **Status**. Veja como a informação trafega na arquitetura:

```
[Cliente HTTP]
           │
           ▼ (POST /orders)
   [OrderController] ───────────(Orquestra)──────────► [CreateOrderUseCase]
           │                                                   │
           │                                            (Salva no Banco)
           │                                                   ▼
           │◄───────────(Retorna Itens Criados)───────── [Banco de Dados]
           │
           ▼
 [QueueEventPublisher] (Para cada estação afetada)
           │
           ├──► 1. Consulta banco atualizado (GetQueueByStationUseCase)
           │
           └──► 2. Despacha nova lista em JSON para o Broker STOMP
                                       │
                                       ▼
                         [Navegadores/Painéis Inscritos]
                         Ex: /topic/queue/wire_cutting/pending
```

---

## 💻 Tecnologias Utilizadas

* **Core:** Java 17+, Spring Boot 3.x
* **Database & Migrations:** Spring Data JPA, PostgreSQL, Flyway Migrations
* **Real-Time & Protocolos:** Spring WebSocket, STOMP Messaging Framework
* **Segurança:** Spring Security, JWT (JSON Web Tokens), BCrypt Criptografia
* **Testes:** JUnit 5, AssertJ, Spring Web Environment

---

## ⚙️ Configuração e Instalação

### Pré-requisitos
* Java 17 ou superior instalado.
* Maven 3.6+ ou encapsulado (`./mvnw`).
* Banco de dados PostgreSQL rodando (clique em perfil de teste caso use H2).

