package punto_venta.sombrilla_verde.repository.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RetiroEfectivoRepository extends JpaRepository<RetiroEfectivoEntity, Integer> {
    @Query("SELECT r FROM retiros_efectivo r WHERE r.fecha >= :fechaInicio ORDER BY r.fecha DESC")
    List<RetiroEfectivoEntity> findAllDesdeFecha(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT COALESCE(SUM(r.monto), 0) FROM retiros_efectivo r WHERE r.fecha >= :fechaInicio")
    BigDecimal sumMontoTotalDesdeFecha(@Param("fechaInicio") LocalDateTime fechaInicio);
}
