package punto_venta.sombrilla_verde.service.compra.detalles_compras;

import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;

import java.util.List;

public interface DetallesCompraService {
    DetallesCompraEntity save(DetallesCompraEntity compra);
    List<DetallesCompraEntity> findAll();
    void deleteById(Integer id);
    DetallesCompraEntity findById(Integer id);
}
