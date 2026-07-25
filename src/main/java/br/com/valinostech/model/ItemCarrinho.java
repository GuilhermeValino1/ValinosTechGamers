package br.com.valinostech.model;

public class ItemCarrinho {
    private Produto produto;
    private Integer quantidade;

    public ItemCarrinho(Produto produto, Integer quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    // Método para calcular o subtotal deste item automático
    public Double getSubtotal() {
        return this.produto.getPreco() * this.quantidade;
    }

    // Getters e Setters
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}