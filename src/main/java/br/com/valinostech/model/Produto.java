package br.com.valinostech.model;



import jakarta.persistence.*;



@Entity

@Table(name = "produtos")

public class Produto {



    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;



    private String nome;

   

    @Column(columnDefinition = "TEXT")

    private String descricao;

   

    private double preco;

   

    // Campo como Integer para aceitar nulos do banco

    private Integer quantidade = 0;



    private String imagemUrl;



    private String fornecedorNome;

    private double precoCusto;

    private String codigoFornecedor;



    public Produto() {}



    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

   

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

   

    public String getDescricao() { return descricao; }

    public void setDescricao(String descricao) { this.descricao = descricao; }

   

    public double getPreco() { return preco; }

    public void setPreco(double preco) { this.preco = preco; }

   

    // CORRIGIDO: Getters e Setters agora usam Integer, igual à variável

    public Integer getQuantidade() { return quantidade; }

    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }



    public String getImagemUrl() { return imagemUrl; }

    public void setImagemUrl(String imagemUrl) { this.imagemUrl = imagemUrl; }



    public String getFornecedorNome() { return fornecedorNome; }

    public void setFornecedorNome(String fornecedorNome) { this.fornecedorNome = fornecedorNome; }

   

    public double getPrecoCusto() { return precoCusto; }

    public void setPrecoCusto(double precoCusto) { this.precoCusto = precoCusto; }

   

    public String getCodigoFornecedor() { return codigoFornecedor; }

    public void setCodigoFornecedor(String codigoFornecedor) { this.codigoFornecedor = codigoFornecedor; }

} 