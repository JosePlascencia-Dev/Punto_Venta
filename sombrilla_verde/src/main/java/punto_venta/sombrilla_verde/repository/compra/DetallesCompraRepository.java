package punto_venta.sombrilla_verde.repository.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import punto_venta.sombrilla_verde.model.entity.compra.CompraEntity;
import punto_venta.sombrilla_verde.model.entity.compra.DetallesCompraEntity;

import java.util.List;

public interface DetallesCompraRepository extends JpaRepository<DetallesCompraEntity, Integer> {
    List<DetallesCompraEntity> findByCompra(CompraEntity compra);
}
