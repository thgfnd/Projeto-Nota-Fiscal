package view;

import control.ClienteControl;
import control.PedidoControl;
import control.ProdutoControl;
import model.ClienteModel;
import model.ItemModel;
import model.ProdutoModel;

import javax.swing.*;
import javax.swing.border.Border;
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
    private JButton btnAdicionarItem, btnFinalizarPedido, btnRemoverItem;
    private JTable tblItens;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;

    // Lista
    private List<ItemModel> itensDoPedido;

    public PedidoView() {
        pedidoControl = new PedidoControl();
        clienteControl = new ClienteControl();
        produtoControl = new ProdutoControl();
        itensDoPedido = new ArrayList<>();

        // Configurações da Janela
        setTitle("Novo Pedido");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Padding geral na janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL SUPERIOR: Seleção de Cliente e Produto ---
        JPanel panelTop = new JPanel(new BorderLayout(10, 10));
        Border paddingForm = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledForm = BorderFactory.createTitledBorder("Dados da Venda");
        panelTop.setBorder(BorderFactory.createCompoundBorder(titledForm, paddingForm));

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.add(new JLabel("Selecione o Cliente:"));
        cmbClientes = new JComboBox<>();
        panelCampos.add(cmbClientes);

        panelCampos.add(new JLabel("Selecione o Produto:"));
        cmbProdutos = new JComboBox<>();
        panelCampos.add(cmbProdutos);

        panelCampos.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField("1");
        panelCampos.add(txtQuantidade);

        panelTop.add(panelCampos, BorderLayout.CENTER);

        // Painel de botões de adicionar/remover item
        JPanel panelAcoesItem = new JPanel();
        btnAdicionarItem = new JButton("Adicionar Produto");
        btnRemoverItem = new JButton("Remover Produto");
        panelAcoesItem.add(btnAdicionarItem);
        panelAcoesItem.add(btnRemoverItem);
        panelTop.add(panelAcoesItem, BorderLayout.SOUTH);

        add(panelTop, BorderLayout.NORTH);

        // --- PAINEL CENTRAL: Tabela de Itens ---
        JPanel panelCenter = new JPanel(new BorderLayout());
        Border paddingTable = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledTable = BorderFactory.createTitledBorder("Itens do Pedido (Carrinho)");
        panelCenter.setBorder(BorderFactory.createCompoundBorder(titledTable, paddingTable));

        // --- CORREÇÃO AQUI ---
        // Instancia o DefaultTableModel sobrescrevendo o método isCellEditable
        tableModel = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Valor Unit.", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Impede que qualquer célula da tabela seja editada
                return false;
            }
        };
        // --- FIM DA CORREÇÃO ---

        tblItens = new JTable(tableModel);
        panelCenter.add(new JScrollPane(tblItens), BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);

        // --- PAINEL INFERIOR: Total e Finalizar ---
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        lblTotal = new JLabel("Valor Total do Pedido: R$ 0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
        panelBottom.add(lblTotal, BorderLayout.CENTER);

        btnFinalizarPedido = new JButton("Finalizar Pedido");
        btnFinalizarPedido.setFont(new Font("Arial", Font.BOLD, 14));
        panelBottom.add(btnFinalizarPedido, BorderLayout.EAST);

        add(panelBottom, BorderLayout.SOUTH);

        // Carrega os dados iniciais
        carregarClientes();
        carregarProdutos();

        // --- AÇÕES DOS BOTÕES ---
        btnAdicionarItem.addActionListener(e -> adicionarItem());
        btnFinalizarPedido.addActionListener(e -> finalizarPedido());
        btnRemoverItem.addActionListener(e -> removerItem());

        setLocationRelativeTo(null); // Centraliza
    }

    // --- MÉTODOS DE RENDERIZAÇÃO E AÇÕES (sem alterações) ---

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