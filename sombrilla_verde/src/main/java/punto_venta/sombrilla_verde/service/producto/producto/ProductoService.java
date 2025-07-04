package punto_venta.sombrilla_verde.service.producto.producto;

import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;

import java.util.List;

public interface ProductoService {
    ProductoEntity save(ProductoEntity provedor);
    List<ProductoEntity> findAll();
    void deleteById(Integer id);
    ProductoEntity findById(Integer id);
    List<ProductoEntity> findAllOrderByExistenciaAsc();
    ProductoEntity findByNombre(String nombre);
    ProductoEntity findByCodigo(String codigo);
    List<ProductoEntity> buscarPorCategoriaYProveedor(Integer categoriaId, Integer proveedorId);
    List<ProductoEntity> findByProveedor(ProveedorEntity proveedor);
}
