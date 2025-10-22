Sistema de Gestão de Vendas (Desktop com Java Swing e MySQL)
Contexto Acadêmico
Aviso Importante: Este projeto foi desenvolvido como uma prova avaliativa para a disciplina de Tópicos Avançado II do curso de Analise e Desenvolvimento de Sistemas da Cruzeiro do Sul. O objetivo principal é demonstrar a aplicação prática dos conceitos de arquitetura de software (MVC, DAO), persistência de dados com banco de dados relacional (MySQL) e desenvolvimento de interfaces gráficas com Java Swing.

📖 Sobre o Projeto
Este é um projeto acadêmico que implementa um sistema de desktop para gestão de vendas, capaz de representar o Modelo Entidade-Relacionamento (MER) de uma nota fiscal. A aplicação foi desenvolvida em Java utilizando a biblioteca gráfica Swing e se conecta a um banco de dados MySQL para persistência de dados.

O sistema permite o gerenciamento completo de clientes e produtos, além da criação de novos pedidos, aplicando regras de negócio essenciais como controle de limite de crédito e atualização automática de estoque.

✨ Funcionalidades
Gerenciamento de Clientes:

Cadastro, alteração, listagem e exclusão de clientes (CRUD).

Definição de um Limite de Crédito para cada cliente.

Visualização em tempo real do Saldo Devedor.

Gerenciamento de Produtos:

Cadastro, alteração, listagem e exclusão de produtos (CRUD).

Controle de Estoque.

Gestão de Pedidos (Vendas):

Tela para criação de novos pedidos.

Seleção de clientes e produtos a partir dos dados do banco.

Adição e remoção de itens do "carrinho de compras" antes de finalizar.

Cálculo automático do valor total do pedido.

Histórico de Vendas:

Tela para visualizar todos os pedidos já realizados.

Ao selecionar um pedido, a tela exibe todos os itens daquela venda específica.

Funcionalidade para excluir um pedido do histórico.

Regras de Negócio Implementadas:

Controle de Crédito: O sistema bloqueia a finalização de um pedido se o saldo devedor atual do cliente somado ao valor da nova compra ultrapassar seu limite de crédito.

Atualização de Estoque: Ao finalizar um pedido, o sistema automaticamente dá baixa na quantidade de itens vendidos do estoque dos produtos correspondentes.

Exclusão em Cascata: O banco de dados foi configurado para manter a integridade dos dados. Ao excluir um cliente, todos os seus pedidos e itens são removidos automaticamente.

🏛️ Arquitetura: MVC + Persistência + DAO
O projeto foi estruturado seguindo uma arquitetura de camadas para separar as responsabilidades, facilitar a manutenção e a escalabilidade do código.

Model: Representa as entidades do negócio (Cliente, Produto, Pedido, Item). São classes Java simples (POJOs) que espelham as tabelas do banco de dados.

View: Camada de apresentação, responsável por toda a interface gráfica com o usuário. Construída com Java Swing (JFrame, JTable, etc.).

Control: O "cérebro" da aplicação. Contém as regras de negócio e faz a ponte entre a View e a camada de Persistence.

Persistence: Camada responsável por se comunicar com o banco de dados, executando as Stored Procedures e as consultas (SELECT).

DAO (Data Access Object): A classe ConexaoDAO é um exemplo deste padrão, responsável unicamente por estabelecer e fechar a conexão com o banco de dados.

🛠️ Tecnologias Utilizadas
Linguagem: Java (JDK 11 ou superior)

Banco de Dados: MySQL 8.0

Interface Gráfica: Java Swing

Gerenciador de Dependências: Apache Maven

IDE de Desenvolvimento: IntelliJ IDEA

Ferramenta de BD: MySQL Workbench

⚙️ Pré-requisitos
Antes de começar, você vai precisar ter instalado em sua máquina:

Java JDK (versão 11 ou superior)

Apache Maven

[link suspeito removido]

MySQL Workbench

Uma IDE Java, como o IntelliJ IDEA

🚀 Instalação e Execução
Siga os passos abaixo para rodar o projeto.

1. Configuração do Banco de Dados

Abra o MySQL Workbench e execute o script SQL abaixo. Ele irá criar o banco de dados, as tabelas, as relações e todas as Stored Procedures necessárias.

SQL

DROP SCHEMA IF EXISTS db_nota_fiscal;
CREATE SCHEMA db_nota_fiscal DEFAULT CHARACTER SET utf8;
USE db_nota_fiscal;

