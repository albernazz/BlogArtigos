-- ------------------------------------------------------------------
-- SCRIPT COMPLETO - TRABALHO FINAL DE LABORATÓRIO DE BANCO DE DADOS
-- PROJETO: Blog de Artigos
-- ------------------------------------------------------------------

-- -----------------------------------------------------
-- 0. CONFIGURAÇÃO INICIAL
-- -----------------------------------------------------
-- Cria o banco de dados se ele não existir
-- DROP DATABASE db_blog_artigos;
CREATE DATABASE IF NOT EXISTS db_blog_artigos
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_general_ci;

-- Seleciona o banco para usar
USE db_blog_artigos;

-- Permite a criação de funções (pode ser necessário no seu MySQL)
SET GLOBAL log_bin_trust_function_creators = 1;


-- -----------------------------------------------------
-- 1. DDL (CRIAÇÃO DAS TABELAS)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS grupos_usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_grupo VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS usuarios (
  id VARCHAR(20) PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  grupo_id INT NOT NULL,
  CONSTRAINT FK_usuario_grupo
    FOREIGN KEY (grupo_id)
    REFERENCES grupos_usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS categorias (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS artigos (
  id VARCHAR(20) PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  conteudo TEXT NOT NULL,
  data_publicacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  data_modificacao TIMESTAMP NULL,
  usuario_id VARCHAR(20) NOT NULL,
  CONSTRAINT FK_artigo_autor
    FOREIGN KEY (usuario_id)
    REFERENCES usuarios(id)
    ON DELETE CASCADE -- <-- CORREÇÃO: Deleta artigos se o usuário for deletado
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS artigo_categoria (
  artigo_id VARCHAR(20) NOT NULL,
  categoria_id INT NOT NULL,
  CONSTRAINT PK_artigo_categoria
    PRIMARY KEY (artigo_id, categoria_id),
  CONSTRAINT FK_ac_artigo
    FOREIGN KEY (artigo_id)
    REFERENCES artigos(id)
    ON DELETE CASCADE,
  CONSTRAINT FK_ac_categoria
    FOREIGN KEY (categoria_id)
    REFERENCES categorias(id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS auditoria_artigos (
  id_auditoria INT AUTO_INCREMENT PRIMARY KEY,
  artigo_id VARCHAR(20) NOT NULL,
  titulo_antigo VARCHAR(255),
  conteudo_antigo TEXT,
  data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  acao VARCHAR(10) NOT NULL
) ENGINE=InnoDB;


-- -----------------------------------------------------
-- 2. DML (CARGA INICIAL OBRIGATÓRIA)
-- -----------------------------------------------------
INSERT IGNORE INTO grupos_usuarios (nome_grupo) VALUES
('ADMIN'),
('AUTOR'),
('LEITOR');


-- -----------------------------------------------------
-- 3. REQUISITO: FUNÇÕES DE GERAÇÃO DE ID
-- -----------------------------------------------------

-- Função 1: Gerar ID de Usuário (ex: 'USR-0001')
-- JUSTIFICATIVA: Cumpre o requisito de regra própria de ID
-- para dados críticos (usuários).
-- -----------------------------------------------------
DELIMITER $$
CREATE FUNCTION FN_GERA_ID_USUARIO()
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE proximo_id INT;
    SELECT COUNT(*) + 1 INTO proximo_id FROM usuarios;
    RETURN CONCAT('USR-', LPAD(proximo_id, 4, '0'));
END$$
DELIMITER ;

-- Função 2: Gerar ID de Artigo (ex: 'ART-2025-00001')
-- JUSTIFICATIVA: ID mais complexo que reinicia a cada ano,
-- demonstrando uma regra de negócio mais elaborada.
-- -----------------------------------------------------
DELIMITER $$
CREATE FUNCTION FN_GERA_ID_ARTIGO()
RETURNS VARCHAR(20)
DETERMINISTIC
BEGIN
    DECLARE proximo_id_ano INT;
    SELECT COUNT(*) + 1 INTO proximo_id_ano FROM artigos
    WHERE YEAR(data_publicacao) = YEAR(NOW());
    RETURN CONCAT('ART-', YEAR(NOW()), '-', LPAD(proximo_id_ano, 5, '0'));
END$$
DELIMITER ;


-- -----------------------------------------------------
-- 4. REQUISITO: TRIGGERS (MÍNIMO 2)
-- -----------------------------------------------------

-- Trigger 1: Atualizar data de modificação
-- JUSTIFICATIVA: Garante que o campo 'data_modificacao' seja
-- preenchido automaticamente pelo SGBD, mantendo a
-- integridade dos dados sem depender da aplicação.
-- -----------------------------------------------------
DELIMITER $$
CREATE TRIGGER TRG_ATUALIZA_DATA_MODIFICACAO
BEFORE UPDATE ON artigos
FOR EACH ROW
BEGIN
    SET NEW.data_modificacao = NOW();
END$$
DELIMITER ;

-- Trigger 2: Auditoria de Artigos
-- JUSTIFICATIVA: Mantém um histórico de alterações no conteúdo
-- dos artigos, salvando a versão antiga na tabela 'auditoria_artigos'
-- antes de um UPDATE. Essencial para rastreabilidade.
-- -----------------------------------------------------
DELIMITER $$
CREATE TRIGGER TRG_AUDITA_ARTIGO_UPDATE
BEFORE UPDATE ON artigos
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_artigos (artigo_id, titulo_antigo, conteudo_antigo, acao)
    VALUES (OLD.id, OLD.titulo, OLD.conteudo, 'UPDATE');
END$$
DELIMITER ;


-- -----------------------------------------------------
-- 5. REQUISITO: PROCEDURES E FUNCTIONS (MÍNIMO 2)
-- -----------------------------------------------------

-- Procedure 1: Publicar um novo artigo
-- JUSTIFICATIVA: Encapsula a lógica de negócio de criar um artigo.
-- Garante ATOMICIDADE (Transação) ao inserir o artigo e sua
-- categoria. O frontend/backend faz uma única chamada.
-- -----------------------------------------------------
DELIMITER $$
CREATE PROCEDURE SP_PUBLICAR_ARTIGO(
    IN p_usuario_id VARCHAR(20),
    IN p_titulo VARCHAR(255),
    IN p_conteudo TEXT,
    IN p_categoria_id INT
)
BEGIN
    DECLARE v_artigo_id VARCHAR(20);
    
    START TRANSACTION;
    
    -- Chama a função de ID
    SET v_artigo_id = FN_GERA_ID_ARTIGO();
    
    -- Insere o artigo
    INSERT INTO artigos (id, titulo, conteudo, usuario_id)
    VALUES (v_artigo_id, p_titulo, p_conteudo, p_usuario_id);
    
    -- Associa a categoria
    INSERT INTO artigo_categoria (artigo_id, categoria_id)
    VALUES (v_artigo_id, p_categoria_id);
    
    COMMIT;
    
    SELECT v_artigo_id AS 'novo_artigo_id';
END$$
DELIMITER ;

-- Function (extra): Contar artigos de um usuário
-- JUSTIFICATIVA: Centraliza a lógica de contagem, podendo ser
-- reutilizada em Views (como faremos abaixo) ou outras procedures.
-- -----------------------------------------------------
DELIMITER $$
CREATE FUNCTION FN_CONTA_ARTIGOS_USUARIO(p_usuario_id VARCHAR(20))
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total FROM artigos WHERE usuario_id = p_usuario_id;
    RETURN total;
END$$
DELIMITER ;


-- -----------------------------------------------------
-- 6. REQUISITO: VIEWS (MÍNIMO 2)
-- -----------------------------------------------------

-- View 1: Leitura pública dos artigos
-- JUSTIFICATIVA: Abstrai a complexidade dos JOINs para o
-- frontend. Também serve como camada de segurança: o usuário
-- 'LEITOR' pode ter permissão de SELECT apenas nesta view,
-- e não nas tabelas base.
-- -----------------------------------------------------
CREATE OR REPLACE VIEW VW_ARTIGOS_PUBLICOS AS
SELECT
    a.id AS artigo_id,
    a.titulo,
    a.conteudo,
    a.data_publicacao,
    a.data_modificacao,
    u.nome AS autor_nome,
    c.nome AS categoria_nome
FROM artigos a
JOIN usuarios u ON a.usuario_id = u.id
JOIN artigo_categoria ac ON a.id = ac.artigo_id
JOIN categorias c ON ac.categoria_id = c.id
ORDER BY a.data_publicacao DESC;

-- View 2: Estatísticas dos Autores
-- JUSTIFICATIVA: Fornece dados pré-processados para um
-- painel administrativo, combinando dados de usuários,
-- grupos e usando a função FN_CONTA_ARTIGOS_USUARIO.
-- -----------------------------------------------------
CREATE OR REPLACE VIEW VW_ESTATISTICAS_AUTOR AS
SELECT
    u.id AS usuario_id,
    u.nome AS autor_nome,
    u.email,
    g.nome_grupo,
    FN_CONTA_ARTIGOS_USUARIO(u.id) AS total_artigos
FROM usuarios u
JOIN grupos_usuarios g ON u.grupo_id = g.id
WHERE g.nome_grupo = 'AUTOR' OR g.nome_grupo = 'ADMIN';


-- -----------------------------------------------------
-- 7. REQUISITO: ÍNDICES (E JUSTIFICATIVAS)
-- -----------------------------------------------------

-- Índice 1: Login de Usuário
-- JUSTIFICATIVA: A coluna 'email' será usada constantemente
-- em cláusulas WHERE para login. Um índice acelera
-- drasticamente esta busca (operações de SELECT).
-- -----------------------------------------------------
CREATE INDEX IDX_USUARIO_EMAIL ON usuarios(email);

-- Índice 2: Busca de Artigos por Título
-- JUSTIFICATIVA: Para uma futura funcionalidade de "busca"
-- no blog (ex: WHERE titulo LIKE '...%'), um índice na
-- coluna 'titulo' melhora a performance da consulta.
-- -----------------------------------------------------
CREATE INDEX IDX_ARTIGO_TITULO ON artigos(titulo);


-- -----------------------------------------------------
-- 8. REQUISITO: CONTROLE DE ACESSO (NÃO USAR ROOT)
-- -----------------------------------------------------
-- Cria as "funções" (ROLES) do sistema
CREATE ROLE IF NOT EXISTS 'ROLE_LEITOR', 'ROLE_AUTOR', 'ROLE_ADMIN';

-- --- Permissões do LEITOR ---
-- (Pode ler os artigos publicados e ver autores)
GRANT SELECT ON db_blog_artigos.VW_ARTIGOS_PUBLICOS TO 'ROLE_LEITOR';

-- --- Permissões do AUTOR ---
-- (Tem tudo do LEITOR)
GRANT 'ROLE_LEITOR' TO 'ROLE_AUTOR';
-- (Pode ver seus próprios dados)
GRANT SELECT ON db_blog_artigos.usuarios TO 'ROLE_AUTOR';
-- (Pode ver categorias)
GRANT SELECT ON db_blog_artigos.categorias TO 'ROLE_AUTOR';
-- (Pode criar artigos - SELECT/INSERT/UPDATE apenas em artigos e tabela N:M)
GRANT SELECT, INSERT, UPDATE ON db_blog_artigos.artigos TO 'ROLE_AUTOR';
GRANT SELECT, INSERT ON db_blog_artigos.artigo_categoria TO 'ROLE_AUTOR';
-- (Pode executar a procedure de publicação)
GRANT EXECUTE ON PROCEDURE db_blog_artigos.SP_PUBLICAR_ARTIGO TO 'ROLE_AUTOR';
-- (Pode usar as funções)
GRANT EXECUTE ON FUNCTION db_blog_artigos.FN_GERA_ID_ARTIGO TO 'ROLE_AUTOR';
GRANT EXECUTE ON FUNCTION db_blog_artigos.FN_CONTA_ARTIGOS_USUARIO TO 'ROLE_AUTOR';

-- --- Permissões do ADMIN ---
-- (Tem tudo do AUTOR e pode gerenciar tudo)
GRANT 'ROLE_AUTOR' TO 'ROLE_ADMIN';
GRANT ALL PRIVILEGES ON db_blog_artigos.* TO 'ROLE_ADMIN' WITH GRANT OPTION;

-- --- Cria os Usuários da Aplicação ---
-- (Sua aplicação Java NÃO deve usar 'root'. Ela usará um destes)
CREATE USER IF NOT EXISTS 'app_leitor'@'localhost' IDENTIFIED BY 'senha_leitor_123';
CREATE USER IF NOT EXISTS 'app_autor'@'localhost' IDENTIFIED BY 'senha_autor_123';
CREATE USER IF NOT EXISTS 'app_admin'@'localhost' IDENTIFIED BY 'senha_admin_123';

-- --- Associa os Usuários às ROLES ---
GRANT 'ROLE_LEITOR' TO 'app_leitor'@'localhost';
GRANT 'ROLE_AUTOR' TO 'app_autor'@'localhost';
GRANT 'ROLE_ADMIN' TO 'app_admin'@'localhost';

-- Ativa as ROLES por padrão para esses usuários
SET DEFAULT ROLE ALL TO
  'app_leitor'@'localhost',
  'app_autor'@'localhost',
  'app_admin'@'localhost';

FLUSH PRIVILEGES;

-- -----------------------------------------------------
-- FIM DO SCRIPT
-- -----------------------------------------------------