package punto_venta.sombrilla_verde.service.venta.venta;

import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.math.BigDecimal;
import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Integer id);
    VentaEntity findById(Integer id);
    Long countVentasDelDia();
    BigDecimal totalVentasDelDia();
    BigDecimal calcularGananciaDelDia();
    BigDecimal totalVentasDesdeUltimaCompra(Integer proveedorId);
    BigDecimal costoVentasDesdeUltimaCompra(Integer proveedorId);
}