CREATE TABLE CLIENTE_01 (
    A01_codigo INT PRIMARY KEY AUTO_INCREMENT,
    A01_nome VARCHAR(100) NOT NULL,
    A01_endereco VARCHAR(200),
    A01_telefone VARCHAR(20),
    A01_cpf VARCHAR(14) UNIQUE,
    A01_credito DECIMAL(10, 2)
);

CREATE TABLE PRODUTO_03 (
    A03_codigo INT PRIMARY KEY AUTO_INCREMENT,
    A03_descricao VARCHAR(150) NOT NULL,
    A03_valor_produto DECIMAL(10, 2) NOT NULL,
    A03_estoque INT
);

CREATE TABLE PEDIDO_02 (
    A02_codigo INT PRIMARY KEY AUTO_INCREMENT,
    A02_data_venda DATE NOT NULL,
    A02_valor_total DECIMAL(10, 2),
    A01_codigo INT
);

CREATE TABLE ITEM_04 (
    A04_codigo INT PRIMARY KEY AUTO_INCREMENT,
    A04_quantidade INT NOT NULL,
    A04_valor_item DECIMAL(10, 2) NOT NULL,
    A02_codigo INT,
    A03_codigo INT
);


ALTER TABLE PEDIDO_02 
ADD CONSTRAINT FK_Pedido_Cliente FOREIGN KEY (A01_codigo)
REFERENCES CLIENTE_01(A01_codigo) ON DELETE CASCADE;

ALTER TABLE ITEM_04 
ADD CONSTRAINT FK_Item_Produto FOREIGN KEY (A03_codigo)
REFERENCES PRODUTO_03(A03_codigo) ON DELETE CASCADE;

ALTER TABLE ITEM_04 
ADD CONSTRAINT FK_Item_Pedido FOREIGN KEY (A02_codigo)
REFERENCES PEDIDO_02(A02_codigo) ON DELETE CASCADE;


DELIMITER $$


CREATE PROCEDURE PROC_INS_CLIENTE(
    IN p_nome VARCHAR(100),
    IN p_endereco VARCHAR(200),
    IN p_telefone VARCHAR(20),
    IN p_cpf VARCHAR(14),
    IN p_credito DECIMAL(10,2)
)
BEGIN
    INSERT INTO CLIENTE_01 (A01_nome, A01_endereco, A01_telefone, A01_cpf, A01_credito)
    VALUES (p_nome, p_endereco, p_telefone, p_cpf, p_credito);
END$$


CREATE PROCEDURE PROC_ALT_CLIENTE(
    IN p_codigo INT,
    IN p_nome VARCHAR(100),
    IN p_endereco VARCHAR(200),
    IN p_telefone VARCHAR(20),
    IN p_cpf VARCHAR(14),
    IN p_credito DECIMAL(10,2)
)
BEGIN
    UPDATE CLIENTE_01
    SET A01_nome = p_nome,
        A01_endereco = p_endereco,
        A01_telefone = p_telefone,
        A01_cpf = p_cpf,
        A01_credito = p_credito
    WHERE A01_codigo = p_codigo;
END$$


CREATE PROCEDURE PROC_DEL_CLIENTE(IN p_codigo INT)
BEGIN
    DELETE FROM CLIENTE_01 WHERE A01_codigo = p_codigo;
END$$



CREATE PROCEDURE PROC_INS_PRODUTO(
    IN p_descricao VARCHAR(150),
    IN p_valor DECIMAL(10,2),
    IN p_estoque INT
)
BEGIN
    INSERT INTO PRODUTO_03 (A03_descricao, A03_valor_produto, A03_estoque)
    VALUES (p_descricao, p_valor, p_estoque);
END$$


CREATE PROCEDURE PROC_ALT_PRODUTO(
    IN p_codigo INT,
    IN p_descricao VARCHAR(150),
    IN p_valor DECIMAL(10,2),
    IN p_estoque INT
)
BEGIN
    UPDATE PRODUTO_03
    SET A03_descricao = p_descricao,
        A03_valor_produto = p_valor,
        A03_estoque = p_estoque
    WHERE A03_codigo = p_codigo;
END$$


CREATE PROCEDURE PROC_DEL_PRODUTO(IN p_codigo INT)
BEGIN
    DELETE FROM PRODUTO_03 WHERE A03_codigo = p_codigo;
END$$



CREATE PROCEDURE PROC_INS_PEDIDO(
    IN p_data_venda DATE,
    IN p_valor_total DECIMAL(10,2),
    IN p_cliente_codigo INT
)
BEGIN
    INSERT INTO PEDIDO_02 (A02_data_venda, A02_valor_total, A01_codigo)
    VALUES (p_data_venda, p_valor_total, p_cliente_codigo);
END$$


