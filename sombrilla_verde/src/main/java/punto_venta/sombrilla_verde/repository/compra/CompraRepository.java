package punto_venta.sombrilla_verde.repository.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;

public interface CompraRepository extends JpaRepository<CompraEntity, Integer> {
}
