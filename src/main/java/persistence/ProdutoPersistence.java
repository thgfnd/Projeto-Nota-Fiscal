package persistence;

import dao.ConexaoDAO;
import model.ProdutoModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoPersistence {

    private ConexaoDAO conexaoDAO;

    public ProdutoPersistence() {
        this.conexaoDAO = new ConexaoDAO();
    }

    // --- MÉTODO PARA INSERIR UM PRODUTO ---
    public void inserirProduto(ProdutoModel produto) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_INS_PRODUTO(?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setString(1, produto.getDescricao());
                ps.setBigDecimal(2, produto.getValorProduto());
                ps.setInt(3, produto.getEstoque());

                ps.execute();
                System.out.println("Produto '" + produto.getDescricao() + "' inserido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao inserir produto: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA LISTAR TODOS OS PRODUTOS ---
    public List<ProdutoModel> listarProdutos() {
        List<ProdutoModel> listaProdutos = new ArrayList<>();
        if (conexaoDAO.conectar()) {
            try {
                String sql = "SELECT * FROM PRODUTO_03 ORDER BY A03_descricao";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ProdutoModel produto = new ProdutoModel();
                    produto.setCodigo(rs.getInt("A03_codigo"));
                    produto.setDescricao(rs.getString("A03_descricao"));
                    produto.setValorProduto(rs.getBigDecimal("A03_valor_produto"));
                    produto.setEstoque(rs.getInt("A03_estoque"));
                    listaProdutos.add(produto);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao listar produtos: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
        return listaProdutos;
    }

    // --- MÉTODO PARA ALTERAR UM PRODUTO ---
    public void alterarProduto(ProdutoModel produto) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_ALT_PRODUTO(?, ?, ?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, produto.getCodigo());
                ps.setString(2, produto.getDescricao());
                ps.setBigDecimal(3, produto.getValorProduto());
                ps.setInt(4, produto.getEstoque());

                ps.execute();
                System.out.println("Produto '" + produto.getDescricao() + "' alterado com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao alterar produto: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA REMOVER UM PRODUTO ---
    public void removerProduto(int codigo) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_DEL_PRODUTO(?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, codigo);

                ps.execute();
                System.out.println("Produto com código " + codigo + " removido com sucesso!");

            } catch (SQLException e) {
                System.err.println("Erro ao remover produto: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }

    // --- MÉTODO PARA ATUALIZAR O ESTOQUE ---
    public void atualizarEstoque(int produtoCodigo, int quantidadeVendida) {
        if (conexaoDAO.conectar()) {
            try {
                String sql = "{CALL PROC_ATUALIZA_ESTOQUE(?, ?)}";
                PreparedStatement ps = conexaoDAO.connection.prepareStatement(sql);

                ps.setInt(1, produtoCodigo);
                ps.setInt(2, quantidadeVendida);

                ps.execute();
                // A mensagem de sucesso aqui não é tão necessária, para não poluir o console.

            } catch (SQLException e) {
                System.err.println("Erro ao atualizar o estoque: " + e.getMessage());
            } finally {
                conexaoDAO.desconectar();
            }
        }
    }
}