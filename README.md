# Gerenciador de Chamados - Backend API

API REST desenvolvida em Java com Spring Boot para gerenciamento de tickets de suporte. O sistema utiliza autenticação JWT e diferenciação de acesso baseada em Roles (Usuário Comum vs. Suporte Técnico).

## Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3** 
* **PostgreSQL**
* **JWT (JSON Web Token)**
* **Docker & Docker Compose**
* **Maven** 
* **Lombok**

## Pré-requisitos

Para rodar este projeto, você precisará de:

* **Java JDK 17 ou superior**
* **Docker** e **Docker Compose** 
* **Maven**.

## Configuração

### Banco de Dados
O projeto está configurado para conectar em um banco PostgreSQL local ou via Docker na porta `5432`.
As credenciais padrões estão em `src/main/resources/application.properties`:

### CORS (Importante para o Frontend)
Para que o frontend funcione, certifique-se de que a origem permitida em src/main/java/com/example/chamado/config/CorsConfig.java corresponde à URL do seu frontend (ex: http://127.0.0.1:5500).

## Como Rodar a Aplicação
```
docker-compose up -d
```
Inicie a aplicação Spring Boot pela IDE (run)
A API estará disponível em: http://localhost:8080

## Principais endpoints

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST   | /auth/login | Autentica o usuário e retorna o token JWT. | Público |
| POST   | /auth/register | Cadastra um novo usuário. A Role (USER/SUPPORT) é definida no corpo do JSON. | Público |
| GET    | /chamados | Lista todos os chamados cadastrados no banco de dados. | ROLE_SUPPORT |
| GET    | /chamados/{userId} | Lista os chamados pertencentes a um usuário específico. | ROLE_USER (apenas o próprio) ou ROLE_SUPPORT |
| POST   | /chamados/{userId} | Cria um novo chamado vinculado ao ID do usuário informado. | ROLE_USER ou ROLE_SUPPORT |
| PUT    | /chamados/{id} | Atualiza um chamado existente (Título, Descrição, Status, Prioridade). | ROLE_SUPPORT |
| DELETE | /chamados/{id} | Exclui permanentemente um chamado do sistema. | ROLE_SUPPORT |

