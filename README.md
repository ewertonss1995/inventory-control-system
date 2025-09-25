# Inventory Control System API

API REST para sistema de controle de estoque desenvolvida em Spring Boot, permitindo gerenciar produtos, categorias e usuários com autenticação JWT e diferentes níveis de acesso.

## 📋 Sobre o Projeto

Sistema backend completo para gerenciamento de inventário, oferecendo uma API REST robusta para controle de produtos, categorias e usuários. Desenvolvido seguindo os princípios da Arquitetura Hexagonal (Ports and Adapters) com Spring Boot.

## 🏗️ Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)** com as seguintes camadas:

- **Adapters**: Controladores REST, repositórios, mappers e manipuladores de exceção
- **Application**: Serviços de negócio e casos de uso
- **Domain**: Entidades, repositórios (interfaces) e enums
- **Port**: Interfaces que definem os contratos dos serviços

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.4.9**
- **Spring Data JPA**
- **Spring Security com JWT**
- **PostgreSQL**
- **H2 Database** (para testes)
- **Flyway** (Migração de banco de dados)
- **Maven**
- **Docker & Docker Compose**
- **Swagger/OpenAPI 3**
- **Lombok**
- **MapStruct**
- **Bean Validation**

## 🌟 Funcionalidades

### Gerenciamento de Produtos
- ✅ Criar produtos com validação
- ✅ Buscar produto por ID
- ✅ Listar todos os produtos
- ✅ Atualizar produtos existentes
- ✅ Excluir produtos
- ✅ Controle de estoque (quantidade)
- ✅ Cálculo automático de preço total

### Gerenciamento de Categorias
- ✅ Criar categorias
- ✅ Buscar categoria por ID
- ✅ Listar todas as categorias
- ✅ Atualizar categorias
- ✅ Excluir categorias
- ✅ Relacionamento com produtos

### Gerenciamento de Usuários
- ✅ Registro de usuários
- ✅ Autenticação com JWT
- ✅ Criação de usuários administrativos
- ✅ Listar usuários (Admin)
- ✅ Buscar usuário por ID
- ✅ Atualizar usuários
- ✅ Excluir usuários

### Segurança e Autorização
- ✅ Autenticação JWT com chaves RSA
- ✅ Autorização baseada em roles (ADMIN/BASIC)
- ✅ Criptografia de senhas com BCrypt
- ✅ Endpoints públicos e protegidos
- ✅ Expiração automática de tokens

## 🗄️ Modelo de Dados

### Entidades Principais

#### Categorias
```sql
CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    registration_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP
);
```

#### Produtos
```sql
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    product_description VARCHAR(100) NOT NULL,
    unit_price NUMERIC NOT NULL,
    quantity INT NOT NULL,
    total_price NUMERIC NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    category_id INT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
```

#### Sistema de Usuários e Roles
```sql
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    user_name VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE users_roles (
    user_id UUID REFERENCES users(user_id),
    role_id INT REFERENCES roles(role_id),
    PRIMARY KEY (user_id, role_id)
);
```

## 🛠️ Configuração e Instalação

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior
- Docker e Docker Compose (opcional)
- PostgreSQL (ou usar via Docker)

### Configuração do Banco de Dados

#### Opção 1: Usando Docker Compose
```bash
cd docker
docker-compose up -d
```

#### Opção 2: PostgreSQL Local
1. Crie um banco chamado `inventorycontroldb`
2. Configure as variáveis de ambiente:
   - `URL_DB`: jdbc:postgresql://localhost:5432/inventorycontroldb
   - `USER_DB`: seu_usuario
   - `PASSWORD_DB`: sua_senha

### Instalação e Execução

1. **Clone o repositório:**
```bash
git clone https://github.com/ewertonss1995/inventory-control-system.git
cd inventory-control-system
```

2. **Configure as variáveis de ambiente:**
```bash
# Windows PowerShell
$env:USER_DB="seu_usuario"
$env:PASSWORD_DB="sua_senha"

# Linux/Mac
export USER_DB=seu_usuario
export PASSWORD_DB=sua_senha
```

3. **Execute a aplicação:**
```bash
# Compilar e instalar dependências
mvn clean install

# Executar a aplicação
mvn spring-boot:run
```

4. **Usando Docker:**
```bash
# Build da imagem
docker build -t inventory-control-system .

# Executar container
docker run -p 8080:8080 \
  -e USER_DB=root \
  -e PASSWORD_DB=abc123.A \
  inventory-control-system
```

## 📚 Documentação da API

### Swagger UI
A documentação interativa da API está disponível em:
- **Local**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Endpoints da API

#### 🔓 Autenticação
- `POST /v1/users/login` - Login do usuário
- `POST /v1/users/create` - Criar usuário básico

#### 📦 Produtos
- `GET /v1/products` - Listar produtos (público)
- `GET /v1/products/{id}` - Buscar produto por ID (público)
- `POST /v1/products` - Criar produto 🔐
- `PUT /v1/products/{id}` - Atualizar produto 🔐
- `DELETE /v1/products/{id}` - Excluir produto 🔐

