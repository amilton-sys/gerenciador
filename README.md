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
git clone https://github.com/seu-usuario/gerenciador.git
```

2. Configure as variáveis de ambiente no arquivo `.env`:
```properties
DB_HOST=localhost
DB_USERNAME=root
DB_PASSWORD=root
DB_DDL_AUTO=update 
//Caso queira usar o import.sql que já existe no projeto coloque como create
DB_DDL_AUTO=update 
MAIL_HOST=smtp.gmail.com
MAIL_USERNAME=seu-email@gmail.com
MAIL_PASSWORD=sua-senha
MAIL_PORT=587
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

## 📦 Estrutura do Projeto