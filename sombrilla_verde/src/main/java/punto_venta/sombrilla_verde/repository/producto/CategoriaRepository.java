package punto_venta.sombrilla_verde.repository.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.producto.CategoriaEntity;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
}
