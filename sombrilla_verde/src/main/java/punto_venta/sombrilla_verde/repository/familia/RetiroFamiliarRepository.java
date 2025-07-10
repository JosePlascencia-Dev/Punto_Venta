package punto_venta.sombrilla_verde.repository.familia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface    RetiroFamiliarRepository extends JpaRepository<RetiroFamiliarEntity, Integer> {
    @Query("SELECT r FROM retiros_familiares r WHERE r.fecha >= :fechaInicio ORDER BY r.fecha DESC")
    List<RetiroFamiliarEntity> findAllDesdeFecha(@Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("SELECT COALESCE(SUM(r.totalCosto), 0) FROM retiros_familiares r WHERE r.fecha >= :fechaInicio")
    BigDecimal sumTotalCostoDesdeFecha(@Param("fechaInicio") LocalDateTime fechaInicio);
}
