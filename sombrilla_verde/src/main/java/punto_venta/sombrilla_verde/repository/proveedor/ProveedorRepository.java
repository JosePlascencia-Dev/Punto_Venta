package punto_venta.sombrilla_verde.repository.proveedor;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.proveedor.ProveedorEntity;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {
}