CREATE PROCEDURE PROC_ALT_PEDIDO(
    IN p_codigo INT,
    IN p_data_venda DATE,
    IN p_valor_total DECIMAL(10,2),
    IN p_cliente_codigo INT
)
BEGIN
    UPDATE PEDIDO_02
    SET A02_data_venda = p_data_venda,
        A02_valor_total = p_valor_total,
        A01_codigo = p_cliente_codigo
    WHERE A02_codigo = p_codigo;
END$$


CREATE PROCEDURE PROC_DEL_PEDIDO(IN p_codigo INT)
BEGIN
    DELETE FROM PEDIDO_02 WHERE A02_codigo = p_codigo;
END$$



CREATE PROCEDURE PROC_INS_ITEM(
    IN p_quantidade INT,
    IN p_valor_item DECIMAL(10,2),
    IN p_pedido_codigo INT,
    IN p_produto_codigo INT
)
BEGIN
    INSERT INTO ITEM_04 (A04_quantidade, A04_valor_item, A02_codigo, A03_codigo)
    VALUES (p_quantidade, p_valor_item, p_pedido_codigo, p_produto_codigo);
END$$


CREATE PROCEDURE PROC_ALT_ITEM(
    IN p_codigo INT,
    IN p_quantidade INT,
    IN p_valor_item DECIMAL(10,2),
    IN p_pedido_codigo INT,
    IN p_produto_codigo INT
)
BEGIN
    UPDATE ITEM_04
    SET A04_quantidade = p_quantidade,
        A04_valor_item = p_valor_item,
        A02_codigo = p_pedido_codigo,
        A03_codigo = p_produto_codigo
    WHERE A04_codigo = p_codigo;
END$$


CREATE PROCEDURE PROC_DEL_ITEM(IN p_codigo INT)
BEGIN
    DELETE FROM ITEM_04 WHERE A04_codigo = p_codigo;
END$$



CREATE PROCEDURE PROC_ATUALIZA_ESTOQUE(
    IN p_produto_codigo INT,
    IN p_quantidade_vendida INT
)
BEGIN
    UPDATE PRODUTO_03
    SET A03_estoque = A03_estoque - p_quantidade_vendida
    WHERE A03_codigo = p_produto_codigo;
END$$


CREATE PROCEDURE PROC_CALCULA_SALDO_DEVEDOR(
    IN p_cliente_codigo INT,
    OUT p_saldo_devedor DECIMAL(10, 2)
)
BEGIN
    SELECT IFNULL(SUM(A02_valor_total), 0)
    INTO p_saldo_devedor
    FROM PEDIDO_02
    WHERE A01_codigo = p_cliente_codigo;
END$$

DELIMITER ;

2. Configuração do Projeto Java

Clone ou baixe o código-fonte do projeto.

Abra a pasta do projeto com o IntelliJ IDEA. O Maven irá baixar automaticamente as dependências (mysql-connector-java).

Importante: Abra o arquivo src/main/java/dao/ConexaoDAO.java e, se necessário, altere as constantes USER e PASSWORD para as credenciais do seu banco de dados MySQL.

3. Executando a Aplicação

O ponto de entrada do programa é a classe MenuView.

No IntelliJ, navegue até o arquivo src/main/java/view/MenuView.java.

Clique com o botão direito do mouse sobre o arquivo e selecione "Run 'MenuView.main()'".

A tela do menu principal será exibida.

## 📸 Screenshots da Aplicação

### Tela de Menu Principal
<img width="426" height="152" alt="menu" src="https://github.com/user-attachments/assets/0ff3fb13-71f4-46ca-a038-c2f5b0a70b11" />


### Gerenciamento de Clientes
<img width="934" height="831" alt="gerenciar-cliente" src="https://github.com/user-attachments/assets/6972a3e8-b4be-4de9-b23e-b9839894cbab" />



### gerenciamento de produto
<img width="935" height="704" alt="gerenciar-produto" src="https://github.com/user-attachments/assets/95fff623-a8b0-4c8a-a90f-3dd65226f214" />



### Tela de Novo Pedido
<img width="939" height="707" alt="fazer-pedidos" src="https://github.com/user-attachments/assets/83d0c2d9-4b02-42eb-9d41-c2dc7cf94ef9" />



### Histórico de Pedidos
<img width="1056" height="826" alt="historico" src="https://github.com/user-attachments/assets/914c5eea-865a-422f-9633-71b19caa562f" />



✒️ Autor
Desenvolvido por Thiago Fernandes Souza    isso esta no meu github, altera ele pra eu colocar como um explicaçao pro professor como que funciona, resumidamente, vou enviar em world a parte
