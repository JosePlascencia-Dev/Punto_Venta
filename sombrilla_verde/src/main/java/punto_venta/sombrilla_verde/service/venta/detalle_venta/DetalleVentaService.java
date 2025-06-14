package punto_venta.sombrilla_verde.service.venta.detalle_venta;

import punto_venta.sombrilla_verde.model.entity.venta.DetalleVentaEntity;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

import java.util.List;

public interface DetalleVentaService {
    DetalleVentaEntity save(DetalleVentaEntity venta);
    List<DetalleVentaEntity> findAll();
    void deleteById(Integer id);
    DetalleVentaEntity findById(Integer id);
    List<DetalleVentaEntity> findByVenta(VentaEntity venta);
}
