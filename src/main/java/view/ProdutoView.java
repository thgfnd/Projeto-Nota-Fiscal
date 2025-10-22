package view;

import control.ProdutoControl;
import model.ProdutoModel;

import javax.swing.*;
import javax.swing.border.Border;
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
        produtoControl = new ProdutoControl();

        // Configurações da Janela
        setTitle("Gerenciamento de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Padding geral na janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL DO FORMULÁRIO (SUPERIOR) ---
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        Border paddingForm = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledForm = BorderFactory.createTitledBorder("Dados do Produto");
        panelForm.setBorder(BorderFactory.createCompoundBorder(titledForm, paddingForm));

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

        // --- PAINEL DOS BOTÕES (INFERIOR) ---
        JPanel panelButtons = new JPanel();
        Border paddingButtons = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border titledButtons = BorderFactory.createTitledBorder("Ações");
        panelButtons.setBorder(BorderFactory.createCompoundBorder(titledButtons, paddingButtons));

        btnCadastrar = new JButton("Cadastrar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar Campos");

        panelButtons.add(btnCadastrar);
        panelButtons.add(btnAlterar);
        panelButtons.add(btnExcluir);
        panelButtons.add(btnLimpar);

        add(panelButtons, BorderLayout.SOUTH);

        // --- TABELA DE PRODUTOS (CENTRAL) ---
        JPanel panelTable = new JPanel(new BorderLayout());
        Border paddingTable = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledTable = BorderFactory.createTitledBorder("Produtos Cadastrados");
        panelTable.setBorder(BorderFactory.createCompoundBorder(titledTable, paddingTable));


        // Instancia o DefaultTableModel sobrescrevendo o método isCellEditable
        tableModel = new DefaultTableModel(new Object[]{"Código", "Descrição", "Valor", "Estoque"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Impede que qualquer célula da tabela seja editada
                return false;
            }
        };


        tblProdutos = new JTable(tableModel);
        panelTable.add(new JScrollPane(tblProdutos), BorderLayout.CENTER);

        add(panelTable, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES E TABELA ---

        btnCadastrar.addActionListener(e -> cadastrarProduto());
        btnAlterar.addActionListener(e -> toggleAlterar()); // MUDANÇA
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

                    // --- MUDANÇA: Bloqueia campos e ajusta botões ---
                    setCamposEditaveis(false);
                    btnCadastrar.setEnabled(false);
                    btnAlterar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                    btnAlterar.setText("Alterar");
                }
            }
        });

        atualizarTabela();
        limparCampos(); // Define o estado inicial
        setLocationRelativeTo(null); // Centraliza
    }

    /**
     * Novo método para controlar a edição dos campos.
     * @param editavel true para habilitar, false para desabilitar.
     */
    private void setCamposEditaveis(boolean editavel) {
        txtDescricao.setEditable(editavel);
        txtValor.setEditable(editavel);
        txtEstoque.setEditable(editavel);
    }

    /**
     * Novo método que controla o clique no botão "Alterar" / "Salvar Alterações".
     */
    private void toggleAlterar() {
        // Se o botão estiver no modo "Alterar", apenas libera os campos
        if (btnAlterar.getText().equals("Alterar")) {
            btnAlterar.setText("Salvar Alterações");
            setCamposEditaveis(true);
            btnExcluir.setEnabled(false);
            btnLimpar.setEnabled(false);
        }
        // Se estiver no modo "Salvar Alterações", executa a lógica de salvar
        else {
            try {
                int codigo = Integer.parseInt(txtCodigo.getText());
                String descricao = txtDescricao.getText();
                BigDecimal valor = new BigDecimal(txtValor.getText());
                int estoque = Integer.parseInt(txtEstoque.getText());

                produtoControl.atualizarProduto(codigo, descricao, valor, estoque);

                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Reverte os botões e campos para o estado "visualizando"
                btnAlterar.setText("Alterar");
                setCamposEditaveis(false);
                btnExcluir.setEnabled(true);
                btnLimpar.setEnabled(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: Selecione um produto na tabela e verifique os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
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

    // O método alterarProduto() foi absorvido pelo toggleAlterar()

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

    /**
     * Método limparCampos atualizado para resetar o estado da tela
     */
    private void limparCampos() {
        txtCodigo.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        txtEstoque.setText("");
        tblProdutos.clearSelection();

        // --- MUDANÇA: Reseta o estado dos campos e botões ---
        setCamposEditaveis(true);
        btnCadastrar.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnLimpar.setEnabled(true);
        btnAlterar.setText("Alterar");
    }
}