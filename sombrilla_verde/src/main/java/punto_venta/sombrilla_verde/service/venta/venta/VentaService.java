package punto_venta.sombrilla_verde.service.venta.venta;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VentaService {
    VentaEntity save(VentaEntity venta);
    List<VentaEntity> findAll();
    void deleteById(Integer id);
    VentaEntity findById(Integer id);
    Long countVentasDelDia();
    BigDecimal totalVentasDelDia();
    BigDecimal calcularGananciaDelDia();
    BigDecimal totalVentasDesdeFecha(Integer proveedorId, LocalDateTime fechaInicio);
    BigDecimal costoVentasDesdeFecha(Integer proveedorId, LocalDateTime fechaInicio);
}
