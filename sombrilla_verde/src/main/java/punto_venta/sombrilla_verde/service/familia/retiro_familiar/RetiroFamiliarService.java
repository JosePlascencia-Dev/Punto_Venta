package punto_venta.sombrilla_verde.service.familia.retiro_familiar;

import org.springframework.data.repository.query.Param;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroEfectivoEntity;
import punto_venta.sombrilla_verde.model.entity.familia.RetiroFamiliarEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface RetiroFamiliarService {
    RetiroFamiliarEntity save(RetiroFamiliarEntity retiroFamiliar);
    List<RetiroFamiliarEntity> findAll();
    void deleteById(Integer id);
    RetiroFamiliarEntity findById(Integer id);
    List<RetiroFamiliarEntity> findAllDesdeFecha(LocalDateTime fechaInicio);
    BigDecimal sumTotalCostoDesdeFecha( LocalDateTime fechaInicio);
}
