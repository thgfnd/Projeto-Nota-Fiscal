package model;

import java.math.BigDecimal;

public class ItemModel {

    // Atributos correspondentes à tabela ITEM_04
    private int codigo;
    private int quantidade;
    private BigDecimal valorItem;

    // Atributos para as Chaves Estrangeiras (FKs)
    private int pedidoCodigo;
    private int produtoCodigo;

    // Getters e Setters

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorItem() {
        return valorItem;
    }

    public void setValorItem(BigDecimal valorItem) {
        this.valorItem = valorItem;
    }

    public int getPedidoCodigo() {
        return pedidoCodigo;
    }

    public void setPedidoCodigo(int pedidoCodigo) {
        this.pedidoCodigo = pedidoCodigo;
    }

    public int getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(int produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }
}