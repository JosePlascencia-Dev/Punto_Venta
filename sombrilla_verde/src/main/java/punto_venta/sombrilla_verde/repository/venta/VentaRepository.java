package punto_venta.sombrilla_verde.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.math.BigDecimal;

public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
    @Query("SELECT COUNT(v) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    Long countVentasDelDia();
    @Query("SELECT SUM(v.total) FROM ventas v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    BigDecimal totalVentasDelDia();
    @Query("""
    SELECT 
        SUM(d.totalLinea - (p.precioCompra * d.cantidad)) 
        - COALESCE((SELECT SUM(r.monto) 
                    FROM retiros_efectivo r 
                    WHERE DATE(r.fecha) = CURRENT_DATE), 0)
    FROM ventas v
    JOIN v.detalles d
    JOIN d.producto p
    WHERE DATE(v.fechaVenta) = CURRENT_DATE
    """)
    BigDecimal calcularGananciaDelDia();

    @Query("""
      SELECT COALESCE(SUM(d.totalLinea), 0) 
      FROM detalle_venta d 
      WHERE d.producto.proveedor.id = :provId
        AND d.venta.fechaVenta > (
          SELECT COALESCE(MAX(c.fechaCompra), '1970-01-01T00:00:00')
          FROM compras_proveedor c
          WHERE c.proveedor.id = :provId
        )
      """)
    BigDecimal totalVentasDesdeUltimaCompra(@Param("provId") Integer proveedorId);

    @Query("""
      SELECT COALESCE(SUM(d.cantidad * p.precioCompra), 0)
      FROM detalle_venta d
      JOIN d.producto p
      WHERE p.proveedor.id = :provId
        AND d.venta.fechaVenta > (
          SELECT COALESCE(MAX(c.fechaCompra), '1970-01-01T00:00:00')
          FROM compras_proveedor c
          WHERE c.proveedor.id = :provId
        )
      """)
    BigDecimal costoVentasDesdeUltimaCompra(@Param("provId") Integer proveedorId);

}
