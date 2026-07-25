package br.com.valinostech.controller;

import br.com.valinostech.model.Produto;
import br.com.valinostech.repository.ProdutoRepository;
import br.com.valinostech.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public static class ItemCarrinho {
        private Produto produto;
        private int quantidade;
        private double subtotal;

        public ItemCarrinho(Produto produto, int quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.subtotal = produto.getPreco() * quantidade;
        }

        public Produto getProduto() { return produto; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { 
            this.quantidade = quantidade; 
            this.subtotal = this.produto.getPreco() * quantidade;
        }
        public double getSubtotal() { return subtotal; }
    }

    @SuppressWarnings("unchecked")
    private List<ItemCarrinho> obterCarrinho(HttpSession session) {
        List<ItemCarrinho> carrinho = (List<ItemCarrinho>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
            session.setAttribute("carrinho", carrinho);
        }
        return carrinho;
    }

    @GetMapping("/adicionar/{id}")
    public String adicionar(@PathVariable("id") Long id, HttpSession session) {
        Produto produto = produtoRepository.findById(id).orElse(null);
        if (produto != null) {
            List<ItemCarrinho> carrinho = obterCarrinho(session);
            carrinho.add(new ItemCarrinho(produto, 1));
        }
        return "redirect:/carrinho";
    }

    @GetMapping
    public String exibirCarrinho(Model model, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        double totalGeral = carrinho.stream().mapToDouble(ItemCarrinho::getSubtotal).sum();
        model.addAttribute("itens", carrinho);
        model.addAttribute("totalGeral", totalGeral);
        return "carrinho";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        if (carrinho.isEmpty()) return "redirect:/carrinho";
        
        double totalGeral = carrinho.stream().mapToDouble(ItemCarrinho::getSubtotal).sum();
        
        // Correção: inicializa o form para o Thymeleaf e guarda o total na sessão
        model.addAttribute("pagamentoForm", new PagamentoForm());
        session.setAttribute("totalGeral", totalGeral);
        
        model.addAttribute("itens", carrinho);
        model.addAttribute("totalGeral", totalGeral);
        return "checkout";
    }

    @GetMapping("/aumentar/{index}")
    public String aumentar(@PathVariable("index") int index, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        if (index >= 0 && index < carrinho.size()) {
            ItemCarrinho item = carrinho.get(index);
            item.setQuantidade(item.getQuantidade() + 1);
        }
        return "redirect:/carrinho";
    }

    @GetMapping("/diminuir/{index}")
    public String diminuir(@PathVariable("index") int index, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        if (index >= 0 && index < carrinho.size()) {
            ItemCarrinho item = carrinho.get(index);
            if (item.getQuantidade() > 1) item.setQuantidade(item.getQuantidade() - 1);
        }
        return "redirect:/carrinho";
    }

    @GetMapping("/remover/{index}")
    public String remover(@PathVariable("index") int index, HttpSession session) {
        List<ItemCarrinho> carrinho = obterCarrinho(session);
        if (index >= 0 && index < carrinho.size()) {
            carrinho.remove(index);
        }
        return "redirect:/carrinho";
    }
}