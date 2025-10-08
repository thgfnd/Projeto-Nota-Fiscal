package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoModel {

    // Atributos correspondentes à tabela PEDIDO_02
    private int codigo;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;

    // Atributo para representar a Chave Estrangeira (FK)
    // Armazena o código do cliente que fez o pedido
    private int clienteCodigo;

    // Getters e Setters

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getClienteCodigo() {
        return clienteCodigo;
    }

    public void setClienteCodigo(int clienteCodigo) {
        this.clienteCodigo = clienteCodigo;
    }
}