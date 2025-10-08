package persistence;

import dao.ConexaoDAO;
import model.ClienteModel;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientePersistence {

    private ConexaoDAO conexaoDAO;

    // Construtor da classe
    public ClientePersistence() {
        this.conexaoDAO = new ConexaoDAO();
    }

    // --- MÉTODO PARA INSERIR UM CLIENTE ---
    public void inserirCliente(ClienteModel cliente) {
        // Tenta conectar ao banco de dados
        if (conexaoDAO.conectar()) {
            try {
                // Prepara a chamada para a procedure PROC_INS_CLIENTE
                String sql = "{CALL PROC_INS_CLIENTE(?, ?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                // Define os parâmetros da procedure
                ps.setString(1, cliente.getNome());
                ps.setString(2, cliente.getEndereco());
                ps.setString(3, cliente.getTelefone());
                ps.setString(4, cliente.getCpf());
                ps.setBigDecimal(5, cliente.getCredito());

                // Executa a procedure
                ps.execute();
                System.out.println("Cliente '" + cliente.getNome() + "' inserido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao inserir cliente: " + e.getMessage());
            } finally {
                // Desconecta do banco de dados, ocorrendo erro ou não
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA LISTAR TODOS OS CLIENTES ---
    public List<ClienteModel> listarClientes() {
        List<ClienteModel> listaClientes = new ArrayList<>();
        if (conexaoDAO.conectar()) {
            try {
                // Prepara a consulta SQL para selecionar todos os clientes
                String sql = "SELECT * FROM CLIENTE_01 ORDER BY A01_nome";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                // Itera sobre o resultado da consulta
                while (rs.next()) {
                    ClienteModel cliente = new ClienteModel();
                    cliente.setCodigo(rs.getInt("A01_codigo"));
                    cliente.setNome(rs.getString("A01_nome"));
                    cliente.setEndereco(rs.getString("A01_endereco"));
                    cliente.setTelefone(rs.getString("A01_telefone"));
                    cliente.setCpf(rs.getString("A01_cpf"));
                    cliente.setCredito(rs.getBigDecimal("A01_credito"));
                    listaClientes.add(cliente);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar clientes: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return listaClientes;
    }

    // --- MÉTODO PARA ALTERAR UM CLIENTE ---
    public void alterarCliente(ClienteModel cliente) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_ALT_CLIENTE(?, ?, ?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                // Define os parâmetros da procedure, incluindo o código
                ps.setInt(1, cliente.getCodigo());
                ps.setString(2, cliente.getNome());
                ps.setString(3, cliente.getEndereco());
                ps.setString(4, cliente.getTelefone());
                ps.setString(5, cliente.getCpf());
                ps.setBigDecimal(6, cliente.getCredito());

                ps.execute();
                System.out.println("Cliente '" + cliente.getNome() + "' alterado com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao alterar cliente: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA REMOVER UM CLIENTE ---
    public void removerCliente(int codigo) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_DEL_CLIENTE(?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                // Define o código do cliente a ser removido
                ps.setInt(1, codigo);

                ps.execute();
                System.out.println("Cliente com código " + codigo + " removido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao remover cliente: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA BUSCAR UM ÚNICO CLIENTE PELO CÓDIGO ---
    public ClienteModel buscarClientePorCodigo(int codigo) {
        ClienteModel cliente = null;
        if (conexaoDAO.conectar()) {
            try {
                String sql = "SELECT * FROM CLIENTE_01 WHERE A01_codigo = ?";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ps.setInt(1, codigo);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    cliente = new ClienteModel();
                    cliente.setCodigo(rs.getInt("A01_codigo"));
                    cliente.setNome(rs.getString("A01_nome"));
                    cliente.setEndereco(rs.getString("A01_endereco"));
                    cliente.setTelefone(rs.getString("A01_telefone"));
                    cliente.setCpf(rs.getString("A01_cpf"));

                    // --- CORREÇÃO APLICADA AQUI ---
                    // Pega o valor do crédito do banco
                    BigDecimal credito = rs.getBigDecimal("A01_credito");
                    // Verifica se o valor é nulo. Se for, usa ZERO. Se não, usa o valor do banco.
                    cliente.setCredito(credito == null ? BigDecimal.ZERO : credito);
                    // --- FIM DA CORREÇÃO ---
                }
            } catch (SQLException e) {
                System.err.println("Erro ao buscar cliente por código: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return cliente;
    }
}