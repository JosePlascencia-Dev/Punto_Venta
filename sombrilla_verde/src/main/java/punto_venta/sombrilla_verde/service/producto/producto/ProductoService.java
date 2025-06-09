package punto_venta.sombrilla_verde.service.producto.producto;

import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity provedor);
    List<ProductoEntity> findAll();
    void deleteById(Integer id);
    ProductoEntity findById(Integer id);
    List<ProductoEntity> findAllOrderByExistenciaAsc();
}
