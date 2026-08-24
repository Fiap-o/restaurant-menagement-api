# Restaurant Management API

Backend do **Tech Challenge — Fase 1** (Pós Tech Arquitetura e Desenvolvimento Java), desenvolvido em Spring Boot. Fornece a base de um sistema de gestão compartilhado entre restaurantes, cobrindo cadastro, atualização e autenticação de usuários (clientes e donos de restaurante).

## Integrantes

- Sophia Amaral Silva
- Erick Roberto Ribeiro

## Tecnologias

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- Spring Security + JWT 
- PostgreSQL 16
- springdoc-openapi (Swagger / OpenAPI 3)
- Docker & Docker Compose
- Lombok

## Funcionalidades

- Cadastro, atualização e exclusão de usuários
- Dois tipos de usuário: `CLIENTE` e `DONO_RESTAURANTE`
- Endpoint dedicado para troca de senha
- Endpoint dedicado para atualização dos demais dados do usuário
- Busca de usuários por nome
- Garantia de e-mail e login únicos
- Autenticação via login/senha com emissão de token JWT
- Respostas de erro padronizadas via RFC 7807 (`ProblemDetail`)
- Versionamento de API (`/api/v1`)
- Documentação interativa via Swagger UI

## Como executar

### Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução
- Para rodar localmente sem Docker (seção abaixo): [Java 17+](https://adoptium.net/) instalado

### Subindo com Docker Compose (recomendado)

```bash
docker compose up -d --build
```

Isso sobe dois containers:

| Serviço    | Descrição                  | Porta |
|------------|-----------------------------|-------|
| `postgres` | Banco de dados PostgreSQL   | 5432  |
| `api`      | Aplicação Spring Boot       | 8080  |

A aplicação fica disponível em `http://localhost:8080`.

### Rodando localmente (sem empacotar em Docker, para desenvolvimento)

1. Suba apenas o banco: `docker compose up -d postgres`
2. Rode a aplicação pela IDE (classe `RestaurantManagementApplication`) ou via:
   ```bash
   ./mvnw spring-boot:run
   ```

> Se você tiver um PostgreSQL nativo instalado no Windows, garanta que ele não esteja ocupando a porta `5432` antes de subir o container — os dois não podem escutar na mesma porta ao mesmo tempo.

## Configuração

Credenciais padrão do banco (definidas em `docker-compose.yml`, usadas também como default em `application.yaml` para execução local):

| Variável | Valor padrão            |
|----------|--------------------------|
| Banco    | `restaurant_management`  |
| Usuário  | `admin`                  |
| Senha    | `admin123`               |

Ao rodar via Docker Compose, essas variáveis são sobrescritas automaticamente no container `api` (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`), apontando para o serviço `postgres` dentro da rede interna do Compose.

O schema do banco é criado/atualizado automaticamente pelo Hibernate (`ddl-auto: update`) na inicialização — não há necessidade de scripts de migração manuais.

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Autenticação

A maior parte dos endpoints exige um token JWT. Fluxo:

1. Cadastre um usuário — `POST /api/v1/usuarios` (não requer autenticação).
2. Autentique-se — `POST /api/v1/auth/login` com login e senha, recebendo um token JWT.
3. Envie o token nas próximas requisições pelo header `Authorization: Bearer <token>`.

No Swagger UI, use o botão **Authorize** para informar o token e testar as rotas protegidas diretamente pela interface.

## Coleção Postman

O arquivo [`restaurant-management.postman_collection.json`](./restaurant-management.postman_collection.json) contém os principais cenários de teste: cadastro de usuário (válido e inválido), troca de senha, atualização de dados, busca por nome e validação de login. Basta importar no Postman.

## Endpoints principais

| Método | Rota                          | Descrição                        | Autenticação |
|--------|-------------------------------|-----------------------------------|--------------|
| POST   | `/api/v1/usuarios`            | Cadastrar usuário                 | Não          |
| PUT    | `/api/v1/usuarios/{id}`       | Atualizar dados do usuário        | Sim          |
| PATCH  | `/api/v1/usuarios/{id}/senha` | Trocar senha                      | Sim          |
| DELETE | `/api/v1/usuarios/{id}`       | Excluir usuário                   | Sim          |
| GET    | `/api/v1/usuarios/{id}`       | Buscar usuário por ID             | Sim          |
| GET    | `/api/v1/usuarios?nome=`      | Buscar usuários por nome          | Sim          |
| POST   | `/api/v1/auth/login`          | Autenticar e obter token JWT      | Não          |

## Tratamento de erros

Erros seguem o padrão RFC 7807 (`ProblemDetail`), retornando `type`, `title`, `status`, `detail` e `timestamp`. Alguns exemplos:

- `400 Bad Request` — dados inválidos, com detalhamento por campo
- `401 Unauthorized` — credenciais inválidas no login
- `404 Not Found` — usuário não encontrado
- `409 Conflict` — e-mail ou login já cadastrado

## Testes automatizados

O projeto inclui testes unitários (JUnit 5 + Mockito) para a camada de serviço, cobrindo cenários de sucesso e de erro:

- `UsuarioServiceImplTest` — cadastro, atualização, troca de senha, exclusão e busca de usuários
- `AuthServiceImplTest` — autenticação com credenciais válidas e inválidas

Para rodar:

```bash
./mvnw test
```

Ou, pela IDE, clique no ícone ▶️ ao lado da classe de teste (`src/test/java/.../service/impl/`).

## Estrutura do projeto

```
src/main/java/com/fiap/restaurant_management/
├── config/          # Configuração de segurança (Spring Security)
├── controller/      # Controllers REST (Usuario, Auth)
├── dto/             # DTOs de requisição e resposta
├── entities/        # Entidades JPA (Usuario, Endereco, TipoUsuario)
├── exception/       # Exceções de domínio e handler global (ProblemDetail)
├── mapper/          # Conversão entre entidades e DTOs
├── repositories/    # Repositórios Spring Data JPA
├── security/        # Filtro e serviço JWT
└── service/         # Regras de negócio (interfaces + implementações)
```
