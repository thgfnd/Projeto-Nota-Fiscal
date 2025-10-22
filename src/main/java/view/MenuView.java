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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout 2x2
        setLayout(new GridLayout(2, 2, 10, 10));

        // --- MUDANÇA MODERNA: Adiciona "padding" (espaçamento interno) ---
        // Cria uma borda de 15 pixels em volta de todos os componentes
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Inicializa os botões
        btnGerenciarClientes = new JButton("Gerenciar Clientes");
        btnGerenciarProdutos = new JButton("Gerenciar Produtos");
        btnNovoPedido = new JButton("Novo Pedido");
        btnHistoricoPedidos = new JButton("Ver Histórico de Pedidos");

        // Adiciona os botões à janela
        add(btnGerenciarClientes);
        add(btnGerenciarProdutos);
        add(btnNovoPedido);
        add(btnHistoricoPedidos);

        // --- AÇÕES DOS BOTÕES (COM THREAD SAFETY) ---

        // Ação para abrir a tela de gerenciamento de clientes
        btnGerenciarClientes.addActionListener(e -> {
            // --- MUDANÇA MODERNA: Garante que a nova janela abra na Thread do Swing ---
            SwingUtilities.invokeLater(() -> {
                ClienteView clienteView = new ClienteView();
                clienteView.setVisible(true);
            });
        });

        // Ação para abrir a tela de gerenciamento de produtos
        btnGerenciarProdutos.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ProdutoView produtoView = new ProdutoView();
                produtoView.setVisible(true);
            });
        });

        // Ação para abrir a tela de novo pedido
        btnNovoPedido.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                PedidoView pedidoView = new PedidoView();
                pedidoView.setVisible(true);
            });
        });

        // Ação para abrir a tela de histórico
        btnHistoricoPedidos.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                HistoricoPedidosView historicoView = new HistoricoPedidosView();
                historicoView.setVisible(true);
            });
        });

        // --- FINALIZAÇÃO DA JANELA ---
        pack(); // Ajusta o tamanho da janela para caber o conteúdo
        setLocationRelativeTo(null); // Centraliza a janela
    }

    // --- MÉTODO PRINCIPAL PARA INICIAR O PROGRAMA ---
    public static void main(String[] args) {
        // --- MUDANÇA MODERNA: Define o Look and Feel "Nimbus" ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se o Nimbus não estiver disponível, usa o padrão do sistema
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Garante que a interface gráfica seja executada na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            MenuView menu = new MenuView();
            menu.setVisible(true);
        });
    }
}