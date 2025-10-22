package view;

import control.PedidoControl;
import model.ItemModel;
import model.PedidoModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoricoPedidosView extends JFrame {

    // Controle
    private PedidoControl pedidoControl;

    // Componentes Visuais
    private JTable tblPedidos;
    private JTable tblItensPedido;
    private DefaultTableModel tableModelPedidos;
    private DefaultTableModel tableModelItens;
    private JButton btnExcluirPedido;

    public HistoricoPedidosView() {
        pedidoControl = new PedidoControl();

        // Configurações da Janela
        setTitle("Histórico de Pedidos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Padding geral na janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL PRINCIPAL DIVIDIDO ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);

        // --- PAINEL SUPERIOR: Tabela de Pedidos ---
        JPanel panelPedidos = new JPanel(new BorderLayout(10, 10));
        Border titledPedidos = BorderFactory.createTitledBorder("Todos os Pedidos");
        Border paddingPedidos = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelPedidos.setBorder(BorderFactory.createCompoundBorder(titledPedidos, paddingPedidos));

        // --- CORREÇÃO 1 AQUI ---
        tableModelPedidos = new DefaultTableModel(new Object[]{"Cód. Pedido", "Data", "Valor Total", "Cód. Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //

        tblPedidos = new JTable(tableModelPedidos);
        panelPedidos.add(new JScrollPane(tblPedidos), BorderLayout.CENTER);

        // --- PAINEL INFERIOR: Tabela de Itens do Pedido Selecionado ---
        JPanel panelItens = new JPanel(new BorderLayout(10, 10));
        Border titledItens = BorderFactory.createTitledBorder("Itens do Pedido Selecionado");
        Border paddingItens = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panelItens.setBorder(BorderFactory.createCompoundBorder(titledItens, paddingItens));

        // --- CORREÇÃO 2 AQUI ---
        tableModelItens = new DefaultTableModel(new Object[]{"Cód. Item", "Cód. Produto", "Quantidade", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //

        tblItensPedido = new JTable(tableModelItens);
        panelItens.add(new JScrollPane(tblItensPedido), BorderLayout.CENTER);

        JPanel panelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExcluirPedido = new JButton("Excluir Pedido Selecionado");
        panelExcluir.add(btnExcluirPedido);
        panelItens.add(panelExcluir, BorderLayout.SOUTH);

        // Adiciona os painéis ao SplitPane
        splitPane.setTopComponent(panelPedidos);
        splitPane.setBottomComponent(panelItens);
        splitPane.setResizeWeight(0.5);

        // --- AÇÃO AO CLICAR EM UM PEDIDO ---
        tblPedidos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tblPedidos.getSelectedRow();
                if (selectedRow != -1) {
                    int pedidoCodigo = (int) tableModelPedidos.getValueAt(selectedRow, 0);
                    carregarItensDoPedido(pedidoCodigo);
                }
            }
        });

        // --- AÇÃO DO BOTÃO EXCLUIR PEDIDO ---
        btnExcluirPedido.addActionListener(e -> {
            int selectedRow = tblPedidos.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Por favor, selecione um pedido na tabela de cima para excluir.", "Nenhum Pedido Selecionado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int pedidoCodigo = (int) tableModelPedidos.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o pedido " + pedidoCodigo + "?\nTodos os seus itens também serão removidos.", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                pedidoControl.excluirPedido(pedidoCodigo);
                JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarPedidos();
                tableModelItens.setRowCount(0);
            }
        });

        // Carrega os dados iniciais
        carregarPedidos();
        setLocationRelativeTo(null); // Centraliza
    }

    // --- MÉTODOS DE AÇÃO

    private void carregarPedidos() {
        tableModelPedidos.setRowCount(0);
        java.util.List<PedidoModel> pedidos = pedidoControl.buscarTodosPedidos();
        for (PedidoModel pedido : pedidos) {
            tableModelPedidos.addRow(new Object[]{
                    pedido.getCodigo(),
                    pedido.getDataVenda(),
                    pedido.getValorTotal(),
                    pedido.getClienteCodigo()
            });
        }
    }

    private void carregarItensDoPedido(int pedidoCodigo) {
        tableModelItens.setRowCount(0);
        java.util.List<ItemModel> itens = pedidoControl.buscarItensPorPedido(pedidoCodigo);
        for (ItemModel item : itens) {
            tableModelItens.addRow(new Object[]{
                    item.getCodigo(),
                    item.getProdutoCodigo(),
                    item.getQuantidade(),
                    item.getValorItem()
            });
        }
    }
}