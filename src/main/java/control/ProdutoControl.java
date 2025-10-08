package control;

import model.ProdutoModel;
import persistence.ProdutoPersistence;

import java.math.BigDecimal;
import java.util.List;

public class ProdutoControl {

    private ProdutoPersistence produtoPersistence;

    public ProdutoControl() {
        this.produtoPersistence = new ProdutoPersistence();
    }

    public void cadastrarProduto(String descricao, BigDecimal valor, int estoque) {
        if (descricao == null || descricao.trim().isEmpty()) {
            System.err.println("Erro: A descrição do produto não pode ser vazia.");
            return;
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Erro: O valor do produto não pode ser negativo.");
            return;
        }

        ProdutoModel novoProduto = new ProdutoModel();
        novoProduto.setDescricao(descricao);
        novoProduto.setValorProduto(valor);
        novoProduto.setEstoque(estoque);

        produtoPersistence.inserirProduto(novoProduto);
    }

    public List<ProdutoModel> buscarTodosProdutos() {
        return produtoPersistence.listarProdutos();
    }

    public void atualizarProduto(int codigo, String descricao, BigDecimal valor, int estoque) {
        if (codigo <= 0) {
            System.err.println("Erro: Código do produto inválido.");
            return;
        }

        ProdutoModel produtoAtualizado = new ProdutoModel();
        produtoAtualizado.setCodigo(codigo);
        produtoAtualizado.setDescricao(descricao);
        produtoAtualizado.setValorProduto(valor);
        produtoAtualizado.setEstoque(estoque);

        produtoPersistence.alterarProduto(produtoAtualizado);
    }

    public void excluirProduto(int codigo) {
        if (codigo <= 0) {
            System.err.println("Erro: Código do produto inválido.");
            return;
        }
        produtoPersistence.removerProduto(codigo);
    }
}