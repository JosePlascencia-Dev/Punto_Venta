package punto_venta.sombrilla_verde.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.math.BigDecimal;

public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
    @Query("SELECT COUNT(v) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    Long countVentasDelDia();
    @Query("SELECT SUM(v.total) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    BigDecimal totalVentasDelDia();
    @Query("""
        SELECT SUM(d.totalLinea - (p.precioCompra * d.cantidad)) AS gananciaTotal
        FROM ventas v
        JOIN v.detalles d
        JOIN d.producto p
        WHERE DATE(v.fechaVenta) = CURRENT_DATE
    """)
    BigDecimal calcularGananciaDelDia();
}
