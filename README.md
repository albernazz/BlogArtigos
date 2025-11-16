# BlogArtigos: Plataforma de Gerenciamento de Artigos

## 📝 Descrição do Projeto

O **BlogArtigos** é uma plataforma robusta e moderna desenvolvida em **Spring Boot** para o gerenciamento e publicação de artigos. O projeto visa fornecer uma solução completa de blog, incluindo funcionalidades de autenticação, CRUD (Criação, Leitura, Atualização e Exclusão) de artigos, categorização e um sistema de visualização pública com recursos de busca.

Este repositório contém o código-fonte completo da aplicação backend, que expõe uma API RESTful para manipulação de dados e também serve páginas HTML dinâmicas através do Thymeleaf.

## ✨ Funcionalidades Principais

*   **Gerenciamento de Artigos:** CRUD completo para criação, edição e exclusão de artigos.
*   **Autenticação e Autorização:** Sistema de segurança baseado em **Spring Security** e **JWT (JSON Web Tokens)** para proteger as rotas da API.
*   **Categorização:** Estrutura para organizar artigos por categorias.
*   **Comentários:** Funcionalidade para gerenciar comentários nos artigos.
*   **Estatísticas:** Módulos para visualização de estatísticas (ex: `VwEstatisticasAutor`).
*   **Busca Pública:** Endpoint otimizado para listar artigos públicos com capacidade de busca por termo no título.
*   **Interface Web:** Páginas dinâmicas para login, home, edição e visualização de estatísticas utilizando **Thymeleaf**.

## 🛠️ Tecnologias Utilizadas

O projeto foi construído utilizando um *stack* moderno e amplamente adotado no ecossistema Java.

| Categoria | Tecnologia | Versão Principal | Descrição |
| :--- | :--- | :--- | :--- |
| **Backend** | Java | 17 | Linguagem de programação principal. |
| **Framework** | Spring Boot | 3.5.7 | Facilita a criação de aplicações Spring autossuficientes e prontas para produção. |
| **Web** | Spring Web | - | Criação de API RESTful e controladores MVC. |
| **Persistência** | Spring Data JPA | - | Abstração e gerenciamento de dados com suporte a MySQL. |
| **Segurança** | Spring Security | - | Autenticação e autorização robustas. |
| **Token** | Java JWT (Auth0) | 4.4.0 | Geração e validação de JSON Web Tokens. |
| **Template Engine** | Thymeleaf | - | Renderização de páginas HTML dinâmicas. |
| **Banco de Dados** | MySQL Connector J | - | Conexão com o banco de dados relacional MySQL. |
| **Validação** | Spring Boot Starter Validation | - | Validação de dados de entrada. |
| **Outros** | Lombok | - | Redução de código boilerplate. |

## 🚀 Como Executar o Projeto

Para configurar e executar o projeto localmente, siga os passos abaixo.

### Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

*   **Java Development Kit (JDK) 17** ou superior.
*   **Maven** (para gerenciamento de dependências).
*   **MySQL** (ou outro banco de dados compatível com JPA, configurado no `application.properties`).

### 1. Clonar o Repositório

```bash
git clone https://github.com/albernazz/BlogArtigos.git
cd BlogArtigos/BlogArtigos
```

### 2. Configuração do Banco de Dados

1.  Crie um banco de dados MySQL para a aplicação.
2.  Edite o arquivo de configuração `src/main/resources/application.properties` com suas credenciais de banco de dados:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco_de_dados
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update # Ou 'create' para a primeira execução
    ```

### 3. Executar a Aplicação

Utilize o Maven para compilar e executar o projeto:

```bash
# Compilar o projeto
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run
```

A aplicação estará acessível em `http://localhost:8080`.

## 🤝 Contribuição

Contribuições são bem-vindas! Se você tiver sugestões de melhoria, correções de bugs ou novas funcionalidades, sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request*.

## 📄 Licença

Este projeto está licenciado sob a licença **MIT**. Consulte o arquivo `LICENSE` para mais detalhes.
