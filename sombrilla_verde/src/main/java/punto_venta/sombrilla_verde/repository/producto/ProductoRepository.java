package punto_venta.sombrilla_verde.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

import java.util.List;

public interface ProductoRepository  extends JpaRepository<ProductoEntity, Integer> {
    @Query("SELECT p FROM productos p ORDER BY p.existencia ASC")
    List<ProductoEntity> findAllOrderByExistenciaAsc();
}
