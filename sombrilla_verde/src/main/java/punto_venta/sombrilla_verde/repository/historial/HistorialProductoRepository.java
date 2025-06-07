package punto_venta.sombrilla_verde.repository.historial;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.historial.HistorialProductosEntity;

public interface HistorialProductoRepository extends JpaRepository<HistorialProductosEntity, Long> {
}
