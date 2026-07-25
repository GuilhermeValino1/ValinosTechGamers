package br.com.valinostech.repository;

import br.com.valinostech.model.Pedido;
import br.com.valinostech.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByUsuario(Usuario usuario);
    
    // Esta consulta soma apenas os pedidos que estão com status 'CONCLUIDO'
    // Se você mudar o status de um pedido para 'CANCELADO', 
    // ele automaticamente sai da soma do faturamento.
    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.status = 'CONCLUIDO'")
    Double calcularFaturamentoTotal();
    
}