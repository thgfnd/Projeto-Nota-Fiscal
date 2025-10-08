package persistence;

import dao.ConexaoDAO;
import model.PedidoModel;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoPersistence {

    private ConexaoDAO conexaoDAO;

    public PedidoPersistence() {
        this.conexaoDAO = new ConexaoDAO();
    }

    // --- MÉTODO PARA INSERIR UM PEDIDO ---
    public void inserirPedido(PedidoModel pedido) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_INS_PEDIDO(?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                // Converte LocalDate do Java para java.sql.Date para o banco
                ps.setDate(1, Date.valueOf(pedido.getDataVenda()));
                ps.setBigDecimal(2, pedido.getValorTotal());
                ps.setInt(3, pedido.getClienteCodigo());

                ps.execute();
                System.out.println("Pedido inserido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao inserir pedido: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA LISTAR TODOS OS PEDIDOS ---
    public List<PedidoModel> listarPedidos() {
        List<PedidoModel> listaPedidos = new ArrayList<>();
        if (conexaoDAO.conectar()) {
            try {
                String sql = "SELECT * FROM PEDIDO_02 ORDER BY A02_data_venda DESC";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    PedidoModel pedido = new PedidoModel();
                    pedido.setCodigo(rs.getInt("A02_codigo"));
                    // Converte java.sql.Date do banco para LocalDate do Java
                    pedido.setDataVenda(rs.getDate("A02_data_venda").toLocalDate());
                    pedido.setValorTotal(rs.getBigDecimal("A02_valor_total"));
                    pedido.setClienteCodigo(rs.getInt("A01_codigo"));
                    listaPedidos.add(pedido);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar pedidos: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return listaPedidos;
    }

    // --- MÉTODO PARA ALTERAR UM PEDIDO ---
    public void alterarPedido(PedidoModel pedido) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_ALT_PEDIDO(?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, pedido.getCodigo());
                ps.setDate(2, Date.valueOf(pedido.getDataVenda()));
                ps.setBigDecimal(3, pedido.getValorTotal());
                ps.setInt(4, pedido.getClienteCodigo());

                ps.execute();
                System.out.println("Pedido alterado com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao alterar pedido: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA REMOVER UM PEDIDO ---
    public void removerPedido(int codigo) {
        if (conexaoDAO.conectar()) {
            try {
                // Lembrar que a procedure PROC_DEL_PEDIDO também remove os itens associados
                String sql = "{CALL PROC_DEL_PEDIDO(?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ps.setInt(1, codigo);
                ps.execute();
                System.out.println("Pedido com código " + codigo + " removido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao remover pedido: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }


    }

    // --- MÉTODO PARA BUSCAR O SALDO DEVEDOR DE UM CLIENTE ---
    public BigDecimal buscarSaldoDevedorCliente(int clienteCodigo) {
        BigDecimal saldoDevedor = BigDecimal.ZERO;
        if (conexaoDAO.conectar()) {
            try {
                // Usamos CallableStatement por causa do parâmetro de saída (OUT)
                String sql = "{CALL PROC_CALCULA_SALDO_DEVEDOR(?, ?)}";
                java.sql.CallableStatement cs = conexaoDAO.connection.prepareCall(sql);

                // Define o parâmetro de entrada (IN)
                cs.setInt(1, clienteCodigo);
                // Registra o parâmetro de saída (OUT)
                cs.registerOutParameter(2, java.sql.Types.DECIMAL);

                cs.execute();

                // Pega o valor retornado pelo parâmetro de saída
                saldoDevedor = cs.getBigDecimal(2);

            } catch (SQLException e) {
                System.err.println("Erro ao buscar saldo devedor: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return saldoDevedor;
    }
}