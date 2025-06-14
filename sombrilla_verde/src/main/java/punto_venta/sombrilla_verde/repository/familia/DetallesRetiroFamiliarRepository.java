package punto_venta.sombrilla_verde.repository.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.familia.DetalleRetiroFamiliarEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;

import java.util.List;

public interface DetallesRetiroFamiliarRepository extends JpaRepository<DetalleRetiroFamiliarEntity, Integer> {
    List<DetalleRetiroFamiliarEntity> findByRetiro(RetiroFamiliarEntity retiro);
}
