package view;

import control.ProdutoControl;
import model.ProdutoModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoView extends JFrame {

    // Componentes da Interface Gráfica (GUI)
    private JTextField txtDescricao, txtValor, txtEstoque, txtCodigo;
    private JButton btnCadastrar, btnAlterar, btnExcluir, btnLimpar;
    private JTable tblProdutos;
    private DefaultTableModel tableModel;

    // Objeto de Controle
    private ProdutoControl produtoControl;

    // --- CONSTRUTOR DA CLASSE ---
    public ProdutoView() {
        // Instancia o controle
        produtoControl = new ProdutoControl();

        // Configurações da Janela
        setTitle("Gerenciamento de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE_ON_CLOSE para não fechar o app inteiro
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO (SUPERIOR) ---
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        add(panelForm, BorderLayout.NORTH);

        panelForm.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        panelForm.add(txtCodigo);

        panelForm.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        panelForm.add(txtDescricao);

        panelForm.add(new JLabel("Valor (ex: 99.90):"));
        txtValor = new JTextField();
        panelForm.add(txtValor);

        panelForm.add(new JLabel("Estoque:"));
        txtEstoque = new JTextField();
        panelForm.add(txtEstoque);

        // --- PAINEL DOS BOTÕES (CENTRAL) ---
        JPanel panelButtons = new JPanel();
        add(panelButtons, BorderLayout.CENTER);

        btnCadastrar = new JButton("Cadastrar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar Campos");

        panelButtons.add(btnCadastrar);
        panelButtons.add(btnAlterar);
        panelButtons.add(btnExcluir);
        panelButtons.add(btnLimpar);

        // --- TABELA DE PRODUTOS (INFERIOR) ---
        tableModel = new DefaultTableModel(new Object[]{"Código", "Descrição", "Valor", "Estoque"}, 0);
        tblProdutos = new JTable(tableModel);
        add(new JScrollPane(tblProdutos), BorderLayout.SOUTH);

        // --- AÇÕES DOS BOTÕES E TABELA ---

        btnCadastrar.addActionListener(e -> cadastrarProduto());
        btnAlterar.addActionListener(e -> alterarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnLimpar.addActionListener(e -> limparCampos());

        tblProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblProdutos.getSelectedRow();
                if (selectedRow != -1) {
                    txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtDescricao.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtValor.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtEstoque.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        // Atualiza a tabela com os dados iniciais do banco
        atualizarTabela();
    }

    // --- MÉTODOS DE AÇÃO ---

    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<ProdutoModel> produtos = produtoControl.buscarTodosProdutos();
        for (ProdutoModel produto : produtos) {
            tableModel.addRow(new Object[]{
                    produto.getCodigo(),
                    produto.getDescricao(),
                    produto.getValorProduto(),
                    produto.getEstoque()
            });
        }
    }

    private void cadastrarProduto() {
        try {
            String descricao = txtDescricao.getText();
            BigDecimal valor = new BigDecimal(txtValor.getText());
            int estoque = Integer.parseInt(txtEstoque.getText());

            produtoControl.cadastrarProduto(descricao, valor, estoque);

            limparCampos();
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: 'Valor' e 'Estoque' devem ser números válidos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarProduto() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            String descricao = txtDescricao.getText();
            BigDecimal valor = new BigDecimal(txtValor.getText());
            int estoque = Integer.parseInt(txtEstoque.getText());

            produtoControl.atualizarProduto(codigo, descricao, valor, estoque);

            limparCampos();
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Selecione um produto na tabela para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto selecionado?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                produtoControl.excluirProduto(codigo);
                limparCampos();
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Selecione um produto na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        txtEstoque.setText("");
    }
}