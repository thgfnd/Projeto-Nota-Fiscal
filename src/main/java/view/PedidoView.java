package view;

import control.ClienteControl;
import control.PedidoControl;
import control.ProdutoControl;
import model.ClienteModel;
import model.ItemModel;
import model.ProdutoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PedidoView extends JFrame {

    // Controles
    private PedidoControl pedidoControl;
    private ClienteControl clienteControl;
    private ProdutoControl produtoControl;

    // Componentes Visuais
    private JComboBox<ClienteModel> cmbClientes;
    private JComboBox<ProdutoModel> cmbProdutos;
    private JTextField txtQuantidade;
    private JButton btnAdicionarItem, btnFinalizarPedido, btnRemoverItem; // Botão de remover adicionado aqui
    private JTable tblItens;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;

    // Lista para guardar os itens do pedido atual
    private List<ItemModel> itensDoPedido;

    public PedidoView() {
        // Inicializa controles e listas
        pedidoControl = new PedidoControl();
        clienteControl = new ClienteControl();
        produtoControl = new ProdutoControl();
        itensDoPedido = new ArrayList<>();

        // Configurações da Janela
        setTitle("Novo Pedido");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL SUPERIOR: Seleção de Cliente e Produto ---
        JPanel panelTop = new JPanel(new GridLayout(3, 2, 10, 10));
        add(panelTop, BorderLayout.NORTH);

        panelTop.add(new JLabel("Selecione o Cliente:"));
        cmbClientes = new JComboBox<>();
        panelTop.add(cmbClientes);

        panelTop.add(new JLabel("Selecione o Produto:"));
        cmbProdutos = new JComboBox<>();
        panelTop.add(cmbProdutos);

        panelTop.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField("1");
        panelTop.add(txtQuantidade);

        // --- PAINEL CENTRAL: Botões de Ação do Carrinho ---
        JPanel panelCenter = new JPanel();
        btnAdicionarItem = new JButton("Adicionar Produto ao Pedido");
        btnRemoverItem = new JButton("Remover Produto do Pedido"); // Botão criado
        panelCenter.add(btnAdicionarItem);
        panelCenter.add(btnRemoverItem); // Botão adicionado ao painel
        add(panelCenter, BorderLayout.CENTER);

        // --- PAINEL INFERIOR: Tabela de Itens e Total ---
        JPanel panelBottom = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Valor Unit.", "Subtotal"}, 0);
        tblItens = new JTable(tableModel);
        panelBottom.add(new JScrollPane(tblItens), BorderLayout.CENTER);

        lblTotal = new JLabel("Valor Total do Pedido: R$ 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelBottom.add(lblTotal, BorderLayout.SOUTH);
        add(panelBottom, BorderLayout.SOUTH);

        // --- PAINEL DIREITO: Finalizar Pedido ---
        JPanel panelRight = new JPanel();
        btnFinalizarPedido = new JButton("Finalizar Pedido");
        panelRight.add(btnFinalizarPedido);
        add(panelRight, BorderLayout.EAST);

        // Carrega os dados iniciais
        carregarClientes();
        carregarProdutos();

        // --- AÇÕES DOS BOTÕES ---
        btnAdicionarItem.addActionListener(e -> adicionarItem());
        btnFinalizarPedido.addActionListener(e -> finalizarPedido());
        btnRemoverItem.addActionListener(e -> removerItem()); // Ação do novo botão
    }

    private void carregarClientes() {
        List<ClienteModel> clientes = clienteControl.buscarTodosClientes();
        for (ClienteModel cliente : clientes) {
            cmbClientes.addItem(cliente);
        }
        cmbClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClienteModel) {
                    setText(((ClienteModel) value).getNome());
                }
                return this;
            }
        });
    }

    private void carregarProdutos() {
        List<ProdutoModel> produtos = produtoControl.buscarTodosProdutos();
        for (ProdutoModel produto : produtos) {
            cmbProdutos.addItem(produto);
        }
        cmbProdutos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ProdutoModel) {
                    setText(((ProdutoModel) value).getDescricao());
                }
                return this;
            }
        });
    }

    private void adicionarItem() {
        try {
            ProdutoModel produtoSelecionado = (ProdutoModel) cmbProdutos.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            if (produtoSelecionado == null || quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Selecione um produto e informe uma quantidade válida.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BigDecimal valorUnitario = produtoSelecionado.getValorProduto();
            BigDecimal subtotal = valorUnitario.multiply(new BigDecimal(quantidade));

            tableModel.addRow(new Object[]{
                    produtoSelecionado.getDescricao(),
                    quantidade,
                    valorUnitario,
                    subtotal
            });

            ItemModel novoItem = new ItemModel();
            novoItem.setProdutoCodigo(produtoSelecionado.getCodigo());
            novoItem.setQuantidade(quantidade);
            novoItem.setValorItem(subtotal);
            itensDoPedido.add(novoItem);

            atualizarTotal();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A quantidade deve ser um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- NOVO MÉTODO PARA REMOVER ITEM ---
    private void removerItem() {
        int selectedRow = tblItens.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um item na tabela para remover.", "Nenhum Item Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        itensDoPedido.remove(selectedRow);
        tableModel.removeRow(selectedRow);
        atualizarTotal();
        JOptionPane.showMessageDialog(this, "Item removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void atualizarTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemModel item : itensDoPedido) {
            total = total.add(item.getValorItem());
        }
        lblTotal.setText("Valor Total do Pedido: R$ " + total);
    }

    private void finalizarPedido() {
        ClienteModel clienteSelecionado = (ClienteModel) cmbClientes.getSelectedItem();
        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (itensDoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um item ao pedido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = pedidoControl.criarNovoPedido(clienteSelecionado.getCodigo(), itensDoPedido);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Pedido finalizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparTela();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao finalizar o pedido.\nVerifique se o cliente possui crédito suficiente.", "Crédito Insuficiente", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparTela() {
        tableModel.setRowCount(0);
        itensDoPedido.clear();
        txtQuantidade.setText("1");
        atualizarTotal();
        cmbClientes.setSelectedIndex(0);
        cmbProdutos.setSelectedIndex(0);
    }
}