#### 📂 Categorias
- `GET /v1/categories` - Listar categorias (público)
- `GET /v1/categories/{id}` - Buscar categoria por ID (público)
- `POST /v1/categories` - Criar categoria 🔐
- `PUT /v1/categories/{id}` - Atualizar categoria 🔐
- `DELETE /v1/categories/{id}` - Excluir categoria 🔐

#### 👥 Usuários (Apenas Admin)
- `GET /v1/users/all` - Listar usuários 👑
- `GET /v1/users/{id}` - Buscar usuário por ID 👑
- `POST /v1/users/create/admin` - Criar admin 👑
- `PUT /v1/users/{id}` - Atualizar usuário 👑
- `DELETE /v1/users/{id}` - Excluir usuário 👑

**Legenda:**
- 🔐 Requer autenticação (Bearer Token)
- 👑 Requer permissão de ADMIN

### Exemplos de Uso da API

#### Login
```bash
curl -X POST http://localhost:8080/v1/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "admin",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "acessToken": "eyJhbGciOiJSUzI1NiJ9...",
  "expiresIn": 300
}
```

#### Criar Produto
```bash
curl -X POST http://localhost:8080/v1/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -d '{
    "productName": "Notebook Dell",
    "productDescription": "Notebook Dell Inspiron 15",
    "unitPrice": 2500.00,
    "quantity": 10,
    "categoryId": 1
  }'
```

#### Listar Produtos
```bash
curl -X GET http://localhost:8080/v1/products
```

## 🔐 Segurança e Autenticação

### Configuração JWT
- **Algoritmo**: RSA256
- **Chave privada**: `src/main/resources/app.key`
- **Chave pública**: `src/main/resources/app.pub`
- **Tempo de expiração**: 300 segundos (5 minutos)

### Roles do Sistema
- **ADMIN**: Acesso completo a todas as funcionalidades
- **BASIC**: Acesso a operações de leitura pública e CRUD de produtos/categorias

### Endpoints Públicos
- Todos os endpoints GET de produtos e categorias
- Login e criação de usuário básico
- Documentação Swagger

## 🧪 Testes

### Executar Testes
```bash
# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn test jacoco:report
```

### Cobertura de Testes
O projeto inclui testes unitários para:
- ✅ Controladores REST
- ✅ Serviços de aplicação
- ✅ Repositórios JPA
- ✅ Mappers
- ✅ Manipuladores de exceção
- ✅ Validações

## 🚀 Deploy e Produção

### Build para Produção
```bash
mvn clean package -DskipTests
java -jar target/inventory-control-system-0.0.1-SNAPSHOT.jar
```

### Variáveis de Ambiente
```bash
# Banco de dados
URL_DB=jdbc:postgresql://localhost:5432/inventorycontroldb
USER_DB=usuario_producao
PASSWORD_DB=senha_producao

# Perfil Spring
SPRING_PROFILES_ACTIVE=prod
```

### Docker Production
```dockerfile
# Dockerfile otimizado para produção
FROM openjdk:17-jre-slim
COPY target/inventory-control-system-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/br/com/training/inventory_control_system/
│   │   ├── adapter/
│   │   │   ├── exception/          # Tratamento global de exceções
│   │   │   ├── in/controllers/     # Controladores REST
│   │   │   └── out/                # Mappers e responses
│   │   ├── application/
│   │   │   ├── exception/          # Exceções de negócio
│   │   │   └── services/           # Serviços de aplicação
│   │   ├── config/                 # Configurações (Security, Swagger)
│   │   ├── domain/
│   │   │   ├── entities/           # Entidades JPA
│   │   │   ├── enums/              # Enumerações
│   │   │   └── repositories/       # Interfaces dos repositórios
│   │   ├── port/in/                # Interfaces dos serviços
│   │   └── InventoryControlSystemApplication.java
│   └── resources/
│       ├── db/migration/           # Scripts Flyway
│       ├── app.key                 # Chave privada JWT
│       ├── app.pub                 # Chave pública JWT
│       └── application.yml         # Configurações da aplicação
└── test/
    └── java/                       # Testes unitários e integração
```

## 🔧 Configurações Principais

### application.yml
```yaml
spring:
  application:
    name: Inventory Control System
  datasource:
    url: ${URL_DB:jdbc:postgresql://localhost:5432/inventorycontroldb}
    username: ${USER_DB}
    password: ${PASSWORD_DB}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

jwt:
  public:
    key: classpath:app.pub
  private:
    key: classpath:app.key
```

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

### Padrões de Desenvolvimento
- Siga os princípios SOLID
- Mantenha a arquitetura hexagonal
- Escreva testes para novas funcionalidades
- Use convenções Java e Spring Boot
- Documente endpoints no Swagger

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato e Suporte

- **Desenvolvedor**: Ewerton Santos Silva
- **GitHub**: [@ewertonss1995](https://github.com/ewertonss1995)
- **Issues**: [Reportar Problemas](https://github.com/ewertonss1995/inventory-control-system/issues)

---

**API REST desenvolvida com ❤️ utilizando Spring Boot e boas práticas de desenvolvimento**
