package view;

import control.ClienteControl;
import model.ClienteModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class ClienteView extends JFrame {

    // 1. Componentes da Interface Gráfica (GUI)
    private JTextField txtNome, txtEndereco, txtTelefone, txtCpf, txtCredito, txtCodigo, txtSaldoDevedor; // Campo adicionado
    private JButton btnCadastrar, btnAlterar, btnExcluir, btnLimpar;
    private JTable tblClientes;
    private DefaultTableModel tableModel;

    // 2. Objeto de Controle
    private ClienteControl clienteControl;

    // --- CONSTRUTOR DA CLASSE: ONDE A TELA É MONTADA ---
    public ClienteView() {
        // Instancia o controle
        clienteControl = new ClienteControl();

        // Configurações da Janela
        setTitle("Gerenciamento de Clientes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DO FORMULÁRIO (SUPERIOR) ---
        JPanel panelForm = new JPanel(new GridLayout(0, 2, 10, 10));
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

        panelForm.add(new JLabel("Crédito:"));
        txtCredito = new JTextField();
        panelForm.add(txtCredito);

        // --- CAMPO DE SALDO DEVEDOR ADICIONADO ---
        panelForm.add(new JLabel("Saldo Devedor:"));
        txtSaldoDevedor = new JTextField();
        txtSaldoDevedor.setEditable(false);
        txtSaldoDevedor.setFont(new Font("Arial", Font.BOLD, 12));
        txtSaldoDevedor.setForeground(Color.RED);
        panelForm.add(txtSaldoDevedor);

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

        // --- TABELA DE CLIENTES (INFERIOR) ---
        tableModel = new DefaultTableModel(new Object[]{"Código", "Nome", "Endereço", "Telefone", "CPF", "Crédito"}, 0);
        tblClientes = new JTable(tableModel);
        add(new JScrollPane(tblClientes), BorderLayout.SOUTH);

        // --- AÇÕES DOS BOTÕES ---
        btnCadastrar.addActionListener(e -> cadastrarCliente());
        btnAlterar.addActionListener(e -> alterarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnLimpar.addActionListener(e -> limparCampos());

        // --- AÇÃO DE CLIQUE NA TABELA ATUALIZADA ---
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblClientes.getSelectedRow();
                if (selectedRow != -1) {
                    // Preenche os campos normais
                    txtCodigo.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtNome.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    txtEndereco.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    txtTelefone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtCpf.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    txtCredito.setText(tableModel.getValueAt(selectedRow, 5).toString());

                    // Busca e exibe o saldo devedor
                    int clienteCodigo = (int) tableModel.getValueAt(selectedRow, 0);
                    BigDecimal saldoDevedor = clienteControl.buscarSaldoDevedor(clienteCodigo);
                    txtSaldoDevedor.setText("R$ " + saldoDevedor);
                }
            }
        });

        // Atualiza a tabela com os dados do banco
        atualizarTabela();
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

    private void alterarCliente() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            String nome = txtNome.getText();
            String endereco = txtEndereco.getText();
            String telefone = txtTelefone.getText();
            String cpf = txtCpf.getText();
            String creditoStr = txtCredito.getText().trim().isEmpty() ? "0" : txtCredito.getText().replace(",", ".");
            BigDecimal credito = new BigDecimal(creditoStr);

            clienteControl.atualizarCliente(codigo, nome, endereco, telefone, cpf, credito);

            limparCampos();
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro: Selecione um cliente e verifique se o campo 'Crédito' é um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
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

    // --- MÉTODO LIMPAR CAMPOS ATUALIZADO ---
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtCpf.setText("");
        txtCredito.setText("");
        txtSaldoDevedor.setText(""); // Limpa o novo campo
    }

    // --- MÉTODO PRINCIPAL PARA EXECUTAR A TELA ---
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClienteView view = new ClienteView();
            view.setVisible(true);
        });
    }
}