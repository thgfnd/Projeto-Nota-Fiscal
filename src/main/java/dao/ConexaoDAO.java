package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDAO {

    // Declaração do objeto de conexão que será usado em toda a classe
    public Connection connection = null;

    // Constantes com as informações de conexão ao banco de dados
    // O nome do nosso banco de dados
    private static final String DATABASE_NAME = "db_nota_fiscal";
    // O URL de conexão JDBC para o MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/" + DATABASE_NAME + "?useTimezone=true&serverTimezone=UTC";
    // O usuário do banco de dados (o padrão geralmente é 'root')
    private static final String USER = "root";
    // A senha do banco de dados (altere se a sua for diferente)
    private static final String PASSWORD = "root";
    // O driver JDBC do MySQL que adicionamos no pom.xml
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Método responsável por estabelecer a conexão com o banco de dados.
     * @return true se a conexão for bem-sucedida, false caso contrário.
     */
    public boolean conectar() {
        try {
            // Carrega o driver JDBC na memória
            Class.forName(DRIVER);
            // Estabelece a conexão usando o DriverManager e as constantes definidas
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver do banco de dados não encontrado. Verifique se o conector foi adicionado ao pom.xml.");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Método responsável por fechar a conexão com o banco de dados.
     */
    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão com o banco de dados.");
            e.printStackTrace();
        }
    }
}