<div align="center">
  <h1>BlogArtigos: Plataforma de Gerenciamento e Publicação de Artigos</h1>
  <p>Uma solução robusta e moderna para gerenciamento de conteúdo de blog, desenvolvida com o ecossistema Spring Boot.</p>

  <!-- Badges de Status e Tecnologias -->
  <p>
    <img src="https://img.shields.io/badge/Status-Ativo-success" alt="Status do Projeto"/>
    <img src="https://img.shields.io/badge/Licença-MIT-blue" alt="Licença MIT"/>
    <img src="https://img.shields.io/badge/Java-17+-orange" alt="Java 17+"/>
    <img src="https://img.shields.io/badge/Spring%20Boot-3.5.7-green" alt="Spring Boot 3.5.7"/>
    <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" alt="MySQL"/>
    <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white" alt="Thymeleaf"/>
  </p>
</div>

---

## 📝 Sobre o Projeto

O **BlogArtigos** é uma plataforma completa projetada para simplificar o gerenciamento e a publicação de artigos de blog. Este projeto é focado no **backend**, oferecendo uma **API RESTful** para manipulação de dados e, simultaneamente, servindo páginas dinâmicas através do **Thymeleaf** para uma interface de administração e visualização.

A arquitetura utiliza o **Spring Boot** para garantir uma aplicação autossuficiente, escalável e de fácil manutenção, seguindo as melhores práticas de desenvolvimento de software.

## ✨ Funcionalidades Detalhadas

A plataforma oferece um conjunto abrangente de recursos para um sistema de blog moderno:

| Categoria | Funcionalidade | Descrição |
| :--- | :--- | :--- |
| **Conteúdo** | **CRUD de Artigos** | Criação, Leitura, Atualização e Exclusão (CRUD) completa de artigos, incluindo suporte a rich text e metadados. |
| **Estrutura** | **Categorização** | Organização eficiente de artigos por categorias, facilitando a navegação e a busca. |
| **Segurança** | **Autenticação e Autorização** | Implementação robusta com **Spring Security** e **JWT (JSON Web Tokens)** para proteger endpoints da API e rotas administrativas. |
| **Interação** | **Gerenciamento de Comentários** | Módulo para moderação e exibição de comentários associados a cada artigo. |
| **Análise** | **Estatísticas** | Visualização de métricas e estatísticas de uso (ex: `VwEstatisticasAutor`), auxiliando na tomada de decisão editorial. |
| **Acesso Público** | **Busca Otimizada** | Endpoint público para listagem e busca de artigos por termo no título, otimizado para performance. |
| **Interface** | **Web Dinâmica** | Páginas de login, dashboard e formulários de edição renderizadas no lado do servidor com **Thymeleaf**. |

## 🛠️ Stack Tecnológico

O projeto é construído sobre um _stack_ de tecnologias Java amplamente reconhecido e maduro:

| Categoria | Tecnologia | Versão Principal | Propósito |
| :--- | :--- | :--- | :--- |
| **Linguagem** | **Java** | 17+ | Linguagem de programação principal, focada em performance e estabilidade. |
| **Framework** | **Spring Boot** | 3.5.7 | Simplifica a configuração e o desenvolvimento de aplicações Spring. |
| **Web** | **Spring Web** | - | Criação de API RESTful e suporte a arquitetura MVC. |
| **Persistência** | **Spring Data JPA** | - | Abstração e gerenciamento de dados, utilizando Hibernate como provedor. |
| **Banco de Dados** | **MySQL** | - | Banco de dados relacional para armazenamento persistente de dados. |
| **Segurança** | **Spring Security** | - | Fornece autenticação e autorização declarativa e robusta. |
| **Token** | **Java JWT (Auth0)** | 4.4.0 | Biblioteca para manipulação segura de JSON Web Tokens. |
| **Frontend** | **Thymeleaf** | - | Motor de template para renderização de HTML dinâmico no lado do servidor. |
| **Auxiliar** | **Lombok** | - | Reduz a verbosidade do código Java (getters, setters, construtores). |

## 🚀 Guia de Execução Local

Siga os passos abaixo para configurar e executar o **BlogArtigos** em seu ambiente de desenvolvimento.

### Pré-requisitos

Certifique-se de que os seguintes softwares estão instalados em sua máquina:

*   **Java Development Kit (JDK) 17** ou superior.
*   **Apache Maven** (para gerenciamento de dependências e _build_).
*   **MySQL Server** (ou um container Docker de MySQL).

### 1. Clonagem do Repositório

Abra seu terminal e clone o projeto:

```bash
git clone https://github.com/albernazz/BlogArtigos.git
cd BlogArtigos/BlogArtigos
```

### 2. Configuração do Banco de Dados

1.  Crie um novo banco de dados no seu servidor MySQL (ex: `blogartigos_db`).
2.  Localize o arquivo de configuração `src/main/resources/application.properties`.
3.  Atualize as propriedades de conexão com suas credenciais:

    ```ini
    # Configurações do MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/blogartigos_db?useSSL=false&serverTimezone=UTC
    spring.datasource.username=seu_usuario_mysql
    spring.datasource.password=sua_senha_mysql

    # Configuração do Hibernate (JPA)
    spring.jpa.hibernate.ddl-auto=update # Use 'create' na primeira execução para gerar o schema
    spring.jpa.show-sql=true
    ```

### 3. Execução da Aplicação

Utilize o wrapper Maven (`mvnw`) para compilar e iniciar a aplicação:

```bash
# 1. Compilar e instalar as dependências
./mvnw clean install

# 2. Iniciar o servidor Spring Boot
./mvnw spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`.

## 🤝 Contribuição

Sua contribuição é muito bem-vinda! Encorajamos a comunidade a nos ajudar a melhorar o **BlogArtigos**.

1.  Faça um **Fork** do projeto.
2.  Crie uma nova _branch_ para sua funcionalidade (`git checkout -b feature/nova-funcionalidade`).
3.  Faça o **Commit** de suas alterações (`git commit -m 'feat: Adiciona nova funcionalidade X'`).
4.  Faça o **Push** para a _branch_ (`git push origin feature/nova-funcionalidade`).
5.  Abra um **Pull Request** detalhado.

## 📄 Licença

Este projeto está distribuído sob a licença **MIT**. Para mais informações, consulte o arquivo [`LICENSE`](LICENSE).

