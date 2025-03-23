# Gerenciador de Gastos

Sistema de gerenciamento de gastos pessoais desenvolvido com Spring Boot.

## 🚀 Funcionalidades

- Controle de despesas mensais
- Gerenciamento de compras
- Cálculo de saldo disponível
- Acompanhamento de gastos por período
- Autenticação de usuários
- Dashboard com gráficos e estatísticas

## 🛠️ Tecnologias

- Java 17
- Spring Boot 3.4.0
- MySQL
- Spring Security
- Thymeleaf
- JPA/Hibernate
- Maven
- Docker

## 📋 Pré-requisitos

- Java 17+
- Maven
- MySQL
- Docker (opcional)

## 🔧 Configuração

1. Clone o repositório:
```bash
git clone https://github.com/seu-user/gerenciador.git
```

2. Configure as variáveis de ambiente no arquivo `.properties`:

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:manager}?createDatabaseIfNotExist=true&serverTimezone=UTC&serverTimezone=UTC
spring.datasource.username=${MYSQL_USERNAME:root}
spring.datasource.password=${MYSQL_PASSWORD:root}

spring.jpa.hibernate.ddl-auto=create com import.sql de dados fictícios
#spring.jpa.hibernate.ddl-auto=update sem import.sql
```

3. Execute o projeto:
```bash
mvn spring-boot:run
```

### 🐳 Usando Docker

1. Build da imagem:
```bash
docker build -t gerenciador .
```

2. Execute com docker-compose:
```bash
docker-compose up -d
```
3. Acessar http://localhost:8080
