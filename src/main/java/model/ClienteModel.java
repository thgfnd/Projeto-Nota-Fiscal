package model;

import java.math.BigDecimal;

public class ClienteModel {

    // 1. Atributos (variáveis)
    // Correspondem às colunas da tabela CLIENTE_01
    private int codigo;
    private String nome;
    private String endereco;
    private String telefone;
    private String cpf;
    private BigDecimal credito;

    // 2. Métodos Getters e Setters
    // São "portas" de acesso para ler e modificar os atributos

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getCredito() {
        return credito;
    }

    public void setCredito(BigDecimal credito) {
        this.credito = credito;
    }
}