# Mony API 💰

A **Mony API** é um sistema de pagamento que permite ao usuário realizar o registro, adicionar itens ao carrinho 
e efetuar o pagamento de suas ordens. Além disso, a API envia uma notificação por e-mail sempre que um pagamento 
é confirmado, oferecendo uma experiência completa e automatizada para o usuário. O objetivo é facilitar o processo 
de compra e pagamento de maneira simples e eficiente.

## Funcionalidades 💡

- **Registro de Usuário**: Permite ao usuário criar uma conta para gerenciar suas ordens.
- **Carrinho de Compras**: O usuário pode adicionar itens ao carrinho e proceder com o pagamento.
- **Processamento de Pagamento**: Realiza o pagamento e atualiza o status das ordens.
- **Notificação por E-mail**: Envia um e-mail ao usuário quando o pagamento é confirmado.

## Estrutura de Pastas 🗂️

- **account**: Gerencia as contas de usuário.
- **locust**: Usado para realizar testes de carga na aplicação.
- **notification**: Envia notificações por e-mail quando um pagamento é realizado.
- **order**: Gerencia o ciclo de vida de um pedido (criação, atualização, status).
- **payment**: Processa os pagamentos e atualiza o status das ordens.
- **server**: Servidor de descoberta para microsserviços utilizando Eureka.

## Tecnologias Usadas 💻

A Mony API foi construída utilizando as seguintes tecnologias:

- **Java 21** ☕: A linguagem principal para o desenvolvimento da API.
- **Spring Boot** ⚡: Framework usado para construir e configurar a API de forma rápida e eficiente.
- **Spring Data JPA** 🗄️: Para facilitar a interação com o banco de dados e o mapeamento das entidades.
- **Eureka (Spring Cloud)** 🌐: Para o gerenciamento e descoberta de microsserviços.
- **PostgreSQL** 🐘: Banco de dados utilizado para armazenar as informações de ordens, usuários, e pagamentos.
- **Locust** 🦗: Ferramenta para realizar testes de carga na aplicação e garantir sua performance sob alta demanda.

## Instalação e Configuração ⚙️

Para rodar a **Mony API** em sua máquina local, siga os passos abaixo:

### 1. Clonando o Repositório

Primeiro, clone o repositório para o seu ambiente local:

```bash
git clone https://github.com/gersonfragoso/ms-mony.git
cd ms-mony
````
### 2. Configurando o Banco de Dados 🗃️

A API usa o PostgreSQL como banco de dados. Para configurá-lo:

Crie um banco de dados no PostgreSQL (ou utilize um existente).

No arquivo application.properties, configure as credenciais do banco;

### Testando a API 🧪
Use o Postman ou outro cliente HTTP para testar os endpoints da API. Você pode consultar a documentação Swagger para obter mais detalhes sobre os endpoints disponíveis.

## Contribuintes ✨

Desenvolvedores responsáveis pelo trabalho no desenvolvimento da **Mony API**:

- **Gerson Fragoso**: Responsável pela implementação do serviço de **Account**.
- **Fernanda Pagano**: Responsável pelos serviços de **Payment**, **Notification** e integração dos microsserviços.
- **Maria Eduarda Sotério**: Responsável pelos serviços de **Order** e **Locust** (testes de carga).
  
Todos os contribuintes trabalharam de forma colaborativa, ajudando uns aos outros nas diferentes áreas da aplicação e na resolução de desafios técnicos.
