package persistence;

import dao.ConexaoDAO;
import model.ItemModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemPersistence {

    private ConexaoDAO conexaoDAO;

    public ItemPersistence() {
        this.conexaoDAO = new ConexaoDAO();
    }

    // --- MÉTODO PARA INSERIR UM ITEM ---
    public void inserirItem(ItemModel item) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_INS_ITEM(?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, item.getQuantidade());
                ps.setBigDecimal(2, item.getValorItem());
                ps.setInt(3, item.getPedidoCodigo());
                ps.setInt(4, item.getProdutoCodigo());

                ps.execute();
                System.out.println("Item inserido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao inserir item: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA LISTAR ITENS DE UM PEDIDO ESPECÍFICO ---
    public List<ItemModel> listarItensPorPedido(int pedidoCodigo) {
        List<ItemModel> listaItens = new ArrayList<>();
        if (conexaoDAO.conectar()) {
            try {
                // Seleciona apenas os itens que pertencem ao pedido informado
                String sql = "SELECT * FROM ITEM_04 WHERE A02_codigo = ?";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ps.setInt(1, pedidoCodigo);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ItemModel item = new ItemModel();
                    item.setCodigo(rs.getInt("A04_codigo"));
                    item.setQuantidade(rs.getInt("A04_quantidade"));
                    item.setValorItem(rs.getBigDecimal("A04_valor_item"));
                    item.setPedidoCodigo(rs.getInt("A02_codigo"));
                    item.setProdutoCodigo(rs.getInt("A03_codigo"));
                    listaItens.add(item);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar itens do pedido: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return listaItens;
    }

    // --- MÉTODO PARA ALTERAR UM ITEM ---
    public void alterarItem(ItemModel item) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_ALT_ITEM(?, ?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, item.getCodigo());
                ps.setInt(2, item.getQuantidade());
                ps.setBigDecimal(3, item.getValorItem());
                ps.setInt(4, item.getPedidoCodigo());
                ps.setInt(5, item.getProdutoCodigo());

                ps.execute();
                System.out.println("Item alterado com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao alterar item: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA REMOVER UM ITEM ---
    public void removerItem(int codigo) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_DEL_ITEM(?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ps.setInt(1, codigo);
                ps.execute();
                System.out.println("Item com código " + codigo + " removido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao remover item: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }
}