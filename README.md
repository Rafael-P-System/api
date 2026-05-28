# 🏦 Friends Bank - API (Back-End)

Esta é a API oficial do sistema **Friends Bank**, desenvolvida para prover suporte às operações financeiras, autenticação e gerenciamento de dados dos usuários. O back-end foi construído com foco em segurança, escalabilidade e boas práticas de desenvolvimento.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3** (Framework principal)
* **Spring Security** (Autenticação e proteção de rotas)
* **Spring Data JPA** (Camada de persistência de dados)
* **Maven** (Gerenciamento de dependências)

---

## 🚀 Como rodar o projeto localmente

Para executar a API em sua máquina, siga os passos abaixo:

1. **Pré-requisitos:**
   * Ter o **JDK 17** (ou superior) instalado.
   * Ter o **Maven** instalado e configurado no seu sistema.

2. **Clone o repositório:**
```bash
   git clone [https://github.com/Rafael-P-System/api.git](https://github.com/Rafael-P-System/api.git)
Acesse a pasta da API:

Bash
   cd api
Compile e execute a aplicação:

Bash
   ./mvnw spring-boot:run
(No Windows, você pode usar mvnw spring-boot:run)

Acesso:
A API estará rodando por padrão em: http://localhost:8080

📁 Estrutura do Projeto
src/main/java/com/friendsbank/api/:

controller/: Endpoints REST que recebem as requisições.

service/: Regras de negócio e processamento de dados.

repository/: Interfaces para interação com o banco de dados.

model/: Entidades do sistema.

security/: Configurações de autenticação e proteção JWT (se aplicável).

👤 Autor
Rafael Pimentel - Meu GitHub

Developed with 💛 for Portfolio
####
