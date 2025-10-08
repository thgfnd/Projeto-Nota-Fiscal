package control;

import model.ClienteModel;
import persistence.ClientePersistence;
import persistence.PedidoPersistence; // <<-- IMPORT ADICIONADO

import java.math.BigDecimal;
import java.util.List;

public class ClienteControl {

    // Cria uma instância da camada de persistência. A Control usará a Persistence para manipular os dados.
    private ClientePersistence clientePersistence;

    // Construtor
    public ClienteControl() {
        this.clientePersistence = new ClientePersistence();
    }

    // --- MÉTODO PARA CADASTRAR UM NOVO CLIENTE ---
    public void cadastrarCliente(String nome, String endereco, String telefone, String cpf, BigDecimal credito) {
        // Regra de Negócio: Antes de cadastrar, podemos validar os dados.
        // Por exemplo, verificar se o nome não está vazio e se o CPF é válido.
        if (nome == null || nome.trim().isEmpty()) {
            System.err.println("Erro: O nome do cliente não pode ser vazio.");
            return; // Interrompe a execução se o nome for inválido
        }

        // Cria um objeto ClienteModel com os dados recebidos
        ClienteModel novoCliente = new ClienteModel();
        novoCliente.setNome(nome);
        novoCliente.setEndereco(endereco);
        novoCliente.setTelefone(telefone);
        novoCliente.setCpf(cpf);
        novoCliente.setCredito(credito);

        // Chama o método da camada de persistência para inserir o cliente no banco
        clientePersistence.inserirCliente(novoCliente);
    }

    // --- MÉTODO PARA BUSCAR E RETORNAR TODOS OS CLIENTES ---
    public List<ClienteModel> buscarTodosClientes() {
        // Simplesmente repassa a solicitação para a camada de persistência
        return clientePersistence.listarClientes();
    }

    // --- MÉTODO PARA ATUALIZAR UM CLIENTE EXISTENTE ---
    public void atualizarCliente(int codigo, String nome, String endereco, String telefone, String cpf, BigDecimal credito) {
        // Validação simples
        if (codigo <= 0) {
            System.err.println("Erro: Código do cliente inválido.");
            return;
        }

        ClienteModel clienteAtualizado = new ClienteModel();
        clienteAtualizado.setCodigo(codigo);
        clienteAtualizado.setNome(nome);
        clienteAtualizado.setEndereco(endereco);
        clienteAtualizado.setTelefone(telefone);
        clienteAtualizado.setCpf(cpf);
        clienteAtualizado.setCredito(credito);

        // Chama o método da camada de persistência para alterar o cliente
        clientePersistence.alterarCliente(clienteAtualizado);
    }

    // --- MÉTODO PARA EXCLUIR UM CLIENTE ---
    public void excluirCliente(int codigo) {
        // Validação
        if (codigo <= 0) {
            System.err.println("Erro: Código do cliente inválido.");
            return;
        }
        // Repassa a solicitação de remoção para a camada de persistência
        clientePersistence.removerCliente(codigo);
    }

    // --- NOVO MÉTODO PARA BUSCAR O SALDO DEVEDOR ---
    public BigDecimal buscarSaldoDevedor(int clienteCodigo) {
        // A camada de controle simplesmente repassa a chamada para a camada de persistência correta.
        // Como o saldo é a soma dos pedidos, a lógica está na PedidoPersistence.
        PedidoPersistence pedidoPersistence = new PedidoPersistence();
        return pedidoPersistence.buscarSaldoDevedorCliente(clienteCodigo);
    }
}