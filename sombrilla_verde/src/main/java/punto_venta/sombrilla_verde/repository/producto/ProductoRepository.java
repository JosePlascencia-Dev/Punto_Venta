package punto_venta.sombrilla_verde.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.producto.ProductoEntity;

public interface ProductoRepository  extends JpaRepository<ProductoEntity, Integer> {
}
