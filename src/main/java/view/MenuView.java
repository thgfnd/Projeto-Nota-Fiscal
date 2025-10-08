package view;

import javax.swing.*;
import java.awt.*;

public class MenuView extends JFrame {

    // Botões para as funcionalidades do sistema
    private JButton btnGerenciarClientes;
    private JButton btnGerenciarProdutos;
    private JButton btnNovoPedido;
    private JButton btnHistoricoPedidos;

    // --- CONSTRUTOR DA CLASSE ---
    public MenuView() {
        // Configurações da Janela Principal
        setTitle("Menu Principal - Sistema de Nota Fiscal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Agora esta é a janela que fecha tudo
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new GridLayout(4, 1, 10, 10)); // Mude de 3 para 4 // Layout simples com 3 linhas

        // Inicializa os botões
        btnGerenciarClientes = new JButton("Gerenciar Clientes");
        btnGerenciarProdutos = new JButton("Gerenciar Produtos");
        btnNovoPedido = new JButton("Novo Pedido");
        btnHistoricoPedidos = new JButton("Ver Histórico de Pedidos");
        add(btnHistoricoPedidos);

        // Adiciona os botões à janela
        add(btnGerenciarClientes);
        add(btnGerenciarProdutos);
        add(btnNovoPedido);

        // --- AÇÕES DOS BOTÕES ---

        // Ação para abrir a tela de gerenciamento de clientes
        btnGerenciarClientes.addActionListener(e -> {
            // Cria e exibe a tela de clientes
            ClienteView clienteView = new ClienteView();
            clienteView.setVisible(true);
        });

        // Ação para abrir a tela de gerenciamento de produtos
        btnGerenciarProdutos.addActionListener(e -> {
            // Cria e exibe a tela de produtos
            ProdutoView produtoView = new ProdutoView();
            produtoView.setVisible(true);
        });

        // Ação para abrir a tela de novo pedido
        btnNovoPedido.addActionListener(e -> {
            PedidoView pedidoView = new PedidoView();
            pedidoView.setVisible(true);
        });

        // Ação para abrir a tela de histórico
        btnHistoricoPedidos.addActionListener(e -> {
            HistoricoPedidosView historicoView = new HistoricoPedidosView();
            historicoView.setVisible(true);
        });
    }

    // --- MÉTODO PRINCIPAL PARA INICIAR O PROGRAMA ---
    public static void main(String[] args) {
        // Garante que a interface gráfica seja executada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            MenuView menu = new MenuView();
            menu.setVisible(true);
        });
    }
}