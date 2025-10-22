package view;

import control.ClienteControl;
import model.ClienteModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class ClienteView extends JFrame {

    // 1. Componentes da Interface Gráfica (GUI)
    private JTextField txtNome, txtEndereco, txtTelefone, txtCpf, txtCredito, txtCodigo, txtSaldoDevedor;
    private JButton btnCadastrar, btnAlterar, btnExcluir, btnLimpar;
    private JTable tblClientes;
    private DefaultTableModel tableModel;

    // 2. Objeto de Controle
    private ClienteControl clienteControl;

    // --- CONSTRUTOR DA CLASSE: ONDE A TELA É MONTADA ---
    public ClienteView() {
        clienteControl = new ClienteControl();

        // Configurações da Janela
        setTitle("Gerenciamento de Clientes");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Padding geral na janela
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- PAINEL DO FORMULÁRIO (SUPERIOR) ---
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
        Border paddingForm = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledForm = BorderFactory.createTitledBorder("Dados do Cliente");
        panelForm.setBorder(BorderFactory.createCompoundBorder(titledForm, paddingForm));

        add(panelForm, BorderLayout.NORTH);

        // Adiciona os campos de texto e labels
        panelForm.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        txtCodigo.setEditable(false);
        panelForm.add(txtCodigo);

        panelForm.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panelForm.add(txtNome);

        panelForm.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField();
        panelForm.add(txtEndereco);

        panelForm.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        panelForm.add(txtTelefone);

        panelForm.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        panelForm.add(txtCpf);

        panelForm.add(new JLabel("Crédito (Limite):"));
        txtCredito = new JTextField();
        panelForm.add(txtCredito);

        panelForm.add(new JLabel("Saldo Devedor:"));
        txtSaldoDevedor = new JTextField();
        txtSaldoDevedor.setEditable(false);
        txtSaldoDevedor.setFont(new Font("Arial", Font.BOLD, 14));
        txtSaldoDevedor.setForeground(Color.RED);
        panelForm.add(txtSaldoDevedor);

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

        // --- TABELA DE CLIENTES (CENTRAL) ---
        JPanel panelTable = new JPanel(new BorderLayout());
        Border paddingTable = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border titledTable = BorderFactory.createTitledBorder("Clientes Cadastrados");
        panelTable.setBorder(BorderFactory.createCompoundBorder(titledTable, paddingTable));


        // Instancia o DefaultTableModel sobrescrevendo o método isCellEditable
        tableModel = new DefaultTableModel(new Object[]{"Código", "Nome", "Endereço", "Telefone", "CPF", "Crédito"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Impede que qualquer célula da tabela seja editada
                return false;
            }
        };
        //

        tblClientes = new JTable(tableModel);
        panelTable.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        add(panelTable, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES ---
        btnCadastrar.addActionListener(e -> cadastrarCliente());
        btnAlterar.addActionListener(e -> toggleAlterar());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnLimpar.addActionListener(e -> limparCampos());

        // --- AÇÃO DE CLIQUE NA TABELA ATUALIZADA ---
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblClientes.getSelectedRow();
                if (selectedRow != -1) {
                    txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtNome.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtEndereco.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtTelefone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtCpf.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtCredito.setText(tableModel.getValueAt(selectedRow, 5).toString());

                    int clienteCodigo = (int) tableModel.getValueAt(selectedRow, 0);
                    BigDecimal saldoDevedor = clienteControl.buscarSaldoDevedor(clienteCodigo);
                    txtSaldoDevedor.setText("R$ " + saldoDevedor);

                    // Bloqueia campos e ajusta botões
                    setCamposEditaveis(false);
                    btnCadastrar.setEnabled(false);
                    btnAlterar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                    btnAlterar.setText("Alterar");
                }
            }
        });

        atualizarTabela();
        limparCampos();
        setLocationRelativeTo(null);
    }

    /**
     * Novo método para controlar a edição dos campos.
     * @param editavel true para habilitar, false para desabilitar.
     */
    private void setCamposEditaveis(boolean editavel) {
        txtNome.setEditable(editavel);
        txtEndereco.setEditable(editavel);
        txtTelefone.setEditable(editavel);
        txtCpf.setEditable(editavel);
        txtCredito.setEditable(editavel);
    }

    /**
     * Novo método que controla o clique no botão "Alterar" / "Salvar Alterações".
     */
    private void toggleAlterar() {
        if (btnAlterar.getText().equals("Alterar")) {
            btnAlterar.setText("Salvar Alterações");
            setCamposEditaveis(true);
            btnExcluir.setEnabled(false);
            btnLimpar.setEnabled(false);
        }
        else {
            try {
                int codigo = Integer.parseInt(txtCodigo.getText());
                String nome = txtNome.getText();
                String endereco = txtEndereco.getText();
                String telefone = txtTelefone.getText();
                String cpf = txtCpf.getText();
                String creditoStr = txtCredito.getText().trim().isEmpty() ? "0" : txtCredito.getText().replace(",", ".");
                BigDecimal credito = new BigDecimal(creditoStr);

                clienteControl.atualizarCliente(codigo, nome, endereco, telefone, cpf, credito);

                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                btnAlterar.setText("Alterar");
                setCamposEditaveis(false);
                btnExcluir.setEnabled(true);
                btnLimpar.setEnabled(true);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Erro: Verifique se o campo 'Crédito' é um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- MÉTODOS DE AÇÃO ---
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<ClienteModel> clientes = clienteControl.buscarTodosClientes();
        for (ClienteModel cliente : clientes) {
            tableModel.addRow(new Object[]{
                    cliente.getCodigo(),
                    cliente.getNome(),
                    cliente.getEndereco(),
                    cliente.getTelefone(),
                    cliente.getCpf(),
                    cliente.getCredito()
            });
        }
    }

    private void cadastrarCliente() {
        try {
            String nome = txtNome.getText();
            String endereco = txtEndereco.getText();
            String telefone = txtTelefone.getText();
            String cpf = txtCpf.getText();
            String creditoStr = txtCredito.getText().trim().isEmpty() ? "0" : txtCredito.getText().replace(",", ".");
            BigDecimal credito = new BigDecimal(creditoStr);

            clienteControl.cadastrarCliente(nome, endereco, telefone, cpf, credito);

            limparCampos();
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: O campo 'Crédito' deve ser um número válido (use . ou , para centavos).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirCliente() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente selecionado?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                clienteControl.excluirCliente(codigo);
                limparCampos();
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Selecione um cliente na tabela para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método limparCampos atualizado para resetar o estado da tela
     */
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtCpf.setText("");
        txtCredito.setText("");
        txtSaldoDevedor.setText("");
        tblClientes.clearSelection();

        setCamposEditaveis(true);
        btnCadastrar.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnLimpar.setEnabled(true);
        btnAlterar.setText("Alterar");
    }
}