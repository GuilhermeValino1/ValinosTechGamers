package br.com.valinostech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private double valorTotal;
    private LocalDateTime dataPedido;
    private String status;
    private String metodoPagamento;
    private String idTransacaoGateway;

    @ManyToMany
    private List<Produto> produtos;

    public Pedido() {
        this.dataPedido = LocalDateTime.now();
        this.status = "AGUARDANDO PAGAMENTO";
    }

    // GETTERS E SETTERS COMPLETOS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public String getIdTransacaoGateway() { return idTransacaoGateway; }
    public void setIdTransacaoGateway(String idTransacaoGateway) { this.idTransacaoGateway = idTransacaoGateway; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}