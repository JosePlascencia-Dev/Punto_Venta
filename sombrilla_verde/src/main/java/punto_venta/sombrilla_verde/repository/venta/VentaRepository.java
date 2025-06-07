package punto_venta.sombrilla_verde.repository.venta;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.venta.VentaEntity;

public interface VentaRepository extends JpaRepository<VentaEntity, Integer> {
}
