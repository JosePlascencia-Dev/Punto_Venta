package punto_venta.sombrilla_verde.service.compra.compra;

import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;

import java.util.List;

public interface CompraService {
    CompraEntity save(CompraEntity compra);
    List<CompraEntity> findAll();
    void deleteById(Integer id);
    CompraEntity findById(Integer id);
}
