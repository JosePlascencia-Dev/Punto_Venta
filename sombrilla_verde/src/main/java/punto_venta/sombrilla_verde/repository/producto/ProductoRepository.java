package punto_venta.sombrilla_verde.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;

import java.util.List;

public interface ProductoRepository  extends JpaRepository<ProductoEntity, Integer> {
    @Query("SELECT p FROM productos p WHERE " + "(:idCategoria IS NULL OR p.categoria.id = :idCategoria) AND " + "(:idProveedor IS NULL OR p.proveedor.id = :idProveedor)")
    List<ProductoEntity> buscarPorCategoriaYProveedor(
            @Param("idCategoria") Integer idCategoria,
            @Param("idProveedor") Integer idProveedor);
    @Query("SELECT p FROM productos p ORDER BY p.existencia ASC")
    List<ProductoEntity> findAllOrderByExistenciaAsc();
    ProductoEntity findByNombre(String nombre);
    ProductoEntity findByCodigo(String codigo);
    List<ProductoEntity> findByProveedor(ProveedorEntity proveedor);
}
