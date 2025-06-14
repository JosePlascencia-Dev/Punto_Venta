package punto_venta.sombrilla_verde.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Integer> {
    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
}
