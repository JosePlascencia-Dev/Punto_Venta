package punto_venta.sombrilla_verde.service.historial;

import punto_venta.sombrilla_verde.model.entity.historial.HistorialProductosEntity;

import java.util.List;

public interface HistorialProductoService {
    HistorialProductosEntity save(HistorialProductosEntity provedor);
    List<HistorialProductosEntity> findAll();
    void deleteById(Long id);
    HistorialProductosEntity findById(Long id);
}
