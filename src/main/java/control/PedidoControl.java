package control;

import model.ClienteModel; // IMPORT ADICIONADO
import model.ItemModel;
import model.PedidoModel;
import persistence.ClientePersistence; // IMPORT ADICIONADO
import persistence.ItemPersistence;
import persistence.PedidoPersistence;
import persistence.ProdutoPersistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PedidoControl {

    private PedidoPersistence pedidoPersistence;
    private ItemPersistence itemPersistence;
    private ProdutoPersistence produtoPersistence;

    public PedidoControl() {
        this.pedidoPersistence = new PedidoPersistence();
        this.itemPersistence = new ItemPersistence();
        this.produtoPersistence = new ProdutoPersistence();
    }

    public boolean criarNovoPedido(int clienteCodigo, List<ItemModel> itens) {
        if (itens == null || itens.isEmpty()) {
            System.err.println("Erro: Um pedido não pode ser criado sem itens.");
            return false;
        }

        ClientePersistence clientePersistence = new ClientePersistence();
        ClienteModel cliente = clientePersistence.buscarClientePorCodigo(clienteCodigo);

        // Adicionando verificação para caso o cliente não seja encontrado
        if (cliente == null) {
            System.err.println("Erro: Cliente com o código " + clienteCodigo + " não encontrado.");
            return false;
        }

        BigDecimal limiteCredito = cliente.getCredito();
        BigDecimal saldoDevedor = pedidoPersistence.buscarSaldoDevedorCliente(clienteCodigo);
        BigDecimal valorNovoPedido = BigDecimal.ZERO;

        for (ItemModel item : itens) {
            valorNovoPedido = valorNovoPedido.add(item.getValorItem());
        }

        if (saldoDevedor.add(valorNovoPedido).compareTo(limiteCredito) > 0) {
            System.err.println("CRÉDITO INSUFICIENTE. Limite: " + limiteCredito + ", Saldo Devedor: " + saldoDevedor + ", Valor do Pedido: " + valorNovoPedido);
            return false;
        }

        PedidoModel novoPedido = new PedidoModel();
        novoPedido.setClienteCodigo(clienteCodigo);
        novoPedido.setDataVenda(LocalDate.now());
        novoPedido.setValorTotal(valorNovoPedido);
        pedidoPersistence.inserirPedido(novoPedido);

        List<PedidoModel> todosPedidos = pedidoPersistence.listarPedidos();
        int ultimoPedidoCodigo = todosPedidos.get(0).getCodigo();

        for (ItemModel item : itens) {
            item.setPedidoCodigo(ultimoPedidoCodigo);
            itemPersistence.inserirItem(item);
            produtoPersistence.atualizarEstoque(item.getProdutoCodigo(), item.getQuantidade());
        }

        System.out.println("Pedido criado com sucesso e estoque atualizado!");
        return true;
    }

    public List<PedidoModel> buscarTodosPedidos() {
        return pedidoPersistence.listarPedidos();
    }



    public List<ItemModel> buscarItensPorPedido(int pedidoCodigo) {
        return itemPersistence.listarItensPorPedido(pedidoCodigo);
    }

    public void excluirPedido(int codigo) {
        if (codigo <= 0) {
            System.err.println("Erro: Código do pedido inválido.");
            return;
        }
        pedidoPersistence.removerPedido(codigo);
    }
}